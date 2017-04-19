package com.github.hyacinth.dialect;

import com.github.hyacinth.Table;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 方言抽象类
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/17
 * Time: 18:07
 */
public abstract class Dialect {

    /**
     * 用于生成Model时，从数据库查询对应表的表结构信息
     *
     * @param tableName 表名
     * @return 查询Sql
     */
    public abstract String forTableBuilderDoBuild(String tableName);

    /**
     * 生成分页查询Sql
     *
     * @param pageNumber 当前查询页码
     * @param pageSize   页大小
     * @param sql        未进行分页处理的查询sql
     * @return 生成好的分页查询Sql
     */
    public abstract String forPaginate(int pageNumber, int pageSize, String sql);

    /**
     * 生成根据主键查询Model的Sql
     *
     * @param table   model对应的表
     * @param columns 需要查询的列
     * @return 生成好的查询Sql
     */
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

    public String forModelFindAll(Table table, String columns) {
        StringBuilder sql = new StringBuilder("select ").append(columns).append(" from ").append(table.getName());
        return sql.toString();
    }

    /**
     * 生成根据Id删除Model的Sql
     *
     * @param table model对应的表
     * @return 生成好的查询Sql
     */
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

    /**
     * 生成Model保存的Sql
     *
     * @param table model对应的表
     * @param attrs 需要保存的属性（列）
     * @param sql   生成好的插入Sql
     * @param paras Sql参数列表
     */
    public void forModelSave(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
        sql.append("insert into ").append(table.getName()).append("(");
        StringBuilder temp = new StringBuilder(") values(");
        for (Map.Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();
            if (table.hasColumnLabel(colName)) {
                if (paras.size() > 0) {
                    sql.append(", ");
                    temp.append(", ");
                }
                sql.append(colName);
                temp.append("?");
                paras.add(e.getValue());
            }
        }
        sql.append(temp.toString()).append(")");
    }

    /**
     * 生成SaveOrUpdate的sql, 前提是数据库支持对应的语法
     *
     * @param table model对应的表
     * @param attrs 需要保存的属性（列）
     * @param sql   生成好的插入Sql
     * @param paras Sql参数列表
     */
    public abstract void forModelSaveOrUpdate(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras);

    /**
     * 生成Model修改的Sql
     *
     * @param table      model对应的表
     * @param attrs      需要修改的属性（列）
     * @param modifyFlag 修改过的属性
     * @param sql        生成好的插入Sql
     * @param paras      Sql参数列表
     */
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

    /**
     * forDb系列方法 同以上forModel系列方法
     */
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

    public String forDbFindAll(String tableName) {
        StringBuilder sql = new StringBuilder("select *  from ").append(tableName);
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

    public void forDbSave(String tableName, String[] pKeys, Map<String, Object> record, StringBuilder sql, List<Object> paras) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        sql.append("insert into ");
        sql.append(tableName).append("(");
        StringBuilder temp = new StringBuilder();
        temp.append(") values(");

        for (Map.Entry<String, Object> e : record.entrySet()) {
            if (paras.size() > 0) {
                sql.append(", ");
                temp.append(", ");
            }
            sql.append(e.getKey());
            temp.append("?");
            paras.add(e.getValue());
        }
        sql.append(temp.toString()).append(")");
    }

    public abstract void forDbSaveOrUpdate(String tableName, String[] pKeys, Map<String, Object> record, StringBuilder sql, List<Object> paras);

    public void forDbUpdate(String tableName, String[] pKeys, Object[] ids, Map<String, Object> record, StringBuilder sql, List<Object> paras) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        sql.append("update ").append(tableName).append(" set ");
        for (Map.Entry<String, Object> e : record.entrySet()) {
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

    public boolean isOracle() {
        return false;
    }

    /**
     * 添加Sql参数
     *
     * @param pst   PreparedStatement
     * @param paras 参数列表
     * @throws SQLException
     */
    public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
        for (int i = 0, size = paras.size(); i < size; i++) {
            pst.setObject(i + 1, paras.get(i));
        }
    }

    /**
     * @see #fillStatement(PreparedStatement, List)
     */
    public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; i++) {
            pst.setObject(i + 1, paras[i]);
        }
    }

    public String getDefaultPrimaryKey() {
        return "id";
    }

    public boolean isPrimaryKey(String colName, String[] pKeys) {
        for (String pKey : pKeys) {
            if (colName.equalsIgnoreCase(pKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 一、forDbXxx 系列方法中若有如下两种情况之一，则需要调用此方法对 pKeys 数组进行 trim():
     * 1：方法中调用了 isPrimaryKey(...)：为了防止在主键相同情况下，由于前后空串造成 isPrimaryKey 返回 false
     * 2：为了防止 tableName、colName 与数据库保留字冲突的，添加了包裹字符的：为了防止串包裹区内存在空串
     * 如 mysql 使用的 "`" 字符以及 PostgreSql 使用的 "\"" 字符
     * 不满足以上两个条件之一的 forDbXxx 系列方法也可以使用 trimPrimaryKeys(...) 方法让 sql 更加美观，但不是必须
     * <p>
     * 二、forModelXxx 由于在映射时已经trim()，故不再需要调用此方法
     */
    public void trimPrimaryKeys(String[] pKeys) {
        for (int i = 0; i < pKeys.length; i++) {
            pKeys[i] = pKeys[i].trim();
        }
    }

}
