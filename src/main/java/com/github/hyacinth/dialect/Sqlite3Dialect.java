package com.github.hyacinth.dialect;

import com.github.hyacinth.Table;

import java.util.List;
import java.util.Map;

/**
 * Sqlite方言
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 16:05
 */
public class Sqlite3Dialect extends Dialect {

    public String forTableBuilderDoBuild(String tableName) {
        return "select * from " + tableName + " where 1 = 2";
    }

    public String forPaginate(int pageNumber, int pageSize, String sql) {
        int offset = pageSize * (pageNumber - 1);
        StringBuilder ret = new StringBuilder(sql);
        ret.append(" limit ").append(offset).append(", ").append(pageSize);
        return ret.toString();
    }

    @Override
    public void forModelSaveOrUpdate(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {

    }

    @Override
    public void forDbSaveOrUpdate(String tableName, String[] pKeys, Map<String, Object> record, StringBuilder sql, List<Object> paras) {

    }
}
