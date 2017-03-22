package com.github.hyacinth;

import com.github.hyacinth.sql.BuildKit;
import com.github.hyacinth.sql.SqlCache;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/12/29
 * Time: 19:50
 */
public abstract class Model<M extends Model> implements Bean, Serializable {

    private static final long serialVersionUID = 1288885111278391312L;

    public static final int FILTER_BY_SAVE = 0;
    public static final int FILTER_BY_UPDATE = 1;

    public M dao() {
        attrs = Dao.daoMap;
        modifyFlag = Dao.daoSet;
        return (M) this;
    }

    /**
     * Attributes of this model
     */
    private Map<String, Object> attrs = getAttrsMap();

    @Override
    public Map<String, Object> getAttrsMap() {
        Config config = getConfig();
        if (config == null)
            return DbKit.brokenConfig.container.getAttrsMap();
        return config.container.getAttrsMap();
    }

    /**
     * Flag of column has been modified. update need this flag
     */
    private Set<String> modifyFlag;

	/*
    private Set<String> getModifyFlag() {
		if (modifyFlag == null)
			modifyFlag = getConfig().container.getModifyFlagSet();	// new HashSet<String>();
		return modifyFlag;
	}*/

    Set<String> getModifyFlag() {
        if (modifyFlag == null) {
            Config config = getConfig();
            if (config == null)
                modifyFlag = DbKit.brokenConfig.container.getModifyFlagSet();
            else
                modifyFlag = config.container.getModifyFlagSet();
        }
        return modifyFlag;
    }

    private String configName = null;

    /**
     * Switching data source, dialect and all config by configName
     */
    public M use(String configName) {
        this.configName = configName;
        return (M) this;
    }

    protected Config getConfig() {
        if (configName != null)
            return DbKit.getConfig(configName);
        return DbKit.getConfig(getUsefulClass());
    }

	/*
    private Config getConfig() {
		return DbKit.getConfig(getUsefulClass());
	}*/

    private Table getMapping() {
        return TableMapping.me().getMapping(getUsefulClass());
    }

    /**
     * Set attribute to model.
     *
     * @param attr  the attribute name of the model
     * @param value the value of the attribute
     * @return this model
     * @throws HyacinthException if the attribute is not exists of the model
     */
    public M set(String attr, Object value) {
        Table table = getMapping();
        // 用于未启动 ActiveRecordPlugin 场景下使用 Model
        if (table == null) {
            attrs.put(attr, value);
            getModifyFlag().add(attr);
            return (M) this;
        }
        if (table.hasColumnLabel(attr)) {
            attrs.put(attr, value);
            getModifyFlag().add(attr);    // Add modify flag, update() need this flag.
            return (M) this;
        }
        throw new HyacinthException("The attribute name does not exist: " + attr);
    }

    /**
     * Put key value pair to the model without check attribute name.
     */
    public M put(String key, Object value) {
        attrs.put(key, value);
        return (M) this;
    }

    /**
     * Put map to the model without check attribute name.
     */
    public M put(Map<String, Object> map) {
        attrs.putAll(map);
        return (M) this;
    }

    /**
     * Put other model to the model without check attribute name.
     */
    public M put(Model model) {
        attrs.putAll(model.getAttrs());
        return (M) this;
    }

    /**
     * Put record to the model without check attribute name.
     */
    public M put(Record record) {
        attrs.putAll(record.getColumns());
        return (M) this;
    }

    /**
     * Convert model to record.
     */
    public Record toRecord() {
        return new Record().setColumns(getAttrs());
    }

    /**
     * Get attribute of any mysql type
     */
    public <T> T get(String attr) {
        return (T) (attrs.get(attr));
    }

    /**
     * Get attribute of any mysql type. Returns defaultValue if null.
     */
    public <T> T get(String attr, Object defaultValue) {
        Object result = attrs.get(attr);
        return (T) (result != null ? result : defaultValue);
    }


    /**
     * 以下13个方法，用于获取指定Java类型的数据
     * <p>
     * String,int,long,bigInteger,date,time,timestamp,double,float,boolean,bigDecimal,bytes,number
     *
     * @param attr 属性名
     */
    public String getStr(String attr) {
        return (String) attrs.get(attr);
    }

    public Integer getInt(String attr) {
        return (Integer) attrs.get(attr);
    }

    public Long getLong(String attr) {
        return (Long) attrs.get(attr);
    }

    public java.math.BigInteger getBigInteger(String attr) {
        return (java.math.BigInteger) attrs.get(attr);
    }

    public java.util.Date getDate(String attr) {
        return (java.util.Date) attrs.get(attr);
    }

    public Time getTime(String attr) {
        return (Time) attrs.get(attr);
    }

    public Timestamp getTimestamp(String attr) {
        return (Timestamp) attrs.get(attr);
    }

    public Double getDouble(String attr) {
        return (Double) attrs.get(attr);
    }

    public Float getFloat(String attr) {
        return (Float) attrs.get(attr);
    }

    public Boolean getBoolean(String attr) {
        return (Boolean) attrs.get(attr);
    }

    public java.math.BigDecimal getBigDecimal(String attr) {
        return (java.math.BigDecimal) attrs.get(attr);
    }

    public byte[] getBytes(String attr) {
        return (byte[]) attrs.get(attr);
    }

    public Number getNumber(String attr) {
        return (Number) attrs.get(attr);
    }

    /**
     * 分页
     *
     * @param pageNumber 当前页
     * @param pageSize   页大小（每页记录数）
     * @param key        key
     * @param page       自定义的page对象
     * @param paras      参数列表
     * @return the Page object
     * @see #doPaginate(Config, int, int, String, Page, Object...)
     */
    public Page<M> paginate(int pageNumber, int pageSize, String key, Page<M> page, Object... paras) {
        if(page == null){
            page = new ProvidePage<M>();
        }
        String sql = SqlCache.fixed.get(key);
        if (sql == null) {
            throw new HyacinthException("Sql can not find! key:" + key);
        }
        Config config = getConfig();
        return doPaginate(config, pageNumber, pageSize, sql, page, paras);
    }

    public Page<M> paginate(int pageNumber, int pageSize, String key, Page<M> page, Map<String, Object> paras) {
        if(page == null){
            page = new ProvidePage<M>();
        }
        List<Object> parasValueList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasValueList);
        Config config = getConfig();
        return doPaginate(config, pageNumber, pageSize, sql, page, parasValueList.toArray());
    }

    public Page<M> paginate(int pageNumber, int pageSize, String key, Page<M> page) {
        return paginate(pageNumber, pageSize, key, page, DbKit.NULL_PARA_ARRAY);
    }

    public Page<M> paginate(int pageNumber, int pageSize, String key, Object... paras) {
        return paginate(pageNumber, pageSize, key, null, paras);
    }

    public Page<M> paginate(int pageNumber, int pageSize, String key, Map<String, Object> paras) {
        return paginate(pageNumber, pageSize, key, null, paras);
    }

    /**
     * @see #paginate(int, int, String, Object...)
     */
    public Page<M> paginate(int pageNumber, int pageSize, String key) {
        return paginate(pageNumber, pageSize, key, DbKit.NULL_PARA_ARRAY);
    }

    private Page<M> doPaginate(Config config, int pageNumber, int pageSize, String sql, Page<M> page, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            if (pageNumber < 1 || pageSize < 1) {
                throw new HyacinthException("PageNumber and pageSize must more than 0");
            }

            String totalSql = BuildKit.buildTotalSql(sql);
            long totalRow = Db.queryColumn(config, conn, totalSql, paras);

            page.setPageNumber(pageNumber).setPageSize(pageSize);
            if (totalRow == 0) {
                return page.setList(new ArrayList<M>(0)).setTotalPage(0).setTotalRow(0);
            }

            int totalPage = (int) (totalRow / pageSize);
            if (totalRow % pageSize != 0) {
                totalPage++;
            }

            if (pageNumber > totalPage) {
                return page.setList(new ArrayList<M>(0)).setTotalPage(totalPage).setTotalRow((int) totalRow);
            }

            // --------
            String pageSql = config.dialect.forPaginate(pageNumber, pageSize, sql);

            List<M> list = find(conn, pageSql, paras);
            return page.setList(list).setTotalPage(totalPage).setTotalRow((int) totalRow);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * Return attribute Map.
     * <p>
     * Danger! The update method will ignore the attribute if you change it directly.
     * You must use set method to change attribute that update method can handle it.
     */
    protected Map<String, Object> getAttrs() {
        return attrs;
    }

    /**
     * Return attribute Set.
     */
    public Set<Map.Entry<String, Object>> _getAttrsEntrySet() {
        return attrs.entrySet();
    }

    /**
     * Save model.
     */
    public boolean save() {
        filter(FILTER_BY_SAVE);

        Config config = getConfig();
        Table table = getMapping();

        StringBuilder sql = new StringBuilder();
        List<Object> paras = new ArrayList<Object>();
        config.dialect.forModelSave(table, attrs, sql, paras);
        // if (paras.size() == 0)	return false;	// The fixed "insert into tableName() values()" works fine, so delete this line

        // --------
        Connection conn = null;
        PreparedStatement pst = null;
        int result = 0;
        try {
            conn = config.getConnection();
            if (config.dialect.isOracle())
                pst = conn.prepareStatement(sql.toString(), table.getPrimaryKey());
            else
                pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            config.dialect.fillStatement(pst, paras);
            result = pst.executeUpdate();
            getGeneratedKey(pst, table, config);
            getModifyFlag().clear();
            return result >= 1;
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(pst, conn);
        }
    }

    /**
     * Get id after save method.
     */
    private void getGeneratedKey(PreparedStatement pst, Table table, Config config) throws SQLException {
        String[] pKeys = table.getPrimaryKey();
        ResultSet rs = pst.getGeneratedKeys();
        for (String pKey : pKeys) {
            if (get(pKey) == null || config.dialect.isOracle()) {
                if (rs.next()) {
                    Class colType = table.getColumnType(pKey);
                    if (colType == Integer.class || colType == int.class)
                        set(pKey, rs.getInt(1));
                    else if (colType == Long.class || colType == long.class)
                        set(pKey, rs.getLong(1));
                    else
                        set(pKey, rs.getObject(1));        // It returns Long object for int colType
                }
            }
        }
        rs.close();
    }

    /**
     * Delete model.
     */
    public boolean delete() {
        Table table = getMapping();
        String[] pKeys = table.getPrimaryKey();
        Object[] ids = new Object[pKeys.length];
        for (int i = 0; i < pKeys.length; i++) {
            ids[i] = attrs.get(pKeys[i]);
            if (ids[i] == null)
                throw new HyacinthException("You can't delete model without primary key value, " + pKeys[i] + " is null");
        }
        return deleteById(table, ids);
    }

    /**
     * Delete model by id.
     *
     * @param idValue the id value of the model
     * @return true if delete succeed otherwise false
     */
    public boolean deleteById(Object idValue) {
        if (idValue == null)
            throw new IllegalArgumentException("idValue can not be null");
        return deleteById(getMapping(), idValue);
    }

    /**
     * Delete model by composite id values.
     *
     * @param idValues the composite id values of the model
     * @return true if delete succeed otherwise false
     */
    public boolean deleteById(Object... idValues) {
        Table table = getMapping();
        if (idValues == null || idValues.length != table.getPrimaryKey().length)
            throw new IllegalArgumentException("Primary key nubmer must equals id value number and can not be null");

        return deleteById(table, idValues);
    }

    private boolean deleteById(Table table, Object... idValues) {
        Config config = getConfig();
        Connection conn = null;
        try {
            conn = config.getConnection();
            String sql = config.dialect.forModelDeleteById(table);
            return Db.update(config, conn, sql, idValues) >= 1;
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * 更新model
     */
    public boolean update() {
        filter(FILTER_BY_UPDATE);

        if (getModifyFlag().isEmpty()) {
            return false;
        }

        Table table = getMapping();
        String[] pKeys = table.getPrimaryKey();
        for (String pKey : pKeys) {
            Object id = attrs.get(pKey);
            if (id == null)
                throw new HyacinthException("You can't update model without Primary Key, " + pKey + " can not be null.");
        }

        Config config = getConfig();
        StringBuilder sql = new StringBuilder();
        List<Object> paras = new ArrayList<Object>();
        config.dialect.forModelUpdate(table, attrs, getModifyFlag(), sql, paras);

        if (paras.size() <= 1) {    // Needn't update
            return false;
        }

        // --------
        Connection conn = null;
        try {
            conn = config.getConnection();
            int result = Db.update(config, conn, sql.toString(), paras.toArray());
            if (result >= 1) {
                getModifyFlag().clear();
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * 查询Model
     */
    private List<M> find(Connection conn, String sql, Object... paras) throws Exception {
        Config config = getConfig();
        Class<M> modelClass = getUsefulClass();
        if (config.devMode)
            checkTableName(modelClass, sql);

        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        List<M> result = ModelBuilder.buildList(rs, modelClass);
        DbKit.close(rs, pst);
        return result;
    }

    /**
     * 查询Model.
     *
     * @param sql   sql语句
     * @param paras fixed 参数
     * @return Model list
     */
    public List<M> find(String sql, Object... paras) {
        Config config = getConfig();
        Connection conn = null;
        try {
            conn = config.getConnection();
            return find(conn, sql, paras);
        } catch (Exception e) {
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @see #find(String, Object...)
     */
    public List<M> find(String key, Map<String, Object> paras) {
        List<Object> parasList = new ArrayList<Object>();
        String sql = DbKit.sqlBuilder.build(key, paras, parasList);
        return find(sql, parasList.toArray());
    }

    /**
     * @see #find(String, Map)
     */
    public List<M> find(String key, M model) {
        return find(key, model.getAttrs());
    }

    /**
     * @see #find(String, Object...)
     */
    public List<M> find(String key) {
        return find(SqlCache.fixed.get(key), DbKit.NULL_PARA_ARRAY);
    }

    /**
     * Check the table name. The table name must in fixed.
     */
    private void checkTableName(Class<? extends Model> modelClass, String sql) {
        Table table = TableMapping.me().getMapping(modelClass);
        if (!sql.toLowerCase().contains(table.getName().toLowerCase()))
            throw new HyacinthException("The table name: " + table.getName() + " not in your fixed.");
    }

    /**
     * 查询第一条记录. 建议在sql中加上 "limit 1"
     *
     * @param key   fixed key
     * @param paras fixed 参数
     * @return Model
     */
    public M findFirst(String key, Object... paras) {
        List<M> result = find(SqlCache.fixed.get(key), paras);
        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * @param key fixed key
     * @see #findFirst(String, Object...)
     */
    public M findFirst(String key) {
        return findFirst(SqlCache.fixed.get(key), DbKit.NULL_PARA_ARRAY);
    }

    /**
     * 根据Id查询 Model 适合单个主键的表.
     * <pre>
     * Example:
     * User user = User.dao.findById(123);
     * </pre>
     *
     * @param idValue the id value of the model
     */
    public M findById(Object idValue) {
        return findByIdLoadColumns(new Object[]{idValue}, "*");
    }

    /**
     * 根据Id查询 Model 适合复合主键的表.
     * <pre>
     * Example:
     * User user = User.dao.findById(123, 456);
     * </pre>
     *
     * @param idValues the composite id values of the model
     */
    public M findById(Object... idValues) {
        return findByIdLoadColumns(idValues, "*");
    }

    /**
     * @see #findByIdLoadColumns(Object[], String)
     */
    public M findByIdLoadColumns(Object idValue, String columns) {
        return findByIdLoadColumns(new Object[]{idValue}, columns);
    }

    /**
     * 根据主键查询Model，但只查询特定的列
     * <pre>
     * Example:
     * User user = User.dao.findByIdLoadColumns(new Object[]{123, 456}, "name, age");
     * </pre>
     *
     * @param idValues 主键ID值
     * @param columns  需要查询的列， 多个列可以用 “,” 号分隔
     */
    public M findByIdLoadColumns(Object[] idValues, String columns) {
        Table table = getMapping();
        if (table.getPrimaryKey().length != idValues.length)
            throw new IllegalArgumentException("id values error, need " + table.getPrimaryKey().length + " id value");

        String sql = getConfig().dialect.forModelFindById(table, columns);
        List<M> result = find(sql, idValues);
        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * @see #_setAttrs(Map)
     */
    public M _setAttrs(M model) {
        return (M) _setAttrs(model.getAttrs());
    }

    /**
     * 设置Model属性值
     *
     * @param attrs Map<String, Object></>
     * @return model
     */
    public M _setAttrs(Map<String, Object> attrs) {
        for (Map.Entry<String, Object> e : attrs.entrySet())
            set(e.getKey(), e.getValue());
        return (M) this;
    }

    /**
     * @see #remove(String...)
     */
    public M remove(String attr) {
        attrs.remove(attr);
        getModifyFlag().remove(attr);
        return (M) this;
    }

    /**
     * 从model中移除属性
     *
     * @param attrs 属性名
     * @return 返回model
     */
    public M remove(String... attrs) {
        if (attrs != null)
            for (String attr : attrs) {
                this.attrs.remove(attr);
                this.getModifyFlag().remove(attr);
            }
        return (M) this;
    }

    /**
     * 移除属性值为空的属性
     *
     * @return 返回model
     */
    public M removeNullValueAttrs() {
        for (Iterator<Map.Entry<String, Object>> it = attrs.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> e = it.next();
            if (e.getValue() == null) {
                it.remove();
                getModifyFlag().remove(e.getKey());
            }
        }
        return (M) this;
    }

    /**
     * 保留指定属性（多个），移除其他属性
     *
     * @param attrs 需要保留的属性
     * @return 返回修改后的model
     */
    public M keep(String... attrs) {
        if (attrs != null && attrs.length > 0) {
            Config config = getConfig();
            Map<String, Object> newAttrs = config.container.getAttrsMap();    // new HashMap<String, Object>(attrs.length);
            Set<String> newModifyFlag = config.container.getModifyFlagSet();    // new HashSet<String>();
            for (String a : attrs) {
                if (this.attrs.containsKey(a))    // prevent put null value to the newColumns
                    newAttrs.put(a, this.attrs.get(a));
                if (this.getModifyFlag().contains(a))
                    newModifyFlag.add(a);
            }
            this.attrs = newAttrs;
            this.modifyFlag = newModifyFlag;
        } else {
            this.attrs.clear();
            this.getModifyFlag().clear();
        }
        return (M) this;
    }

    /**
     * 保留指定属性（单个），移除其他属性
     *
     * @param attr 需要保留的属性
     * @return 返回修改后的model
     */
    public M keep(String attr) {
        if (attrs.containsKey(attr)) {    // prevent put null value to the newColumns
            Object keepIt = attrs.get(attr);
            boolean keepFlag = getModifyFlag().contains(attr);
            attrs.clear();
            getModifyFlag().clear();
            attrs.put(attr, keepIt);
            if (keepFlag)
                getModifyFlag().add(attr);
        } else {
            attrs.clear();
            getModifyFlag().clear();
        }
        return (M) this;
    }

    /**
     * 清除属性
     *
     * @return 返回修改后的model
     */
    public M clear() {
        attrs.clear();
        getModifyFlag().clear();
        return (M) this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> e : attrs.entrySet()) {
            if (first)
                first = false;
            else
                sb.append(", ");

            Object value = e.getValue();
            if (value != null)
                value = value.toString();
            sb.append(e.getKey()).append(":").append(value);
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Model))
            return false;
        if (getUsefulClass() != ((Model) o).getUsefulClass())
            return false;
        if (o == this)
            return true;
        return this.attrs.equals(((Model) o).attrs);
    }

    public int hashCode() {
        return (attrs == null ? 0 : attrs.hashCode()) ^ (getModifyFlag() == null ? 0 : getModifyFlag().hashCode());
    }

    /**
     * 获取所有属性名
     */
    public String[] _getAttrNames() {
        Set<String> attrNameSet = attrs.keySet();
        return attrNameSet.toArray(new String[attrNameSet.size()]);
    }

    /**
     * 获取所有值（不包括属性名称）
     */
    public Object[] _getAttrValues() {
        Collection<Object> attrValueCollection = attrs.values();
        return attrValueCollection.toArray(new Object[attrValueCollection.size()]);
    }

    private Class<M> getUsefulClass() {
        Class c = getClass();
        return c.getName().indexOf("EnhancerByCGLIB") == -1 ? c : c.getSuperclass();    // com.demo.blog.Blog$$EnhancerByCGLIB$$69a17158
    }

    /**
     * filter () 方法将被 save()、update() 调用，可用于过滤类似于 XSS 攻击脚本
     *
     * @param filterBy 0 表示当前正被 save() 调用, 1 表示当前正被 update() 调用
     */
    protected void filter(int filterBy) {

    }


}
