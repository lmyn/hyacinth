package com.github.hyacinth;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/10
 * Time: 9:42
 */
public class TableMapping {
    private final Map<Class<? extends Model<?>>, Table> mapping = new HashMap<Class<? extends Model<?>>, Table>();

    private static TableMapping me = new TableMapping();

    private TableMapping() {
    }

    public static TableMapping me() {
        return me;
    }

    public void addMapping(Table table) {
        mapping.put(table.getModelClass(), table);
    }

    public Table getMapping(Class<? extends Model> modelClass) {
        return mapping.get(modelClass);
    }
}
