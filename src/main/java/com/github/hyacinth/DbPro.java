package com.github.hyacinth;

import com.github.hyacinth.sql.BuildKit;
import com.github.hyacinth.sql.SqlCache;
import com.github.hyacinth.tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 10:45
 */
public class DbPro {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbPro.class);

    private final Config config;

    static DbPro MAIN = null;
    private static final Map<String, DbPro> map = new HashMap<String, DbPro>();

    /**
     * for DbKit.addConfig(configName)
     */
    static void init(String configName) {
        MAIN = new DbPro(configName);
        map.put(configName, MAIN);
    }

    /**
     * for DbKit.removeConfig(configName)
     */
    static void removeDbProWithConfig(String configName) {
        if (MAIN != null && MAIN.config.getName().equals(configName)) {
            MAIN = null;
        }
        map.remove(configName);
    }

    public DbPro() {
        if (DbKit.config == null) {
            throw new RuntimeException("The main config is null, initialize ActiveRecordPlugin first");
        }
        this.config = DbKit.config;
    }

    public DbPro(String configName) {
        this.config = DbKit.getConfig(configName);
        if (this.config == null) {
            throw new IllegalArgumentException("Config not found by configName: " + configName);
        }
    }

    public static DbPro use(String configName) {
        DbPro result = map.get(configName);
        if (result == null) {
            result = new DbPro(configName);
            map.put(configName, result);
        }
        return result;
    }

    public static DbPro use() {
        return MAIN;
    }

    <T> List<T> query(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        List result = new ArrayList();
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        int colAmount = rs.getMetaData().getColumnCount();
        if (colAmount > 1) {
            while (rs.next()) {
                Object[] temp = new Object[colAmount];
                for (int i = 0; i < colAmount; i++) {
                    temp[i] = rs.getObject(i + 1);
                }
                result.add(temp);
            }
        } else if (colAmount == 1) {
            while (rs.next()) {
                result.add(rs.getObject(1));
            }
        }
        DbKit.close(rs, pst);
        return result;
    }

    /**
     * @see #query(Config, Connection, String, Object...)
     */
    public <T> List<T> query(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return query(config, conn, sql, paras);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * Execute sql query and return the first result. I recommend add "limit 1" in your sql.
     *
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return Object[] if your sql has select more than one column,
     * and it return Object if your sql has select only one column.
     */
    public <T> T queryFirst(String sql, Object... paras) {
        List<T> result = query(sql, paras);
        return (result.size() > 0 ? result.get(0) : null);
    }

    /**
     * @param sql an SQL statement
     * @see #queryFirst(String, Object...)
     */
    public <T> T queryFirst(String sql) {
        // return queryFirst(sql, DbKit.NULL_PARA_ARRAY);
        List<T> result = query(sql, DbKit.NULL_PARA_ARRAY);
        return (result.size() > 0 ? result.get(0) : null);
    }

    // 26 queryXxx method below -----------------------------------------------

    <T> T queryColumn(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        Object obj = null;
        if (rs.next()) {
            obj = rs.getObject(1);
        }
        DbKit.close(rs, pst);
        return (T) obj;
    }

    /**
     * Execute sql query just return one column.
     *
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return <T> T
     */
    public <T> T queryColumn(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return queryColumn(config, conn, sql, paras);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    public <T> T queryColumn(String sql) {
        return queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public String queryStr(String sql, Object... paras) {
        return (String) queryColumn(sql, paras);
    }

    public String queryStr(String sql) {
        return (String) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Integer queryInt(String sql, Object... paras) {
        return (Integer) queryColumn(sql, paras);
    }

    public Integer queryInt(String sql) {
        return (Integer) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Long queryLong(String sql, Object... paras) {
        return (Long) queryColumn(sql, paras);
    }

    public Long queryLong(String sql) {
        return (Long) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Double queryDouble(String sql, Object... paras) {
        return (Double) queryColumn(sql, paras);
    }

    public Double queryDouble(String sql) {
        return (Double) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Float queryFloat(String sql, Object... paras) {
        return (Float) queryColumn(sql, paras);
    }

    public Float queryFloat(String sql) {
        return (Float) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public java.math.BigDecimal queryBigDecimal(String sql, Object... paras) {
        return (java.math.BigDecimal) queryColumn(sql, paras);
    }

    public java.math.BigDecimal queryBigDecimal(String sql) {
        return (java.math.BigDecimal) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public byte[] queryBytes(String sql, Object... paras) {
        return (byte[]) queryColumn(sql, paras);
    }

    public byte[] queryBytes(String sql) {
        return (byte[]) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public java.util.Date queryDate(String sql, Object... paras) {
        return (java.util.Date) queryColumn(sql, paras);
    }

    public java.util.Date queryDate(String sql) {
        return (java.util.Date) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Time queryTime(String sql, Object... paras) {
        return (Time) queryColumn(sql, paras);
    }

    public Time queryTime(String sql) {
        return (Time) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Timestamp queryTimestamp(String sql, Object... paras) {
        return (Timestamp) queryColumn(sql, paras);
    }

    public Timestamp queryTimestamp(String sql) {
        return (Timestamp) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Boolean queryBoolean(String sql, Object... paras) {
        return (Boolean) queryColumn(sql, paras);
    }

    public Boolean queryBoolean(String sql) {
        return (Boolean) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Short queryShort(String sql, Object... paras) {
        return (Short) queryColumn(sql, paras);
    }

    public Short queryShort(String sql) {
        return (Short) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }

    public Number queryNumber(String sql, Object... paras) {
        return (Number) queryColumn(sql, paras);
    }

    public Number queryNumber(String sql) {
        return (Number) queryColumn(sql, DbKit.NULL_PARA_ARRAY);
    }
    // 26 queryXxx method under -----------------------------------------------

    /**
     * Execute sql update
     */
    int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        int result = pst.executeUpdate();
        DbKit.close(pst);
        return result;
    }

    /**
     * Execute update, insert or delete sql statement.
     *
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>,
     * or <code>DELETE</code> statements, or 0 for SQL statements
     * that return nothing
     */
    public int update(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return update(config, conn, sql, paras);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @param sql an SQL statement
     * @see #update(String, Object...)
     */
    public int update(String sql) {
        return update(sql, DbKit.NULL_PARA_ARRAY);
    }

    List<Record> find(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        List<Record> result = RecordBuilder.buildList(config, rs);
        DbKit.close(rs, pst);
        return result;
    }

    /**
     * @see #find(Config, Connection, String, Object...)
     */
    public List<Record> find(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return find(config, conn, sql, paras);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @param sql the sql statement
     * @see #find(String, Object...)
     */
    public List<Record> find(String sql) {
        return find(sql, DbKit.NULL_PARA_ARRAY);
    }

    Record findFirst(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        Record record = RecordBuilder.build(config, rs);
        DbKit.close(rs, pst);
        return record;
    }

    /**
     * Find first record. I recommend add "limit 1" in your sql.
     *
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return the Record object
     */
    public Record findFirst(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return findFirst(config, conn, sql, paras);
        } catch (SQLException e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @param sql an SQL statement
     * @see #findFirst(String, Object...)
     */
    public Record findFirst(String sql) {
        return findFirst(sql, DbKit.NULL_PARA_ARRAY);
    }

    /**
     * Find record by id with default primary key.
     * <pre>
     * Example:
     * Record user = DbPro.use().findById("user", 15);
     * </pre>
     *
     * @param tableName the table name of the table
     * @param idValue   the id value of the record
     */
    public Record findById(String tableName, Object idValue) {
        return findById(tableName, config.dialect.getDefaultPrimaryKey(), idValue);
    }

    /**
     * Find record by id.
     * <pre>
     * Example:
     * Record user = DbPro.use().findById("user", "user_id", 123);
     * Record userRole = DbPro.use().findById("user_role", "user_id, role_id", 123, 456);
     * </pre>
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param idValue    the id value of the record, it can be composite id values
     */
    public Record findById(String tableName, String primaryKey, Object... idValue) {
        String[] pKeys = primaryKey.split(",");
        if (pKeys.length != idValue.length)
            throw new IllegalArgumentException("primary key number must equals id value number");

        String sql = config.dialect.forDbFindById(tableName, pKeys);
        List<Record> result = find(sql, idValue);
        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * Delete record by id with default primary key.
     * <pre>
     * Example:
     * DbPro.use().deleteById("user", 15);
     * </pre>
     *
     * @param tableName the table name of the table
     * @param idValue   the id value of the record
     * @return true if delete succeed otherwise false
     */
    public boolean deleteById(String tableName, Object idValue) {
        return deleteById(tableName, config.dialect.getDefaultPrimaryKey(), idValue);
    }

    /**
     * Delete record by id.
     * <pre>
     * Example:
     * DbPro.use().deleteById("user", "user_id", 15);
     * DbPro.use().deleteById("user_role", "user_id, role_id", 123, 456);
     * </pre>
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param idValue    the id value of the record, it can be composite id values
     * @return true if delete succeed otherwise false
     */
    public boolean deleteById(String tableName, String primaryKey, Object... idValue) {
        String[] pKeys = primaryKey.split(",");
        if (pKeys.length != idValue.length)
            throw new IllegalArgumentException("primary key number must equals id value number");

        String sql = config.dialect.forDbDeleteById(tableName, pKeys);
        return update(sql, idValue) >= 1;
    }

    /**
     * Delete record.
     * <pre>
     * Example:
     * boolean succeed = DbPro.use().delete("user", "id", user);
     * </pre>
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the record
     * @return true if delete succeed otherwise false
     */
    public boolean delete(String tableName, String primaryKey, Record record) {
        String[] pKeys = primaryKey.split(",");
        if (pKeys.length <= 1)
            return deleteById(tableName, primaryKey, record.get(primaryKey));

        config.dialect.trimPrimaryKeys(pKeys);
        Object[] idValue = new Object[pKeys.length];
        for (int i = 0; i < pKeys.length; i++) {
            idValue[i] = record.get(pKeys[i]);
            if (idValue[i] == null)
                throw new IllegalArgumentException("The value of primary key \"" + pKeys[i] + "\" can not be null in record object");
        }
        return deleteById(tableName, primaryKey, idValue);
    }

    /**
     * <pre>
     * Example:
     * boolean succeed = DbPro.use().delete("user", user);
     * </pre>
     *
     * @see #delete(String, String, Record)
     */
    public boolean delete(String tableName, Record record) {
        String defaultPrimaryKey = config.dialect.getDefaultPrimaryKey();
        return deleteById(tableName, defaultPrimaryKey, record.get(defaultPrimaryKey));
    }

    /**
     * 分页
     *
     * @param pageNumber 当前页
     * @param pageSize   页大小（每页记录数）
     * @param sql        sql语句
     * @param page       自定义的page对象
     * @param paras      参数列表
     * @return the Page object
     * @see #doPaginate(Config, Connection, int, int, String, Page, Object...)
     */
    public Page<Record> paginate(int pageNumber, int pageSize, String sql, Page<Record> page, Object... paras) {
        if(page == null){
            page = new ProvidePage<Record>();
        }
        Connection conn = null;
        try {
            conn = config.getConnection();
            return doPaginate(config, conn, pageNumber, pageSize, sql, page, paras);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    public Page<Record> paginate(int pageNumber, int pageSize, String sql, Object... paras) {
        return paginate(pageNumber, pageSize, sql, null, paras);
    }

    public Page<Record> paginate(int pageNumber, int pageSize, String sql) {
        return paginate(pageNumber, pageSize, sql, DbKit.NULL_PARA_ARRAY);
    }

    Page<Record> paginate(Config config, Connection conn, int pageNumber, int pageSize, String sql, Object... paras) throws SQLException {
        return doPaginate(config, conn, pageNumber, pageSize, sql, new ProvidePage<Record>(), paras);
    }

    Page<Record> doPaginate(Config config, Connection conn, int pageNumber, int pageSize, String sql, Page<Record> page, Object... paras) throws SQLException {
        if (pageNumber < 1 || pageSize < 1) {
            throw new HyacinthException("pageNumber and pageSize must more than 0");
        }

        String totalSql = BuildKit.buildTotalSql(sql);
        long totalRow = Db.queryColumn(config, conn, totalSql, paras);

        page.setPageNumber(pageNumber).setPageSize(pageSize);
        if (totalRow == 0) {
            return page.setList(new ArrayList<Record>(0)).setTotalPage(0).setTotalRow(0);
        }

        int totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }

        if (pageNumber > totalPage) {
            return page.setList(new ArrayList<Record>(0)).setTotalPage(totalPage).setTotalRow((int) totalRow);
        }

        // --------
        String pageSql = config.dialect.forPaginate(pageNumber, pageSize, sql);
        List<Record> list = find(config, conn, pageSql, paras);
        return page.setList(list).setTotalPage(totalPage).setTotalRow((int) totalRow);
    }

    boolean save(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        String[] pKeys = primaryKey.split(",");
        List<Object> paras = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        config.dialect.forDbSave(tableName, pKeys, record, sql, paras);

        PreparedStatement pst;
        if (config.dialect.isOracle())
            pst = conn.prepareStatement(sql.toString(), pKeys);
        else
            pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

        config.dialect.fillStatement(pst, paras);
        int result = pst.executeUpdate();
        getGeneratedKey(pst, record, pKeys);
        DbKit.close(pst);
        return result >= 1;
    }

    /**
     * Get id after save record.
     */
    private void getGeneratedKey(PreparedStatement pst, Record record, String[] pKeys) throws SQLException {
        ResultSet rs = pst.getGeneratedKeys();
        for (String pKey : pKeys)
            if (record.get(pKey) == null || config.dialect.isOracle())
                if (rs.next())
                    record.set(pKey, rs.getObject(1));
        rs.close();
    }

    /**
     * Save record.
     * <pre>
     * Example:
     * Record userRole = new Record().set("user_id", 123).set("role_id", 456);
     * DbPro.use().save("user_role", "user_id, role_id", userRole);
     * </pre>
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the record will be saved
     */
    public boolean save(String tableName, String primaryKey, Record record) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return save(config, conn, tableName, primaryKey, record);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @see #save(String, String, Record)
     */
    public boolean save(String tableName, Record record) {
        return save(tableName, config.dialect.getDefaultPrimaryKey(), record);
    }

    boolean update(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        String[] pKeys = primaryKey.split(",");
        Object[] ids = new Object[pKeys.length];

        for (int i = 0; i < pKeys.length; i++) {
            ids[i] = record.get(pKeys[i].trim());    // .trim() is important!
            if (ids[i] == null)
                throw new HyacinthException("You can't update record without Primary Key, " + pKeys[i] + " can not be null.");
        }

        StringBuilder sql = new StringBuilder();
        List<Object> paras = new ArrayList<Object>();
        config.dialect.forDbUpdate(tableName, pKeys, ids, record, sql, paras);

        if (paras.size() <= 1) {    // Needn't update
            return false;
        }

        return update(config, conn, sql.toString(), paras.toArray()) >= 1;
    }

    /**
     * Update Record.
     * <pre>
     * Example:
     * DbPro.use().update("user_role", "user_id, role_id", record);
     * </pre>
     *
     * @param tableName  the table name of the Record save to
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the Record object
     */
    public boolean update(String tableName, String primaryKey, Record record) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return update(config, conn, tableName, primaryKey, record);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * Update record with default primary key.
     * <pre>
     * Example:
     * DbPro.use().update("user", record);
     * </pre>
     *
     * @see #update(String, String, Record)
     */
    public boolean update(String tableName, Record record) {
        return update(tableName, config.dialect.getDefaultPrimaryKey(), record);
    }

    private int[] batch(Config config, Connection conn, String sql, Object[][] paras, int batchSize) throws SQLException {
        if (paras == null || paras.length == 0)
            return new int[0];
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int[] result = new int[paras.length];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < paras.length; i++) {
            for (int j = 0; j < paras[i].length; j++) {
                Object value = paras[i][j];
                if (config.dialect.isOracle()) {
                    if (value instanceof java.sql.Date)
                        pst.setDate(j + 1, (java.sql.Date) value);
                    else if (value instanceof Timestamp)
                        pst.setTimestamp(j + 1, (Timestamp) value);
                    else
                        pst.setObject(j + 1, value);
                } else
                    pst.setObject(j + 1, value);
            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                if (isInTransaction == false)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        int[] r = pst.executeBatch();
        if (isInTransaction == false)
            conn.commit();
        for (int k = 0; k < r.length; k++)
            result[pointer++] = r[k];
        DbKit.close(pst);
        return result;
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     * <pre>
     * Example:
     * String sql = "insert into user(name, cash) values(?, ?)";
     * int[] result = DbPro.use().batch(sql, new Object[][]{{"James", 888}, {"zhanjin", 888}});
     * </pre>
     *
     * @param sql   The SQL to execute.
     * @param paras An array of query replacement parameters.  Each row in this array is one set of batch replacement values.
     * @return The number of rows updated per statement
     */
    public int[] batch(String sql, Object[][] paras, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sql, paras, batchSize);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            config.close(conn);
        }
    }

    private int[] batch(Config config, Connection conn, String sql, String columns, List list, int batchSize) throws SQLException {
        if (list == null || list.size() == 0)
            return new int[0];
        Object element = list.get(0);
        if (!(element instanceof Record) && !(element instanceof Model))
            throw new IllegalArgumentException("The element in list must be Model or Record.");
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");
        boolean isModel = element instanceof Model;

        String[] columnArray = columns.split(",");
        for (int i = 0; i < columnArray.length; i++)
            columnArray[i] = columnArray[i].trim();

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int size = list.size();
        int[] result = new int[size];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < size; i++) {
            Map map = isModel ? ((Model) list.get(i)).getAttrs() : ((Record) list.get(i)).getColumns();
            for (int j = 0; j < columnArray.length; j++) {
                Object value = map.get(columnArray[j]);
                if (config.dialect.isOracle()) {
                    if (value instanceof java.sql.Date)
                        pst.setDate(j + 1, (java.sql.Date) value);
                    else if (value instanceof Timestamp)
                        pst.setTimestamp(j + 1, (Timestamp) value);
                    else
                        pst.setObject(j + 1, value);
                } else
                    pst.setObject(j + 1, value);
            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                if (isInTransaction == false)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        int[] r = pst.executeBatch();
        if (isInTransaction == false)
            conn.commit();
        for (int k = 0; k < r.length; k++)
            result[pointer++] = r[k];
        DbKit.close(pst);
        return result;
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     * <pre>
     * Example:
     * String sql = "insert into user(name, cash) values(?, ?)";
     * int[] result = DbPro.use().batch(sql, "name, cash", modelList, 500);
     * </pre>
     *
     * @param sql               The SQL to execute.
     * @param columns           the columns need be processed by sql.
     * @param modelOrRecordList model or record object list.
     * @param batchSize         batch size.
     * @return The number of rows updated per statement
     */
    public int[] batch(String sql, String columns, List modelOrRecordList, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sql, columns, modelOrRecordList, batchSize);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            config.close(conn);
        }
    }

    private int[] batch(Config config, Connection conn, List<String> sqlList, int batchSize) throws SQLException {
        if (sqlList == null || sqlList.size() == 0)
            return new int[0];
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int size = sqlList.size();
        int[] result = new int[size];
        Statement st = conn.createStatement();
        for (int i = 0; i < size; i++) {
            st.addBatch(sqlList.get(i));
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = st.executeBatch();
                if (isInTransaction == false)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        int[] r = st.executeBatch();
        if (isInTransaction == false)
            conn.commit();
        for (int k = 0; k < r.length; k++)
            result[pointer++] = r[k];
        DbKit.close(st);
        return result;
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     * <pre>
     * Example:
     * int[] result = DbPro.use().batch(sqlList, 500);
     * </pre>
     *
     * @param sqlList   The SQL list to execute.
     * @param batchSize batch size.
     * @return The number of rows updated per statement
     */
    public int[] batch(List<String> sqlList, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sqlList, batchSize);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            config.close(conn);
        }
    }

    /**
     * Batch save models using the "insert into ..." sql generated by the first model in modelList.
     * Ensure all the models can use the same sql as the first model.
     */
    public int[] batchSave(List<? extends Model> modelList, int batchSize) {
        if (modelList == null || modelList.size() == 0)
            return new int[0];

        Model model = modelList.get(0);
        Map<String, Object> attrs = model.getAttrs();
        String[] attrNames = new String[attrs.entrySet().size()];
        int index = 0;
        // the same as the iterator in Dialect.forModelSave() to ensure the order of the attrs
        for (Map.Entry<String, Object> e : attrs.entrySet())
            attrNames[index++] = e.getKey();
        String columns = StringTools.join(attrNames, ",");

        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<Object>();
        config.dialect.forModelSave(TableMapping.me().getMapping(model.getClass()), attrs, sql, parasNoUse);
        return batch(sql.toString(), columns, modelList, batchSize);
    }

    /**
     * Batch save records using the "insert into ..." sql generated by the first record in recordList.
     * Ensure all the record can use the same sql as the first record.
     *
     * @param tableName the table name
     */
    public int[] batchSave(String tableName, List<Record> recordList, int batchSize) {
        if (recordList == null || recordList.size() == 0)
            return new int[0];

        Record record = recordList.get(0);
        Map<String, Object> cols = record.getColumns();
        String[] colNames = new String[cols.entrySet().size()];
        int index = 0;
        // the same as the iterator in Dialect.forDbSave() to ensure the order of the columns
        for (Map.Entry<String, Object> e : cols.entrySet())
            colNames[index++] = e.getKey();
        String columns = StringTools.join(colNames, ",");

        String[] pKeysNoUse = new String[0];
        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<Object>();
        config.dialect.forDbSave(tableName, pKeysNoUse, record, sql, parasNoUse);
        return batch(sql.toString(), columns, recordList, batchSize);
    }

    /**
     * Batch update models using the attrs names of the first model in modelList.
     * Ensure all the models can use the same sql as the first model.
     */
    public int[] batchUpdate(List<? extends Model> modelList, int batchSize) {
        if (modelList == null || modelList.size() == 0)
            return new int[0];

        Model model = modelList.get(0);
        Table table = TableMapping.me().getMapping(model.getClass());
        String[] pKeys = table.getPrimaryKey();
        Map<String, Object> attrs = model.getAttrs();
        List<String> attrNames = new ArrayList<String>();
        // the same as the iterator in Dialect.forModelSave() to ensure the order of the attrs
        for (Map.Entry<String, Object> e : attrs.entrySet()) {
            String attr = e.getKey();
            if (config.dialect.isPrimaryKey(attr, pKeys) == false)
                attrNames.add(attr);
        }
        for (String pKey : pKeys)
            attrNames.add(pKey);
        String columns = StringTools.join(attrNames.toArray(new String[attrNames.size()]), ",");

        // update all attrs of the model not use the midifyFlag of every single model
        Set<String> modifyFlag = attrs.keySet();    // model.getModifyFlag();

        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<Object>();
        config.dialect.forModelUpdate(TableMapping.me().getMapping(model.getClass()), attrs, modifyFlag, sql, parasNoUse);
        return batch(sql.toString(), columns, modelList, batchSize);
    }

    /**
     * Batch update records using the columns names of the first record in recordList.
     * Ensure all the records can use the same sql as the first record.
     *
     * @param tableName  the table name
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     */
    public int[] batchUpdate(String tableName, String primaryKey, List<Record> recordList, int batchSize) {
        if (recordList == null || recordList.size() == 0)
            return new int[0];

        String[] pKeys = primaryKey.split(",");
        config.dialect.trimPrimaryKeys(pKeys);

        Record record = recordList.get(0);
        Map<String, Object> cols = record.getColumns();
        List<String> colNames = new ArrayList<String>();
        // the same as the iterator in Dialect.forDbUpdate() to ensure the order of the columns
        for (Map.Entry<String, Object> e : cols.entrySet()) {
            String col = e.getKey();
            if (config.dialect.isPrimaryKey(col, pKeys) == false)
                colNames.add(col);
        }
        for (String pKey : pKeys)
            colNames.add(pKey);
        String columns = StringTools.join(colNames.toArray(new String[colNames.size()]), ",");

        Object[] idsNoUse = new Object[pKeys.length];
        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<Object>();
        config.dialect.forDbUpdate(tableName, pKeys, idsNoUse, record, sql, parasNoUse);
        return batch(sql.toString(), columns, recordList, batchSize);
    }

    /**
     * Batch update records with default primary key, using the columns names of the first record in recordList.
     * Ensure all the records can use the same sql as the first record.
     *
     * @param tableName the table name
     */
    public int[] batchUpdate(String tableName, List<Record> recordList, int batchSize) {
        return batchUpdate(tableName, config.dialect.getDefaultPrimaryKey(), recordList, batchSize);
    }

}
