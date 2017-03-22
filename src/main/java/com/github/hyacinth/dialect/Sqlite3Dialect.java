package com.github.hyacinth.dialect;

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
}
