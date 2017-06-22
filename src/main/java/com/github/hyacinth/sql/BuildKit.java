package com.github.hyacinth.sql;

import com.github.hyacinth.HyacinthException;
import com.github.hyacinth.tools.StringTools;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Sql生成器，主要用于自动生成查询总条数的Sql语句
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/16
 * Time: 17:57
 */
public class BuildKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildKit.class);

    /**
     * 生成查询总条数的sql 并去掉order by
     * <p>
     * Example:
     * select a, b, c, (select name from table2 t2 where t2.d = t1.d) as d from table1 t1 order by t1.a desc
     * <p>
     * return: SELECT COUNT(*) AS COUNT FROM table1 t1
     *
     * @param sql 原查询sql
     * @return count fixed
     */
    public static String buildTotalSql(String sql) {
        //处理UNION、UNION ALL 的情况，需要求UNION部分的sql用（号包起来
        if (sql.startsWith("(")) {
            return newTotalSql(sql);
        }

        StringBuilder sqlBuilder = new StringBuilder(sql);
        String lowerCaseSql = sql.toLowerCase();
        int selectIndex = 6, fromIndex = 4;
        while (true) {
            fromIndex = lowerCaseSql.indexOf("from", fromIndex);
            char lastChar = lowerCaseSql.charAt(fromIndex - 1);
            char nextChar = lowerCaseSql.charAt(fromIndex + 4);
            while (!((lastChar == ' ' || lastChar == ')') && (nextChar == ' ' || nextChar == '('))) {
                fromIndex = lowerCaseSql.indexOf("from", fromIndex + 3);
                lastChar = lowerCaseSql.charAt(fromIndex - 1);
                nextChar = lowerCaseSql.charAt(fromIndex + 4);
            }
            selectIndex = lowerCaseSql.indexOf("select", selectIndex);
            lastChar = lowerCaseSql.charAt(selectIndex - 1);
            nextChar = lowerCaseSql.charAt(selectIndex + 6);
            while (!((lastChar == ' ' || lastChar == '(') && nextChar == ' ')) {
                selectIndex = lowerCaseSql.indexOf("select", selectIndex + 5);
                lastChar = lowerCaseSql.charAt(selectIndex - 1);
                nextChar = lowerCaseSql.charAt(selectIndex + 6);
            }
            if (selectIndex == -1 || selectIndex > fromIndex) {
                break;
            }
            fromIndex = fromIndex + 3;
            selectIndex = selectIndex + 5;
        }

        int orderIndex = lowerCaseSql.lastIndexOf("order ");
        if ((orderIndex > lowerCaseSql.lastIndexOf(")"))
                && (lowerCaseSql.charAt(orderIndex - 1) == ' ' || lowerCaseSql.charAt(orderIndex - 1) == ')')) {
            sqlBuilder.delete(orderIndex, lowerCaseSql.length() - 1);
        }
        sqlBuilder = sqlBuilder.replace(6, fromIndex, " COUNT(*) AS total ");

        return sqlBuilder.toString();
    }


    public static void main(String[] args) {

        String sql = "select (select (select iselectd from tafrombselectle4) as t3 from tafromble3) as tfrom1, (select * from table2) as t1 from table1 WHERE id = (select id from table4 order by abc limit 1)order by likjux;";
        System.out.println(buildTotalSql(sql));

        long end, start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            buildTotalSql(sql);
        }

        end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        for(int i =0; i< 100000; i++){
            buildTotalSql2(sql);
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static String buildTotalSql2(String sql) {
        Select select = parserSqlforSelect(sql);
        if (select == null) {
            throw new HyacinthException("Total SQL generated failure");
        }

        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            //去除排序
            plainSelect.setOrderByElements(null);
            //获取sql分组信息
            List<Expression> groupItems = plainSelect.getGroupByColumnReferences();
            if (groupItems != null && !groupItems.isEmpty()) {
                return newTotalSql(select.toString());
            } else {
                //替换原有SelectItem 为 TotalColumn @a,b,c -> count(*)
                SelectItem selectItem = new TotalColumn();
                List<SelectItem> selectItems = new ArrayList<SelectItem>();
                selectItems.add(selectItem);

                plainSelect.setSelectItems(selectItems);
            }
        } else if (selectBody instanceof SetOperationList) {
            //处理多个union、union all组合查询
            SetOperationList setOperationList = (SetOperationList) selectBody;
            setOperationList.setOrderByElements(null);
            return newTotalSql(select.toString());
        }

        return select.toString();
    }

    /**
     * 解析Sql
     *
     * @param sql
     * @return
     */
    public static Select parserSqlforSelect(String sql) {
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        if (!(statement instanceof Select)) return null;
        return (Select) statement;
    }

    /**
     * @see #cutOrderBy(SelectBody)
     */
    public static String cutOrderBy(String sql) {
        Select select = parserSqlforSelect(sql);
        //解析失败，返回原sql
        if (select == null) return sql;
        cutOrderBy(select.getSelectBody());
        return select.toString();
    }

    /**
     * 除去Sql末尾Order By
     *
     * @param selectBody
     */
    private static void cutOrderBy(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            //去除排序
            plainSelect.setOrderByElements(null);
        } else if (selectBody instanceof SetOperationList) {
            //处理多个union、union all组合查询
            SetOperationList setOperationList = (SetOperationList) selectBody;
            setOperationList.setOrderByElements(null);
        }
    }

    private static String newTotalSql(String sql) {
        return new StringBuilder("SELECT COUNT(*) AS total FROM (").append(sql).append(") hyacinth_alias0").toString();
    }

    /**
     * 模板参数化处理
     *
     * @param paras
     * @param sql
     * @param parasList
     */
    public static void parameterizedRender(Map<String, Object> paras, StringBuilder sql, List<Object> parasList) {
        int start, end = -1; //#index
        while (true) {
            //获取动态参数‘{’，‘}’索引位置
            start = sql.indexOf("{", ++end);
            end = sql.indexOf("}", start + 1);

            if (start == -1 || end == -1) break;

            //获取参数变量值
            String keyStr = sql.substring(start + 1, end).trim();
            Object value = getParamsValue(keyStr, paras);

            //参数值处理
            char sign = sql.charAt(start - 1);
            if (sign == '#') {
                parasList.add(value);
                //将参数化表达式替换成sql参数占位符
                sql.replace(start - 1, end + 1, "?");
                //重置结束位置
                end = start;
            } else if (sign == '@') {
                if (value == null) {
                    LOGGER.error("@{{}} The parameter cannot be empty!", keyStr);
                    continue;
                }
                StringBuilder multipleBuilder = new StringBuilder("(");
                if (value.getClass().isArray()) { //数组
                    Object[] objArray = (Object[]) value;
                    for (int i = 0; i < objArray.length; i++) {
                        parasList.add(objArray[i]);
                        multipleBuilder.append("?,");
                    }
                } else if (value instanceof Collection) { //集合
                    Iterator iterator = ((Collection) value).iterator();
                    while (iterator.hasNext()) {
                        parasList.add(iterator.next());
                        multipleBuilder.append("?,");
                    }
                } else {
                    LOGGER.error("@{{}} The parameter must be an array or collection!", keyStr);
                    continue;
                }
                if (multipleBuilder.length() > 0) {
                    multipleBuilder.deleteCharAt(multipleBuilder.length() - 1).append(")");
                }
                sql.replace(start - 1, end + 1, multipleBuilder.toString());
                //重置结束位置
                end = start + multipleBuilder.length();
            }
        }
    }

    /**
     * 根据key从参数map集合中获取对应的值
     *
     * @param key
     * @param params
     * @return
     */
    private static Object getParamsValue(String key, Map<String, Object> params) {
        Object value = params.get(key);
        if (value == null) {
            //user.orders.order.id
            String[] keys = StringTools.split(key, '.');
            int keysIndexs = keys.length - 1;

            if (keysIndexs == 0) return null;
            Map temp = params;
            for (int i = 0; i < keys.length; i++) {
                if (keysIndexs != i) {
                    temp = (Map) temp.get(keys[i]);
                    if (temp == null) break;
                } else {
                    value = temp.get(keys[i]);
                }
            }
        }
        return value;
    }

}
