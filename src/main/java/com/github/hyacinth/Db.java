package com.github.hyacinth;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/11
 * Time: 15:52
 */
public class Db {
    public static DbPro use() {
        return DbPro.MAIN;
    }

    public static DbPro use(String configName) {
        return DbPro.use(configName);
    }

    static <T> List<T> query(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.query(config, conn, sql, paras);
    }

    /**
     * @see #query(String, Object...)
     */
    public static <T> List<T> query(String sql, Object... paras) {
        return DbPro.MAIN.query(sql, paras);
    }

    /**
     * @see #query(String, Object...)
     * @param sql an SQL statement
     */
    public static <T> List<T> query(String sql) {
        return DbPro.MAIN.query(sql);
    }

    /**
     * Execute sql query and return the first result. I recommend add "limit 1" in your sql.
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return Object[] if your sql has select more than one column,
     * 			and it return Object if your sql has select only one column.
     */
    public static <T> T queryFirst(String sql, Object... paras) {
        return DbPro.MAIN.queryFirst(sql, paras);
    }

    /**
     * @see #queryFirst(String, Object...)
     * @param sql an SQL statement
     */
    public static <T> T queryFirst(String sql) {
        return DbPro.MAIN.queryFirst(sql);
    }

    // 26 queryXxx method below -----------------------------------------------
    /**
     * Execute sql query just return one column.
     * @param <T> the type of the column that in your sql's select statement
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return <T> T
     */
    public static <T> T queryColumn(String sql, Object... paras) {
        return DbPro.MAIN.queryColumn(sql, paras);
    }

    public static <T> T queryColumn(String sql) {
        return DbPro.MAIN.queryColumn(sql);
    }

    public static String queryStr(String sql, Object... paras) {
        return DbPro.MAIN.queryStr(sql, paras);
    }

    public static String queryStr(String sql) {
        return DbPro.MAIN.queryStr(sql);
    }

    public static Integer queryInt(String sql, Object... paras) {
        return DbPro.MAIN.queryInt(sql, paras);
    }

    public static Integer queryInt(String sql) {
        return DbPro.MAIN.queryInt(sql);
    }

    public static Long queryLong(String sql, Object... paras) {
        return DbPro.MAIN.queryLong(sql, paras);
    }

    public static Long queryLong(String sql) {
        return DbPro.MAIN.queryLong(sql);
    }

    public static Double queryDouble(String sql, Object... paras) {
        return DbPro.MAIN.queryDouble(sql, paras);
    }

    public static Double queryDouble(String sql) {
        return DbPro.MAIN.queryDouble(sql);
    }

    public static Float queryFloat(String sql, Object... paras) {
        return DbPro.MAIN.queryFloat(sql, paras);
    }

    public static Float queryFloat(String sql) {
        return DbPro.MAIN.queryFloat(sql);
    }

    public static java.math.BigDecimal queryBigDecimal(String sql, Object... paras) {
        return DbPro.MAIN.queryBigDecimal(sql, paras);
    }

    public static java.math.BigDecimal queryBigDecimal(String sql) {
        return DbPro.MAIN.queryBigDecimal(sql);
    }

    public static byte[] queryBytes(String sql, Object... paras) {
        return DbPro.MAIN.queryBytes(sql, paras);
    }

    public static byte[] queryBytes(String sql) {
        return DbPro.MAIN.queryBytes(sql);
    }

    public static java.util.Date queryDate(String sql, Object... paras) {
        return DbPro.MAIN.queryDate(sql, paras);
    }

    public static java.util.Date queryDate(String sql) {
        return DbPro.MAIN.queryDate(sql);
    }

    public static java.sql.Time queryTime(String sql, Object... paras) {
        return DbPro.MAIN.queryTime(sql, paras);
    }

    public static java.sql.Time queryTime(String sql) {
        return DbPro.MAIN.queryTime(sql);
    }

    public static java.sql.Timestamp queryTimestamp(String sql, Object... paras) {
        return DbPro.MAIN.queryTimestamp(sql, paras);
    }

    public static java.sql.Timestamp queryTimestamp(String sql) {
        return DbPro.MAIN.queryTimestamp(sql);
    }

    public static Boolean queryBoolean(String sql, Object... paras) {
        return DbPro.MAIN.queryBoolean(sql, paras);
    }

    public static Boolean queryBoolean(String sql) {
        return DbPro.MAIN.queryBoolean(sql);
    }

    public static Short queryShort(String sql, Object... paras) {
        return DbPro.MAIN.queryShort(sql, paras);
    }

    public static Short queryShort(String sql) {
        return DbPro.MAIN.queryShort(sql);
    }

    public static Number queryNumber(String sql, Object... paras) {
        return DbPro.MAIN.queryNumber(sql, paras);
    }

    public static Number queryNumber(String sql) {
        return DbPro.MAIN.queryNumber(sql);
    }
    // 26 queryXxx method under -----------------------------------------------

    /**
     * Execute sql update
     */
    static int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.update(config, conn, sql, paras);
    }

    /**
     * Execute update, insert or delete sql statement.
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>,
     *         or <code>DELETE</code> statements, or 0 for SQL statements
     *         that return nothing
     */
    public static int update(String sql, Object... paras) {
        return DbPro.MAIN.update(sql, paras);
    }

    /**
     * @see #update(String, Object...)
     * @param sql an SQL statement
     */
    public static int update(String sql) {
        return DbPro.MAIN.update(sql);
    }

    static List<Record> find(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.find(config, conn, sql, paras);
    }

    /**
     * @see #find(String, Object...)
     */
    public static List<Record> find(String sql, Object... paras) {
        return DbPro.MAIN.find(sql, paras);
    }

    /**
     * @see #find(String)
     * @param sql the sql statement
     */
    public static List<Record> find(String sql) {
        return DbPro.MAIN.find(sql);
    }

    /**
     * Find first record. I recommend add "limit 1" in your sql.
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return the Record object
     */
    public static Record findFirst(String sql, Object... paras) {
        return DbPro.MAIN.findFirst(sql, paras);
    }

    /**
     * @see #findFirst(String, Object...)
     * @param sql an SQL statement
     */
    public static Record findFirst(String sql) {
        return DbPro.MAIN.findFirst(sql);
    }

    /**
     * Find record by id with default primary key.
     * <pre>
     * Example:
     * Record user = Db.findById("user", 15);
     * </pre>
     * @param tableName the table name of the table
     * @param idValue the id value of the record
     */
    public static Record findById(String tableName, Object idValue) {
        return DbPro.MAIN.findById(tableName, idValue);
    }

    /**
     * Find record by id.
     * <pre>
     * Example:
     * Record user = Db.findById("user", "user_id", 123);
     * Record userRole = Db.findById("user_role", "user_id, role_id", 123, 456);
     * </pre>
     * @param tableName the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param idValue the id value of the record, it can be composite id values
     */
    public static Record findById(String tableName, String primaryKey, Object... idValue) {
        return DbPro.MAIN.findById(tableName, primaryKey, idValue);
    }

    /**
     * Delete record by id with default primary key.
     * <pre>
     * Example:
     * Db.deleteById("user", 15);
     * </pre>
     * @param tableName the table name of the table
     * @param idValue the id value of the record
     * @return true if delete succeed otherwise false
     */
    public static boolean deleteById(String tableName, Object idValue) {
        return DbPro.MAIN.deleteById(tableName, idValue);
    }

    /**
     * Delete record by id.
     * <pre>
     * Example:
     * Db.deleteById("user", "user_id", 15);
     * Db.deleteById("user_role", "user_id, role_id", 123, 456);
     * </pre>
     * @param tableName the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param idValue the id value of the record, it can be composite id values
     * @return true if delete succeed otherwise false
     */
    public static boolean deleteById(String tableName, String primaryKey, Object... idValue) {
        return DbPro.MAIN.deleteById(tableName, primaryKey, idValue);
    }

    /**
     * Delete record.
     * <pre>
     * Example:
     * boolean succeed = Db.delete("user", "id", user);
     * </pre>
     * @param tableName the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record the record
     * @return true if delete succeed otherwise false
     */
    public static boolean delete(String tableName, String primaryKey, Record record) {
        return DbPro.MAIN.delete(tableName, primaryKey, record);
    }

    /**
     * <pre>
     * Example:
     * boolean succeed = Db.delete("user", user);
     * </pre>
     * @see #delete(String, String, Record)
     */
    public static boolean delete(String tableName, Record record) {
        return DbPro.MAIN.delete(tableName, record);
    }

    static Page<Record> paginate(Config config, Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) throws SQLException {
        return DbPro.MAIN.paginate(config, conn, pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    /**
     * Paginate.
     * @param pageNumber the page number
     * @param pageSize the page size
     * @param select the select part of the sql statement
     * @param sqlExceptSelect the sql statement excluded select part
     * @param paras the parameters of sql
     * @return the Page object
     */
    public static Page<Record> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return DbPro.MAIN.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        return DbPro.MAIN.paginate(pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    /**
     * @see #paginate(int, int, String, String)
     */
    public static Page<Record> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return DbPro.MAIN.paginate(pageNumber, pageSize, select, sqlExceptSelect);
    }

    static boolean save(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        return DbPro.MAIN.save(config, conn, tableName, primaryKey, record);
    }

    /**
     * Save record.
     * <pre>
     * Example:
     * Record userRole = new Record().set("user_id", 123).set("role_id", 456);
     * Db.save("user_role", "user_id, role_id", userRole);
     * </pre>
     * @param tableName the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record the record will be saved
     */
    public static boolean save(String tableName, String primaryKey, Record record) {
        return DbPro.MAIN.save(tableName, primaryKey, record);
    }

    /**
     * @see #save(String, String, Record)
     */
    public static boolean save(String tableName, Record record) {
        return DbPro.MAIN.save(tableName, record);
    }

    static boolean update(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        return DbPro.MAIN.update(config, conn, tableName, primaryKey, record);
    }

    /**
     * Update Record.
     * <pre>
     * Example:
     * Db.update("user_role", "user_id, role_id", record);
     * </pre>
     * @param tableName the table name of the Record save to
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record the Record object
     */
    public static boolean update(String tableName, String primaryKey, Record record) {
        return DbPro.MAIN.update(tableName, primaryKey, record);
    }

    /**
     * Update record with default primary key.
     * <pre>
     * Example:
     * Db.update("user", record);
     * </pre>
     * @see #update(String, String, Record)
     */
    public static boolean update(String tableName, Record record) {
        return DbPro.MAIN.update(tableName, record);
    }

    /**
     * @see DbPro#batch(String, Object[][], int)
     */
    public static int[] batch(String sql, Object[][] paras, int batchSize) {
        return DbPro.MAIN.batch(sql, paras, batchSize);
    }

    /**
     * @see DbPro#batch(String, String, List, int)
     */
    public static int[] batch(String sql, String columns, List modelOrRecordList, int batchSize) {
        return DbPro.MAIN.batch(sql, columns, modelOrRecordList, batchSize);
    }

    /**
     * @see DbPro#batch(List, int)
     */
    public static int[] batch(List<String> sqlList, int batchSize) {
        return DbPro.MAIN.batch(sqlList, batchSize);
    }

    /**
     * @see DbPro#batchSave(List, int)
     */
    public static int[] batchSave(List<? extends Model> modelList, int batchSize) {
        return DbPro.MAIN.batchSave(modelList, batchSize);
    }

    /**
     * @see DbPro#batchSave(String, List, int)
     */
    public static int[] batchSave(String tableName, List<Record> recordList, int batchSize) {
        return DbPro.MAIN.batchSave(tableName, recordList, batchSize);
    }

    /**
     * @see DbPro#batchUpdate(List, int)
     */
    public static int[] batchUpdate(List<? extends Model> modelList, int batchSize) {
        return DbPro.MAIN.batchUpdate(modelList, batchSize);
    }

    /**
     * @see DbPro#batchUpdate(String, String, List, int)
     */
    public static int[] batchUpdate(String tableName, String primaryKey, List<Record> recordList, int batchSize) {
        return DbPro.MAIN.batchUpdate(tableName, primaryKey, recordList, batchSize);
    }

    /**
     * @see DbPro#batchUpdate(String, List, int)
     */
    public static int[] batchUpdate(String tableName, List<Record> recordList, int batchSize) {
        return DbPro.MAIN.batchUpdate(tableName, recordList, batchSize);
    }

}
