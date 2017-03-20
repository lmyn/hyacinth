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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sql生成器，主要用于自动生成查询总条数的Sql语句
 * <p>
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/16
 * Time: 17:57
 */
public class SqlBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlBuilder.class);

    /**
     * 生成查询总条数的sql 并去掉order by
     * <p>
     * Example:
     * select a, b, c, (select name from table2 t2 where t2.d = t1.d) as d from table1 t1 order by t1.a desc
     * <p>
     * return: SELECT COUNT(*) AS COUNT FROM table1 t1
     *
     * @param sql       原查询sql
     * @return count fixed
     */
    public static String buildTotalSql(String sql) {
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
            if(groupItems.size() > 0){
                return newTotalSql(select.toString());
            }else{
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

    private static String newTotalSql(String sql){
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
            //下一个动态参数扫描的起始位置

            //获取参数变量值
            String keyStr = sql.substring(start + 1, end).trim();
            Object value = getParamsValue(keyStr, paras);

            //参数值处理
            char sign = sql.charAt(start - 1);
            if (sign == '#') {
                parasList.add(value);
                //将参数化表达式替换成sql参数占位符
                sql.replace(start - 1, end + 1, "?");
            } else if (sign == '@') {
                StringBuilder multipleBuilder = new StringBuilder();
                //处理数组
                if (value != null && value.getClass().isArray()) {
                    Object[] objArray = (Object[]) value;
                    for (int i = 0; i < objArray.length; i++) {
                        parasList.add(objArray[i]);
                        multipleBuilder.append("?,");
                    }
                }
                if (multipleBuilder.length() > 0) {
                    multipleBuilder.deleteCharAt(multipleBuilder.length() - 1);
                }
                sql.replace(start - 1, end, multipleBuilder.toString());
            } else {

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
