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
     * @see #query(SqlKey, Object...)
     */
    public static <T> List<T> query(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.query(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static <T> List<T> query(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.query(sql, parasValueList.toArray());
    }

    /**
     * @param sqlKey an SQL statement
     * @see #query(SqlKey, Object...)
     */
    public static <T> List<T> query(SqlKey sqlKey) {
        return DbPro.MAIN.query(SqlCache.fixed.get(sqlKey.toString()));
    }

    /**
     * Execute sql query and return the first result. I recommend add "limit 1" in your sql.
     *
     * @param sqlKey an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras  the parameters of sql
     * @return Object[] if your sql has select more than one column,
     * and it return Object if your sql has select only one column.
     */
    public static <T> T queryFirst(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryFirst(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static <T> T queryFirst(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryFirst(sql, parasValueList.toArray());
    }

    /**
     * @param sqlKey an SQL statement
     * @see #queryFirst(SqlKey, Object...)
     */
    public static <T> T queryFirst(SqlKey sqlKey) {
        return DbPro.MAIN.queryFirst(sqlKey.toString());
    }

    // 26 queryXxx method below -----------------------------------------------

    /**
     * Execute sql query just return one column.
     *
     * @param <T>   the type of the column that in your sql's select statement
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return <T> T
     */
    static <T> T queryColumn(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.queryColumn(config, conn, sql, paras);
    }

    public static <T> T queryColumn(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryColumn(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static <T> T queryColumn(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryColumn(sql, parasValueList.toArray());
    }

    public static <T> T queryColumn(SqlKey sqlKey) {
        return DbPro.MAIN.queryColumn(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static String queryStr(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryStr(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static String queryStr(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryStr(sql, parasValueList.toArray());
    }

    public static String queryStr(SqlKey sqlKey) {
        return DbPro.MAIN.queryStr(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static Integer queryInt(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryInt(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static Integer queryInt(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryInt(sql, parasValueList.toArray());
    }

    public static Integer queryInt(SqlKey sqlKey) {
        return DbPro.MAIN.queryInt(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static Long queryLong(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryLong(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static Long queryLong(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryLong(sql, parasValueList.toArray());
    }

    public static Long queryLong(SqlKey sqlKey) {
        return DbPro.MAIN.queryLong(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static Double queryDouble(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryDouble(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static Double queryDouble(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryDouble(sql, parasValueList.toArray());
    }

    public static Double queryDouble(SqlKey sqlKey) {
        return DbPro.MAIN.queryDouble(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static Float queryFloat(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryFloat(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static Float queryFloat(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryFloat(sql, parasValueList.toArray());
    }

    public static Float queryFloat(SqlKey sqlKey) {
        return DbPro.MAIN.queryFloat(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static java.math.BigDecimal queryBigDecimal(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryBigDecimal(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static java.math.BigDecimal queryBigDecimal(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryBigDecimal(sql, parasValueList.toArray());
    }

    public static java.math.BigDecimal queryBigDecimal(SqlKey sqlKey) {
        return DbPro.MAIN.queryBigDecimal(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static byte[] queryBytes(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryBytes(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static byte[] queryBytes(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryBytes(sql, parasValueList.toArray());
    }

    public static byte[] queryBytes(SqlKey sqlKey) {
        return DbPro.MAIN.queryBytes(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static java.util.Date queryDate(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryDate(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static java.util.Date queryDate(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryDate(sql, parasValueList.toArray());
    }

    public static java.util.Date queryDate(SqlKey sqlKey) {
        return DbPro.MAIN.queryDate(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static java.sql.Time queryTime(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryTime(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static java.sql.Time queryTime(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryTime(sql, parasValueList.toArray());
    }

    public static java.sql.Time queryTime(SqlKey sqlKey) {
        return DbPro.MAIN.queryTime(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static java.sql.Timestamp queryTimestamp(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryTimestamp(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static java.sql.Timestamp queryTimestamp(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryTimestamp(sql, parasValueList.toArray());
    }

    public static java.sql.Timestamp queryTimestamp(SqlKey sqlKey) {
        return DbPro.MAIN.queryTimestamp(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static Boolean queryBoolean(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryBoolean(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static Boolean queryBoolean(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryBoolean(sql, parasValueList.toArray());
    }

    public static Boolean queryBoolean(SqlKey sqlKey) {
        return DbPro.MAIN.queryBoolean(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static Short queryShort(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryShort(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static Short queryShort(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryShort(sql, parasValueList.toArray());
    }

    public static Short queryShort(SqlKey sqlKey) {
        return DbPro.MAIN.queryShort(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static Number queryNumber(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.queryNumber(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static Number queryNumber(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.queryNumber(sql, parasValueList.toArray());
    }

    public static Number queryNumber(SqlKey sqlKey) {
        return DbPro.MAIN.queryNumber(SqlCache.fixed.get(sqlKey.toString()));
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
     *
     * @param sqlKey an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras  the parameters of sql
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>,
     * or <code>DELETE</code> statements, or 0 for SQL statements
     * that return nothing
     */
    public static int update(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.update(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static int update(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.update(sql, parasValueList.toArray());
    }

    /**
     * @param sqlKey an SQL statement
     * @see #update(SqlKey, Object...)
     */
    public static int update(SqlKey sqlKey) {
        return DbPro.MAIN.update(SqlCache.fixed.get(sqlKey.toString()));
    }

    static List<Record> find(Config config, Connection conn, String sql, Object... paras) throws SQLException, IllegalAccessException, InstantiationException {
        return DbPro.MAIN.find(config, conn, sql, paras);
    }

    /**
     * @see #find(SqlKey, Object...)
     */
    public static List<Record> find(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.find(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static List<Record> find(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.find(sql, parasValueList.toArray());
    }

    /**
     * @param sqlKey the sql statement
     * @see #find(SqlKey)
     */
    public static List<Record> find(SqlKey sqlKey) {
        return DbPro.MAIN.find(SqlCache.fixed.get(sqlKey.toString()));
    }

    public static Record findFirst(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.findFirst(config, conn, sql, paras);
    }

    /**
     * Find first record. I recommend add "limit 1" in your sql.
     *
     * @param sqlKey an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras  the parameters of sql
     * @return the Record object
     */
    public static Record findFirst(SqlKey sqlKey, Object... paras) {
        return DbPro.MAIN.findFirst(SqlCache.fixed.get(sqlKey.toString()), paras);
    }

    public static Record findFirst(SqlKey sqlKey, Map<String, Object> paras) {
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.findFirst(sql, parasValueList.toArray());
    }

    /**
     * @param sqlKey an SQL statement
     * @see #findFirst(SqlKey, Object...)
     */
    public static Record findFirst(SqlKey sqlKey) {
        return DbPro.MAIN.findFirst(SqlCache.fixed.get(sqlKey.toString()));
    }

    /**
     * Find record by id with default primary key.
     * <pre>
     * Example:
     * Record user = Db.findById("user", 15);
     * </pre>
     *
     * @param tableName the table name of the table
     * @param idValue   the id value of the record
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
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param idValue    the id value of the record, it can be composite id values
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
     *
     * @param tableName the table name of the table
     * @param idValue   the id value of the record
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
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param idValue    the id value of the record, it can be composite id values
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
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the record
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
     *
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
     *
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @param sqlKey     sql statement
     * @param paras      the parameters of sql
     * @return the Page object
     */

    public static Page<Record> paginate(int pageNumber, int pageSize, SqlKey sqlKey, Page<Record> page, Object... paras) {
        if (page == null) {
            page = new ProvidePage<Record>();
        }
        return DbPro.MAIN.paginate(pageNumber, pageSize, SqlCache.fixed.get(sqlKey.toString()), page, paras);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, SqlKey sqlKey, Page<Record> page, Map<String, Object> paras) {
        if (page == null) {
            page = new ProvidePage<Record>();
        }
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(sqlKey.toString(), paras, parasValueList);
        return DbPro.MAIN.paginate(pageNumber, pageSize, sql, page, parasValueList);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, SqlKey sqlKey, Object... paras) {
        return paginate(pageNumber, pageSize, sqlKey, new ProvidePage<Record>(), paras);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, SqlKey sqlKey, Map<String, Object> paras) {
        return paginate(pageNumber, pageSize, sqlKey, new ProvidePage<Record>(), paras);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, SqlKey sqlKey) {
        return DbPro.MAIN.paginate(pageNumber, pageSize, SqlCache.fixed.get(sqlKey.toString()));
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
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the record will be saved
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
     *
     * @param tableName  the table name of the Record save to
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the Record object
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
     *
     * @see #update(String, String, Record)
     */
    public static boolean update(String tableName, Record record) {
        return DbPro.MAIN.update(tableName, record);
    }

    /**
     * @see DbPro#batch(String, Object[][], int)
     */
    public static int[] batch(SqlKey sqlKey, Object[][] paras, int batchSize) {
        return DbPro.MAIN.batch(SqlCache.fixed.get(sqlKey.toString()), paras, batchSize);
    }

    /**
     * @see DbPro#batch(String, String, List, int)
     */
    public static int[] batch(SqlKey sqlKey, String columns, List modelOrRecordList, int batchSize) {
        return DbPro.MAIN.batch(SqlCache.fixed.get(sqlKey.toString()), columns, modelOrRecordList, batchSize);
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
