package com.github.hyacinth;

import com.github.hyacinth.tools.StringTools;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/5
 * Time: 15:35
 */
public class Table {
    private String name;
    private String[] primaryKey = null;
    private Map<String, Class<?>> columnTypeMap = new HashMap<String, Class<?>>();
    private Map<String, Column> columnMap = new HashMap<String, Column>();

    private Class<? extends Model<?>> modelClass;

    public Table(String name, Class<? extends Model<?>> modelClass) {
        if (StringTools.isBlank(name))
            throw new IllegalArgumentException("Table name can not be blank.");
        if (modelClass == null)
            throw new IllegalArgumentException("Model class can not be null.");

        this.name = name.trim();
        this.modelClass = modelClass;
    }

    void setPrimaryKey(String[] primaryKeys) {
        this.primaryKey = primaryKeys;
    }

    void setPrimaryKey(String primaryKey) {
        String[] arr = primaryKey.split(",");
        for (int i = 0; i < arr.length; i++)
            arr[i] = arr[i].trim();
        this.primaryKey = arr;
    }

    void setColumnTypeMap(Map<String, Class<?>> columnTypeMap) {
        if (columnTypeMap == null) {
            throw new IllegalArgumentException("columnTypeMap can not be null");
        }

        this.columnTypeMap = columnTypeMap;
    }

    public String getName() {
        return name;
    }

    void setColumnType(String columnLabel, Class<?> columnType) {
        columnTypeMap.put(columnLabel, columnType);
    }

    public Class<?> getColumnType(String columnLabel) {
        return columnTypeMap.get(columnLabel);
    }

    void setColumnMap(Map<String, Column> columnMap) {
        if (columnMap == null) {
            throw new IllegalArgumentException("columnMap can not be null");
        }

        this.columnMap = columnMap;
    }

    void setColumn(String field, Column column) {
        this.columnMap.put(field, column);
    }

    /**
     * 验证指定列是否在存在于当前model
     */
    public boolean hasColumnLabel(String columnLabel) {
        return columnTypeMap.containsKey(columnLabel);
    }

    /**
     * 获取主键 update() and delete() 使用.
     */
    public String[] getPrimaryKey() {
        return primaryKey;
    }

    public Class<? extends Model<?>> getModelClass() {
        return modelClass;
    }

    public Map<String, Class<?>> getColumnTypeMap() {
        return Collections.unmodifiableMap(columnTypeMap);
    }

    public Set<Map.Entry<String, Class<?>>> getColumnTypeMapEntrySet() {
        return columnTypeMap.entrySet();
    }
}
