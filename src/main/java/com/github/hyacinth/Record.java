package com.github.hyacinth;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 通用数据承载对象
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/11
 * Time: 15:52
 */
public class Record {
    private static final long serialVersionUID = 905784513600884082L;

    /**
     * 数据承载model
     */
    private Map<String, Object> columns;

    /**
     * 根据配置设置container
     *
     * @param configName 配置名
     */
    public Record setContainerByConfigName(String configName) {
        Config config = DbKit.getConfig(configName);
        if (config == null)
            throw new IllegalArgumentException("Config not found: " + configName);

        processColumnsMap(config);
        return this;
    }

    // 仅仅在 RecordBuilder中使用
    void setColumnsMap(Map<String, Object> columns) {
        this.columns = columns;
    }

    @SuppressWarnings("unchecked")
    private void processColumnsMap(Config config) {
        if (columns == null || columns.size() == 0) {
            columns = config.container.getColumnsMap();
        } else {
            Map<String, Object> columnsOld = columns;
            columns = config.container.getColumnsMap();
            columns.putAll(columnsOld);
        }
    }

    /**
     * 获取数据承载Map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getColumns() {
        if (columns == null) {
            if (DbKit.config == null) {
                columns = DbKit.brokenConfig.container.getColumnsMap();
            } else {
                columns = DbKit.config.container.getColumnsMap();
            }
        }
        return columns;
    }

    /**
     * 设置数据
     *
     * @param columns data map
     */
    public Record setColumns(Map<String, Object> columns) {
        this.getColumns().putAll(columns);
        return this;
    }

    /**
     * 设置record数据为指定record数据
     *
     * @param record record
     */
    public Record setColumns(Record record) {
        getColumns().putAll(record.getColumns());
        return this;
    }

    /**
     * 设置record数据为指定model数据
     *
     * @param model model
     */
    public Record setColumns(Model<?> model) {
        getColumns().putAll(model.getAttrs());
        return this;
    }

    /**
     * 移除单属性
     *
     * @param column 需要移除的属性名
     */
    public Record remove(String column) {
        getColumns().remove(column);
        return this;
    }

    /**
     * 移除多个属性
     *
     * @param columns 需要移除的属性名数组
     */
    public Record remove(String... columns) {
        if (columns != null)
            for (String c : columns) {
                this.getColumns().remove(c);
            }
        return this;
    }

    /**
     * 移除属性值为空的属性
     */
    public Record removeNullValueColumns() {
        for (java.util.Iterator<Map.Entry<String, Object>> it = getColumns().entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> e = it.next();
            if (e.getValue() == null) {
                it.remove();
            }
        }
        return this;
    }

    /**
     * 保留指定属性列，删除其他属性列
     *
     * @param columns 需要保留的属性列
     */
    public Record keep(String... columns) {
        if (columns != null && columns.length > 0) {
            Map<String, Object> newColumns = new HashMap<String, Object>(columns.length);    // getConfig().container.getColumnsMap();
            for (String c : columns)
                if (this.getColumns().containsKey(c))    // prevent put null value to the newColumns
                    newColumns.put(c, this.getColumns().get(c));

            this.getColumns().clear();
            this.getColumns().putAll(newColumns);
        } else
            this.getColumns().clear();
        return this;
    }

    /**
     * @see #keep(String...)
     */
    public Record keep(String column) {
        if (getColumns().containsKey(column)) {    // prevent put null value to the newColumns
            Object keepIt = getColumns().get(column);
            getColumns().clear();
            getColumns().put(column, keepIt);
        } else
            getColumns().clear();
        return this;
    }

    /**
     * 清空所有属性
     */
    public Record clear() {
        getColumns().clear();
        return this;
    }

    /**
     * 设置属性列
     *
     * @param column 属性列名
     * @param value  属性值
     */
    public Record set(String column, Object value) {
        getColumns().put(column, value);
        return this;
    }

    /**
     * 获取指定属性值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String column) {
        return (T) getColumns().get(column);
    }

    /**
     * 获取指定属性值，如果为空，范围默认值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String column, Object defaultValue) {
        Object result = getColumns().get(column);
        return (T) (result != null ? result : defaultValue);
    }

    /**
     * 以下13个方法，用于获取指定Java类型的数据
     * <p>
     * String,int,long,bigInteger,date,time,timestamp,double,float,boolean,bigDecimal,bytes,number
     *
     * @param column 属性列名
     * @return javaType
     */
    public String getStr(String column) {
        return (String) getColumns().get(column);
    }

    public Integer getInt(String column) {
        return (Integer) getColumns().get(column);
    }

    public Long getLong(String column) {
        return (Long) getColumns().get(column);
    }

    public java.math.BigInteger getBigInteger(String column) {
        return (java.math.BigInteger) getColumns().get(column);
    }

    public java.util.Date getDate(String column) {
        return (java.util.Date) getColumns().get(column);
    }

    public java.sql.Time getTime(String column) {
        return (java.sql.Time) getColumns().get(column);
    }

    public java.sql.Timestamp getTimestamp(String column) {
        return (java.sql.Timestamp) getColumns().get(column);
    }

    public Double getDouble(String column) {
        return (Double) getColumns().get(column);
    }

    public Float getFloat(String column) {
        return (Float) getColumns().get(column);
    }

    public Boolean getBoolean(String column) {
        return (Boolean) getColumns().get(column);
    }

    public java.math.BigDecimal getBigDecimal(String column) {
        return (java.math.BigDecimal) getColumns().get(column);
    }

    public byte[] getBytes(String column) {
        return (byte[]) getColumns().get(column);
    }

    public Number getNumber(String column) {
        return (Number) getColumns().get(column);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> e : getColumns().entrySet()) {
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
        if (!(o instanceof Record))
            return false;
        if (o == this)
            return true;
        return this.getColumns().equals(((Record) o).getColumns());
    }

    public int hashCode() {
        return getColumns() == null ? 0 : getColumns().hashCode();
    }

    /**
     * 获取所有属性列
     */
    public String[] getColumnNames() {
        Set<String> attrNameSet = getColumns().keySet();
        return attrNameSet.toArray(new String[attrNameSet.size()]);
    }

    /**
     * 获取所有属性值
     */
    public Object[] getColumnValues() {
        java.util.Collection<Object> attrValueCollection = getColumns().values();
        return attrValueCollection.toArray(new Object[attrValueCollection.size()]);
    }
}
