package com.github.hyacinth.dialect;

import com.github.hyacinth.Record;
import com.github.hyacinth.Table;

import java.util.List;
import java.util.Map;

/**
 * SqlServer方言
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 16:06
 */
public class SqlServerDialect extends Dialect {

    public String forTableBuilderDoBuild(String tableName) {
        return "select * from " + tableName + " where 1 = 2";
    }

    /**
     * sql.replaceFirst("(?i)select", "") 正则中带有 "(?i)" 前缀，指定在匹配时不区分大小写
     */
    public String forPaginate(int pageNumber, int pageSize, String sql) {
        int end = pageNumber * pageSize;
        if (end <= 0) {
            end = pageSize;
        }
        int begin = (pageNumber - 1) * pageSize;
        if (begin < 0) {
            begin = 0;
        }
        StringBuilder ret = new StringBuilder();
        ret.append("SELECT * FROM ( SELECT row_number() over (order by tempcolumn) temprownumber, * FROM ");
        ret.append(" ( SELECT TOP ").append(end).append(" tempcolumn=0,");
        ret.append(sql.replaceFirst("(?i)select", ""));
        ret.append(")vip)mvp where temprownumber>").append(begin);
        return ret.toString();
    }

    @Override
    public void forModelSaveOrUpdate(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {

    }

    @Override
    public void forDbSaveOrUpdate(String tableName, String[] pKeys, Record record, StringBuilder sql, List<Object> paras) {

    }
}
