package com.github.hyacinth;

import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.github.hyacinth.sql.TotalColumn;
import com.github.hyacinth.sql.BuildKit;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/16
 * Time: 10:34
 */
public class OtherTest {

    @Test
    public void test(){
        String sql = "select (select c1 from table1 where a in (select (select id from tab1) as t1 from tab) ) as a, (select c2 from table2) as b  from market_price where category_code = (select category_code from market_price order by category_code desc limit 1) order by price desc";

        StringBuilder sqlBuilder = new StringBuilder(sql);
        int from = sqlBuilder.indexOf(" from ");
        int sign1 = sqlBuilder.indexOf("(");

        int sign2 = sqlBuilder.indexOf(")");

        while (from > sign1++ && from < sign2++) {
            while((sign1 = sqlBuilder.indexOf("(", ++sign1)) < sign2){
                sign2 = sqlBuilder.indexOf(")", ++sign2);
            }
            sign2 = sqlBuilder.indexOf("(", sign2);
            from = sqlBuilder.indexOf(" from ", sign2);
            sign2 = sqlBuilder.indexOf(")", sign2);
        }

        System.out.println(sqlBuilder.substring(0, from));
    }


    @Test
    public void test2() throws JSQLParserException {
        //String fixed = "select (select c1 from table1 where a in (select (select id from tab1) as t1 from tab) ) as a, (select c2 from table2) as b  from market_price where category_code = (select category_code from market_price order by category_code desc limit 1) order by price desc";
        String sql = "select * from table1 where a = (select b from table2 order by cb desc limit 1) and d = ? and f = 'order by asc' order by abc";
//        String fixed = "(select sdf from sdfd) UNION (select * from k)";
//        CCJSqlParserManager parserManager = new CCJSqlParserManager();
//        Select select = (Select) parserManager.parse(new StringReader(fixed));
        Statement stmt = CCJSqlParserUtil.parse(sql);
        Select select = (Select) stmt;
        SelectBody selectBody = select.getSelectBody();
        processSelectBody(selectBody);
        System.out.println(select.toString());
    }

    @Test
    public void test3() throws JSQLParserException {
        String sql1 = "select * from table1 where a = (select b from table2 order by cb desc limit 1) and d = ? and f = ' select order by asc' order by ?";
        String sql2 = "select a, b, c, (select name from table2 t2 where t2.d = t1.d) as d from table1 t1";
        String sql3 = "select (select c1 from table1 where a in (select (select id from tab1 where a ='fewa ( fewa' and b = ' from ') as t1 from tab) ) as a, (select c2 from table2) as b  from market_price where category_code = (select category_code from market_price order by category_code desc limit 1) order by price desc";
        String sql = "(select * from ad where sort = 12)\n" +
                "union\n" +
                "(select * from ad where sort = 10)\n" +
                "\n" +
                "order by id asc";

        Statement stmt = CCJSqlParserUtil.parse(sql);
        Select select = (Select) stmt;

        SelectBody selectBody = select.getSelectBody();
        if(selectBody instanceof PlainSelect){
            PlainSelect plainSelect = (PlainSelect) selectBody;
            plainSelect.setOrderByElements(null);

            plainSelect.setSelectItems(null);

            SelectItem selectItem = new TotalColumn();
            List<SelectItem> selectItems = new ArrayList<SelectItem>();
            selectItems.add(selectItem);

            plainSelect.setSelectItems(selectItems);
        }
        System.out.println(select.toString());

    }

    @Test
    public void test4(){
//        String fixed = "select (select c1 from table1 where a in (select (select id from tab1) as t1 from tab) ) as a, (select c2 from table2) as b  from market_price where category_code = (select category_code from market_price order by category_code desc limit 1) order by price desc";
        String sql = "select * from table1 where a = (select b from table2 order by cb desc limit 1) and d = ? and f = 'order by asc' order by ?";

        System.out.println(get_sql_select_count(sql));
    }

    @Test
    public void test5() throws JSQLParserException {
        String sql = "insert into myschama.tabName2 (col1, col2, col3) VALUES ('sdgf', ?, ?)";
        Statement stmt = CCJSqlParserUtil.parse(sql);
        if(stmt instanceof Select){
            Select select = (Select) stmt;
        }
    }

    @Test
    public void test6(){
        String sql = "select sex from student group by sex order by sex";
        String sql2 = "select a, " +
                "b,  " +
                "c, " +
                "(select name from table2 t2 where t2.d = t1.d) as d " +
                "from table1 t1 where a = ? " +
                "group by a,b,c " +
                "order by b desc";
        Long start,end;
        start = System.currentTimeMillis();
        for(int i = 0; i<1; i++){
            BuildKit.buildTotalSql(sql2);
        }
        end = System.currentTimeMillis();
        System.out.println("JSqlParser:" + (end-start));


        System.out.println(BuildKit.buildTotalSql(sql2));
    }

    @Test
    public void test7() throws JSQLParserException {
        String sql = "SELECT count(*) from ((SELECT * FROM ad WHERE sort = 12) UNION (SELECT * FROM ad WHERE sort = 10)) hyacinth_tmp0 order by fewa asc";
        Statement stmt = CCJSqlParserUtil.parse(sql);
        Select select = (Select) stmt;
        System.out.println(select.toString());
    }

    public static String get_sql_select_count(String sql) {
        String count_sql = sql.replaceAll("\\s+", " ");
        int pos = count_sql.toLowerCase().indexOf(" from ");
        count_sql = count_sql.substring(pos);

        pos = count_sql.toLowerCase().lastIndexOf(" order by ");
        int lastpos = count_sql.toLowerCase().lastIndexOf(")");
        if (pos != -1 && pos > lastpos) {
            count_sql = count_sql.substring(0, pos);
        }

        String regex = "(left|right|inner) join (fetch )?\\w+(\\.\\w+)*";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        count_sql = p.matcher(count_sql).replaceAll("");

        count_sql = "select count(*) " + count_sql;
        return count_sql;


    }

    @Test
    public void test8(){

        String sql = " select eventId,eventKey,eventName,flag from event where eventId = ? and eventKey = ? and eventName = ?";
        String sql2 = "select a, b,  c, (select name from table2 t2 where t2.d = t1.d) as d from table1 t1 where a = ? group by a,b,c order by b desc";
        String sql3 = "(select * from ad where sort = 12)\n" +
                "union\n" +
                "(select * from ad where sort = 10)\n" +
                "\n" +
                "order by id asc";
        long start, end;
        start = System.currentTimeMillis();
        //使用mysql解析
        MySqlStatementParser sqlStatementParser = new MySqlStatementParser(sql2) ;
        //解析select查询
        SQLSelectStatement sqlStatement = (SQLSelectStatement) sqlStatementParser.parseSelect();
        SQLSelect sqlSelect = sqlStatement.getSelect() ;

        //获取sql查询块
        MySqlSelectQueryBlock sqlSelectQuery = (MySqlSelectQueryBlock) sqlSelect.getQuery();
        sqlSelectQuery.setOrderBy(null);
        sqlStatement.toString();

        end = System.currentTimeMillis();
        System.out.println(end - start);

//        StringBuffer out = new StringBuffer() ;
        //创建sql解析的标准化输出
//        SQLASTOutputVisitor sqlastOutputVisitor = SQLUtils.createFormatOutputVisitor(out , null , JdbcUtils.MYSQL) ;
//
//        //解析select项
//        out.delete(0, out.length()) ;
//        for (SQLSelectItem sqlSelectItem : sqlSelectQuery.getSelectList()) {
//            if(out.length()>1){
//                out.append(",") ;
//            }
//            sqlSelectItem.accept(sqlastOutputVisitor);
//        }
//        System.out.println("SELECT "+out) ;
//
//        //解析from
//        out.delete(0, out.length()) ;
//        sqlSelectQuery.getFrom().accept(sqlastOutputVisitor) ;
//        System.out.println("FROM "+out) ;
//
//        //解析where
//        out.delete(0, out.length()) ;
//        sqlSelectQuery.getWhere().accept(sqlastOutputVisitor) ;
//        System.out.println("WHERE "+out);



    }

    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                List<SelectBody> plainSelects = operationList.getSelects();
                for (SelectBody plainSelect : plainSelects) {
                    processPlainSelect((PlainSelect) plainSelect);
                }
            }
            if (!orderByHashParameters(operationList.getOrderByElements())) {
                operationList.setOrderByElements(null);
            }
        }
    }

    public void processPlainSelect(PlainSelect plainSelect) {
        if (!orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    public void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoin() != null) {
                if (subJoin.getJoin().getRightItem() != null) {
                    processFromItem(subJoin.getJoin().getRightItem());
                }
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {

        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = (SubSelect) (lateralSubSelect.getSubSelect());
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
        //Table时不用处理
    }

    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().toUpperCase().contains("?")) {
                return true;
            }
        }
        return false;
    }

}
