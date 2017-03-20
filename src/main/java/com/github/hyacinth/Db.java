package com.github.hyacinth;

import com.github.hyacinth.sql.SqlCache;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public static <T> List<T> query(String key, Object... paras) {
        return DbPro.MAIN.query(SqlCache.fixed.get(key), paras);
    }

    public static <T> List<T> query(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.query(sql, parasValueList.toArray());
    }

    /**
     * @see #query(String, Object...)
     * @param key an SQL statement
     */
    public static <T> List<T> query(String key) {
        return DbPro.MAIN.query(SqlCache.fixed.get(key));
    }

    /**
     * Execute sql query and return the first result. I recommend add "limit 1" in your sql.
     * @param key an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return Object[] if your sql has select more than one column,
     * 			and it return Object if your sql has select only one column.
     */
    public static <T> T queryFirst(String key, Object... paras) {
        return DbPro.MAIN.queryFirst(SqlCache.fixed.get(key), paras);
    }

    public static <T> T queryFirst(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryFirst(sql, parasValueList.toArray());
    }

    /**
     * @see #queryFirst(String, Object...)
     * @param key an SQL statement
     */
    public static <T> T queryFirst(String key) {
        return DbPro.MAIN.queryFirst(key);
    }

    // 26 queryXxx method below -----------------------------------------------
    /**
     * Execute sql query just return one column.
     * @param <T> the type of the column that in your sql's select statement
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return <T> T
     */
    static <T> T queryColumn(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.queryColumn(config, conn, sql, paras);
    }

    public static <T> T queryColumn(String key, Object... paras) {
        return DbPro.MAIN.queryColumn(SqlCache.fixed.get(key), paras);
    }

    public static <T> T queryColumn(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryColumn(sql, parasValueList.toArray());
    }

    public static <T> T queryColumn(String key) {
        return DbPro.MAIN.queryColumn(SqlCache.fixed.get(key));
    }

    public static String queryStr(String key, Object... paras) {
        return DbPro.MAIN.queryStr(SqlCache.fixed.get(key), paras);
    }

    public static String queryStr(String key, Map<String, Object> paras){
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryStr(sql, parasValueList.toArray());
    }

    public static String queryStr(String key) {
        return DbPro.MAIN.queryStr(SqlCache.fixed.get(key));
    }

    public static Integer queryInt(String key, Object... paras) {
        return DbPro.MAIN.queryInt(SqlCache.fixed.get(key), paras);
    }

    public static Integer queryInt(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryInt(sql, parasValueList.toArray());
    }

    public static Integer queryInt(String key) {
        return DbPro.MAIN.queryInt(SqlCache.fixed.get(key));
    }

    public static Long queryLong(String key, Object... paras) {
        return DbPro.MAIN.queryLong(SqlCache.fixed.get(key), paras);
    }

    public static Long queryLong(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryLong(sql, parasValueList.toArray());
    }

    public static Long queryLong(String key) {
        return DbPro.MAIN.queryLong(SqlCache.fixed.get(key));
    }

    public static Double queryDouble(String key, Object... paras) {
        return DbPro.MAIN.queryDouble(SqlCache.fixed.get(key), paras);
    }

    public static Double queryDouble(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryDouble(sql, parasValueList.toArray());
    }

    public static Double queryDouble(String key) {
        return DbPro.MAIN.queryDouble(SqlCache.fixed.get(key));
    }

    public static Float queryFloat(String key, Object... paras) {
        return DbPro.MAIN.queryFloat(SqlCache.fixed.get(key), paras);
    }

    public static Float queryFloat(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryFloat(sql, parasValueList.toArray());
    }

    public static Float queryFloat(String key) {
        return DbPro.MAIN.queryFloat(SqlCache.fixed.get(key));
    }

    public static java.math.BigDecimal queryBigDecimal(String key, Object... paras) {
        return DbPro.MAIN.queryBigDecimal(SqlCache.fixed.get(key), paras);
    }

    public static java.math.BigDecimal queryBigDecimal(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryBigDecimal(sql, parasValueList.toArray());
    }

    public static java.math.BigDecimal queryBigDecimal(String key) {
        return DbPro.MAIN.queryBigDecimal(SqlCache.fixed.get(key));
    }

    public static byte[] queryBytes(String key, Object... paras) {
        return DbPro.MAIN.queryBytes(SqlCache.fixed.get(key), paras);
    }

    public static byte[] queryBytes(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryBytes(sql, parasValueList.toArray());
    }

    public static byte[] queryBytes(String key) {
        return DbPro.MAIN.queryBytes(SqlCache.fixed.get(key));
    }

    public static java.util.Date queryDate(String key, Object... paras) {
        return DbPro.MAIN.queryDate(SqlCache.fixed.get(key), paras);
    }

    public static java.util.Date queryDate(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryDate(sql, parasValueList.toArray());
    }

    public static java.util.Date queryDate(String key) {
        return DbPro.MAIN.queryDate(SqlCache.fixed.get(key));
    }

    public static java.sql.Time queryTime(String key, Object... paras) {
        return DbPro.MAIN.queryTime(SqlCache.fixed.get(key), paras);
    }

    public static java.sql.Time queryTime(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryTime(sql, parasValueList.toArray());
    }

    public static java.sql.Time queryTime(String key) {
        return DbPro.MAIN.queryTime(SqlCache.fixed.get(key));
    }

    public static java.sql.Timestamp queryTimestamp(String key, Object... paras) {
        return DbPro.MAIN.queryTimestamp(SqlCache.fixed.get(key), paras);
    }

    public static java.sql.Timestamp queryTimestamp(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryTimestamp(sql, parasValueList.toArray());
    }

    public static java.sql.Timestamp queryTimestamp(String key) {
        return DbPro.MAIN.queryTimestamp(SqlCache.fixed.get(key));
    }

    public static Boolean queryBoolean(String key, Object... paras) {
        return DbPro.MAIN.queryBoolean(SqlCache.fixed.get(key), paras);
    }

    public static Boolean queryBoolean(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryBoolean(sql, parasValueList.toArray());
    }

    public static Boolean queryBoolean(String key) {
        return DbPro.MAIN.queryBoolean(SqlCache.fixed.get(key));
    }

    public static Short queryShort(String key, Object... paras) {
        return DbPro.MAIN.queryShort(SqlCache.fixed.get(key), paras);
    }

    public static Short queryShort(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryShort(sql, parasValueList.toArray());
    }

    public static Short queryShort(String key) {
        return DbPro.MAIN.queryShort(SqlCache.fixed.get(key));
    }

    public static Number queryNumber(String key, Object... paras) {
        return DbPro.MAIN.queryNumber(SqlCache.fixed.get(key), paras);
    }

    public static Number queryNumber(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.queryNumber(sql, parasValueList.toArray());
    }

    public static Number queryNumber(String key) {
        return DbPro.MAIN.queryNumber(SqlCache.fixed.get(key));
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
     * @param key an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>,
     *         or <code>DELETE</code> statements, or 0 for SQL statements
     *         that return nothing
     */
    public static int update(String key, Object... paras) {
        return DbPro.MAIN.update(SqlCache.fixed.get(key), paras);
    }

    public static int update(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.update(sql, parasValueList.toArray());
    }

    /**
     * @see #update(String, Object...)
     * @param key an SQL statement
     */
    public static int update(String key) {
        return DbPro.MAIN.update(SqlCache.fixed.get(key));
    }

    static List<Record> find(Config config, Connection conn, String sql, Object... paras) throws SQLException, IllegalAccessException, InstantiationException {
        return DbPro.MAIN.find(config, conn, sql, paras);
    }

    /**
     * @see #find(String, Object...)
     */
    public static List<Record> find(String key, Object... paras) {
        return DbPro.MAIN.find(SqlCache.fixed.get(key), paras);
    }

    public static List<Record> find(String key, Map<String, Object> paras){
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.find(sql, parasValueList.toArray());
    }

    /**
     * @see #find(String)
     * @param key the sql statement
     */
    public static List<Record> find(String key) {
        return DbPro.MAIN.find(SqlCache.fixed.get(key));
    }

    public static Record findFirst(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.findFirst(config, conn, sql, paras);
    }

    /**
     * Find first record. I recommend add "limit 1" in your sql.
     * @param key an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return the Record object
     */
    public static Record findFirst(String key, Object... paras) {
        return DbPro.MAIN.findFirst(SqlCache.fixed.get(key), paras);
    }

    public static Record findFirst(String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.findFirst(sql, parasValueList.toArray());
    }

    /**
     * @see #findFirst(String, Object...)
     * @param key an SQL statement
     */
    public static Record findFirst(String key) {
        return DbPro.MAIN.findFirst(SqlCache.fixed.get(key));
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

    static Page<Record> paginate(Config config, Connection conn, int pageNumber, int pageSize, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.paginate(config, conn, pageNumber, pageSize, sql, paras);
    }

    /**
     * Paginate.
     * @param pageNumber the page number
     * @param pageSize the page size
     * @param key sql statement
     * @param paras the parameters of sql
     * @return the Page object
     */
    public static Page<Record> paginate(int pageNumber, int pageSize, String key, Object... paras) {
        return DbPro.MAIN.paginate(pageNumber, pageSize, SqlCache.fixed.get(key), paras);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, String key, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        return DbPro.MAIN.paginate(pageNumber, pageSize, sql, parasValueList);
    }

    /**
     * @see #paginate(int, int, String, Object...)
     */
    public static Page<Record> paginate(int pageNumber, int pageSize, String key) {
        return DbPro.MAIN.paginate(pageNumber, pageSize, SqlCache.fixed.get(key));
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
    public static int[] batch(String key, Object[][] paras, int batchSize) {
        return DbPro.MAIN.batch(SqlCache.fixed.get(key), paras, batchSize);
    }

    /**
     * @see DbPro#batch(String, String, List, int)
     */
    public static int[] batch(String key, String columns, List modelOrRecordList, int batchSize) {
        return DbPro.MAIN.batch(SqlCache.fixed.get(key), columns, modelOrRecordList, batchSize);
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
