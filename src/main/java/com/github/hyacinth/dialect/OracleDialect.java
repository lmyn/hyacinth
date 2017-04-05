package com.github.hyacinth.dialect;

import com.github.hyacinth.Record;
import com.github.hyacinth.Table;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Oracle方言
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 16:02
 */
public class OracleDialect extends Dialect {

    public String forTableBuilderDoBuild(String tableName) {
        return "select * from " + tableName + " where rownum < 1";
    }

    // insert into table (id,name) values(seq.nextval, ？)
    public void forModelSave(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
        sql.append("insert into ").append(table.getName()).append("(");
        StringBuilder temp = new StringBuilder(") values(");
        String[] pKeys = table.getPrimaryKey();
        int count = 0;
        for (Map.Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();
            if (table.hasColumnLabel(colName)) {
                if (count++ > 0) {
                    sql.append(", ");
                    temp.append(", ");
                }
                sql.append(colName);
                Object value = e.getValue();
                if (value instanceof String && isPrimaryKey(colName, pKeys) && ((String) value).endsWith(".nextval")) {
                    temp.append(value);
                } else {
                    temp.append("?");
                    paras.add(value);
                }
            }
        }
        sql.append(temp.toString()).append(")");
    }

    @Override
    public void forModelSaveOrUpdate(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {

    }

    public void forDbSave(String tableName, String[] pKeys, Record record, StringBuilder sql, List<Object> paras) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        sql.append("insert into ");
        sql.append(tableName).append("(");
        StringBuilder temp = new StringBuilder();
        temp.append(") values(");

        int count = 0;
        for (Map.Entry<String, Object> e : record.getColumns().entrySet()) {
            String colName = e.getKey();
            if (count++ > 0) {
                sql.append(", ");
                temp.append(", ");
            }
            sql.append(colName);

            Object value = e.getValue();
            if (value instanceof String && isPrimaryKey(colName, pKeys) && ((String) value).endsWith(".nextval")) {
                temp.append(value);
            } else {
                temp.append("?");
                paras.add(value);
            }
        }
        sql.append(temp.toString()).append(")");
    }

    @Override
    public void forDbSaveOrUpdate(String tableName, String[] pKeys, Record record, StringBuilder sql, List<Object> paras) {

    }

    public String forPaginate(int pageNumber, int pageSize, String sql) {
        int start = (pageNumber - 1) * pageSize;
        int end = pageNumber * pageSize;
        StringBuilder ret = new StringBuilder();
        ret.append("select * from ( select row_.*, rownum rownum_ from (  ");
        ret.append(sql);
        ret.append(" ) row_ where rownum <= ").append(end).append(") table_alias");
        ret.append(" where table_alias.rownum_ > ").append(start);
        return ret.toString();
    }

    public boolean isOracle() {
        return true;
    }

    public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
        for (int i = 0, size = paras.size(); i < size; i++) {
            Object value = paras.get(i);
            fillStatement(pst, i + 1, value);
        }
    }

    public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; i++) {
            Object value = paras[i];
            fillStatement(pst, i + 1, value);
        }
    }

    public String getDefaultPrimaryKey() {
        return "ID";
    }

    private void fillStatement(PreparedStatement pst, int index, Object value) throws SQLException {
        if (value instanceof java.sql.Date) {
            pst.setDate(index, (java.sql.Date) value);
        } else if (value instanceof java.sql.Timestamp) {
            pst.setTimestamp(index, (java.sql.Timestamp) value);
        } else {
            pst.setObject(index, value);
        }
    }
}
