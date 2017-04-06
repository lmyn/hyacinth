package com.github.hyacinth;

import com.github.hyacinth.sql.SqlCache;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用的数据库操作工具类
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/11
 * Time: 15:52
 */
public class Db {

    public static DbPro use() {
        return DbPro.MAIN;
    }

    /**
     * use方法用于切换不同的数据源
     *
     * @param configName
     * @return
     */
    public static DbPro use(String configName) {
        return DbPro.use(configName);
    }

    static <T> List<T> query(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.query(config, conn, sql, paras);
    }

    /**
     * query系列方法，返回类型为泛型
     * 若查询为单个列，那么返回的泛型类型为该列对应的java数据类型，否则为Object[]
     *
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
     * @param sqlKey sqlKey
     * @see #query(SqlKey, Object...)
     */
    public static <T> List<T> query(SqlKey sqlKey) {
        return DbPro.MAIN.query(SqlCache.fixed.get(sqlKey.toString()));
    }

    /**
     * 查询第一个值，如果不能确定范围记录数，请再sql中接入"limit 1"对返回条数进行限制
     *
     * @param sqlKey sqlKey
     * @param paras  paras
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
     * @see #queryFirst(SqlKey, Object...)
     */
    public static <T> T queryFirst(SqlKey sqlKey) {
        return DbPro.MAIN.queryFirst(sqlKey.toString());
    }

    /**
     * 以下43个方法，用于获取指定Java类型的数据
     * <p>
     * String,int,long,bigInteger,date,time,timestamp,double,float,boolean,bigDecimal,bytes,number
     *
     * @param <T>   若查询为单个列，那么返回的泛型类型为该列对应的java数据类型，否则为Object[]
     * @param sql   sql语句
     * @param paras 参数列表
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

    /**
     * update系列方法用于执行insert, update, delete等操作
     *
     * @param sqlKey sqlKey
     * @param paras  参数列表
     * @return 更新记录数
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
     * @see #update(SqlKey, Object...)
     */
    public static int update(SqlKey sqlKey) {
        return DbPro.MAIN.update(SqlCache.fixed.get(sqlKey.toString()));
    }

    static int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.update(config, conn, sql, paras);
    }

    /**
     * find系列方法
     *
     * @param sqlKey sqlKey
     * @param paras  参数列表
     * @return
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
     * @see #find(SqlKey, Object...)
     */
    public static List<Record> find(SqlKey sqlKey) {
        return DbPro.MAIN.find(SqlCache.fixed.get(sqlKey.toString()));
    }

    static List<Record> find(Config config, Connection conn, String sql, Object... paras) throws SQLException, IllegalAccessException, InstantiationException {
        return DbPro.MAIN.find(config, conn, sql, paras);
    }

    /**
     * Record查询，返回第一条记录，如果返回结果集为多条记录，那么只取第一条
     *
     * @param sqlKey sqlKey
     * @param paras  参数列表
     * @return Record
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
     * @see #findFirst(SqlKey, Object...)
     */
    public static Record findFirst(SqlKey sqlKey) {
        return DbPro.MAIN.findFirst(SqlCache.fixed.get(sqlKey.toString()));
    }

    static Record findFirst(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.findFirst(config, conn, sql, paras);
    }

    /**
     * 根据Id查询Record，默认主键为id
     * <pre>
     * 示例:
     * Record user = Db.findById("user", 15);
     * </pre>
     *
     * @param tableName 表名
     * @param idValue   主键值
     */
    public static Record findById(String tableName, Object idValue) {
        return DbPro.MAIN.findById(tableName, idValue);
    }

    /**
     * 根据Id查询Record，需指定id列
     * <pre>
     * 示例:
     * Record user = Db.findById("user", "user_id", 123);
     * Record userRole = Db.findById("user_role", "user_id, role_id", 123, 456);
     * </pre>
     *
     * @param tableName  表名
     * @param primaryKey 主键列，复合主键用","号分割
     * @param idValue    主键值数组
     */
    public static Record findById(String tableName, String primaryKey, Object... idValue) {
        return DbPro.MAIN.findById(tableName, primaryKey, idValue);
    }

    /**
     * 根据Id删除一条记录，默认主键为id
     * <pre>
     * 示例:
     * Db.deleteById("user", 15);
     * </pre>
     *
     * @param tableName 表名
     * @param idValue   主键
     * @return 删除成功返回true, 否则false
     */
    public static boolean deleteById(String tableName, Object idValue) {
        return DbPro.MAIN.deleteById(tableName, idValue);
    }

    /**
     * 根据Id删除一条记录，需指定主键列
     * <pre>
     * 示例:
     * Db.deleteById("user", "user_id", 15);
     * Db.deleteById("user_role", "user_id, role_id", 123, 456);
     * </pre>
     *
     * @param tableName  表名
     * @param primaryKey 主键，复合主键使用","号进行分割
     * @param idValue    id值
     * @return 删除成功返回true, 否则false
     */
    public static boolean deleteById(String tableName, String primaryKey, Object... idValue) {
        return DbPro.MAIN.deleteById(tableName, primaryKey, idValue);
    }

    /**
     * 根据Id删除一条记录，需指定主键列
     * <pre>
     * 示例:
     * boolean succeed = Db.delete("user", "id", user);
     * </pre>
     *
     * @param tableName  表名
     * @param primaryKey 主键，复合主键使用","号进行分割
     * @param record     record指定了id之后，id值将从record中进行获取
     * @return 删除成功返回true, 否则false
     */
    public static boolean deleteById(String tableName, String primaryKey, Record record) {
        return DbPro.MAIN.delete(tableName, primaryKey, record);
    }

    /**
     * 根据Id删除一条记录，采用默认主键id
     * <pre>
     * 示例:
     * boolean succeed = Db.delete("user", user);
     * </pre>
     *
     * @see #deleteById(String, String, Record)
     */
    public static boolean deleteById(String tableName, Record record) {
        return DbPro.MAIN.delete(tableName, record);
    }

    static Page<Record> paginate(Config config, Connection conn, int pageNumber, int pageSize, String sql, Object... paras) throws SQLException {
        return DbPro.MAIN.paginate(config, conn, pageNumber, pageSize, sql, paras);
    }

    /**
     * 分页系列方法
     *
     * @param pageNumber 当前页码
     * @param pageSize   页大小
     * @param sqlKey     sqlKey
     * @param paras      参数列表
     * @return 分页对象
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
     * 保存
     * <pre>
     * 示例:
     * Record userRole = new Record().set("user_id", 123).set("role_id", 456);
     * Db.save("user_role", "user_id, role_id", userRole);
     * </pre>
     *
     * @param tableName  表名
     * @param primaryKey 主键，复合主键使用","号进行分割
     * @param record     需要保存的record
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

    /**
     * 更新record，需指定id列
     * <pre>
     * 示例:
     * Db.update("user_role", "user_id, role_id", record);
     * </pre>
     *
     * @param tableName  表名
     * @param primaryKey 主键，复合主键使用","号进行分割
     * @param record     需要保存的record
     */
    public static boolean update(String tableName, String primaryKey, Record record) {
        return DbPro.MAIN.update(tableName, primaryKey, record);
    }

    /**
     * 更新record，默认主键列为id
     * <pre>
     * 示例:
     * Db.update("user", record);
     * </pre>
     *
     * @see #update(String, String, Record)
     */
    public static boolean update(String tableName, Record record) {
        return DbPro.MAIN.update(tableName, record);
    }

    static boolean update(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        return DbPro.MAIN.update(config, conn, tableName, primaryKey, record);
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
