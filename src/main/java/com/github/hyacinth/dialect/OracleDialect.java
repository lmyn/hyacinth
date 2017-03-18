package com.github.hyacinth.dialect;

import com.github.hyacinth.Record;
import com.github.hyacinth.Table;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
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

    public String forModelDeleteById(Table table) {
        String[] pKeys = table.getPrimaryKey();
        StringBuilder sql = new StringBuilder(45);
        sql.append("delete from ");
        sql.append(table.getName());
        sql.append(" where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(pKeys[i]).append(" = ?");
        }
        return sql.toString();
    }

    public void forModelUpdate(Table table, Map<String, Object> attrs, Set<String> modifyFlag, StringBuilder sql, List<Object> paras) {
        sql.append("update ").append(table.getName()).append(" set ");
        String[] pKeys = table.getPrimaryKey();
        for (Map.Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();
            if (modifyFlag.contains(colName) && !isPrimaryKey(colName, pKeys) && table.hasColumnLabel(colName)) {
                if (paras.size() > 0) {
                    sql.append(", ");
                }
                sql.append(colName).append(" = ? ");
                paras.add(e.getValue());
            }
        }
        sql.append(" where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(pKeys[i]).append(" = ?");
            paras.add(attrs.get(pKeys[i]));
        }
    }

    public String forModelFindById(Table table, String columns) {
        StringBuilder sql = new StringBuilder("select ").append(columns).append(" from ");
        sql.append(table.getName());
        sql.append(" where ");
        String[] pKeys = table.getPrimaryKey();
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(pKeys[i]).append(" = ?");
        }
        return sql.toString();
    }

    public String forDbFindById(String tableName, String[] pKeys) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(" where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(pKeys[i]).append(" = ?");
        }
        return sql.toString();
    }

    public String forDbDeleteById(String tableName, String[] pKeys) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        StringBuilder sql = new StringBuilder("delete from ").append(tableName).append(" where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(pKeys[i]).append(" = ?");
        }
        return sql.toString();
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

    public void forDbUpdate(String tableName, String[] pKeys, Object[] ids, Record record, StringBuilder sql, List<Object> paras) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        sql.append("update ").append(tableName).append(" set ");
        for (Map.Entry<String, Object> e : record.getColumns().entrySet()) {
            String colName = e.getKey();
            if (!isPrimaryKey(colName, pKeys)) {
                if (paras.size() > 0) {
                    sql.append(", ");
                }
                sql.append(colName).append(" = ? ");
                paras.add(e.getValue());
            }
        }
        sql.append(" where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            sql.append(pKeys[i]).append(" = ?");
            paras.add(ids[i]);
        }
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
            if (value instanceof java.sql.Date) {
                pst.setDate(i + 1, (java.sql.Date) value);
            } else if (value instanceof java.sql.Timestamp) {
                pst.setTimestamp(i + 1, (java.sql.Timestamp) value);
            } else {
                pst.setObject(i + 1, value);
            }
        }
    }

    public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; i++) {
            Object value = paras[i];
            if (value instanceof java.sql.Date) {
                pst.setDate(i + 1, (java.sql.Date) value);
            } else if (value instanceof java.sql.Timestamp) {
                pst.setTimestamp(i + 1, (java.sql.Timestamp) value);
            } else {
                pst.setObject(i + 1, value);
            }
        }
    }

    public String getDefaultPrimaryKey() {
        return "ID";
    }
}
