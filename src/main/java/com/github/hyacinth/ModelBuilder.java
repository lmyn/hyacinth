package com.github.hyacinth;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 将ResultSet封装成Model
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/3
 * Time: 10:34
 */
public class ModelBuilder {

    /**
     * 用于处理返回结果为单条记录或取结果集第一条记录应用场景
     *
     *
     * @param rs         ResultSet
     * @param modelClass modelClass
     * @param <M>        model
     * @return model 如果查询结果集为空则返回null
     */
    public static <M extends Model> M build(ResultSet rs, Class<M> modelClass) throws SQLException, IllegalAccessException, InstantiationException {
        M model = null;
        // 获取结果集数据结构以及相应列的数据类型
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        // +1 是为了和ResultSet中索引同步（ResultSet索引从1开始），统一循环的起始索引为1
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        RsKit.bindLabelNamesAndTypes(rsmd, labelNames, types);

        if (rs.next()) {
            model = modelClass.newInstance();
            RsKit.fetch(rs, columnCount, labelNames, types, model.getAttrs());
        }
        return model;
    }

    /**
     * 用于处理需要获取多条结果集
     *
     * @param rs         rs
     * @param modelClass modelClass
     * @param <M>        model
     * @return List<model></>
     */
    public static <M extends Model> List<M> buildList(ResultSet rs, Class<M> modelClass) throws SQLException, IllegalAccessException, InstantiationException {
        List<M> list = new ArrayList<M>();
        // 获取结果集数据结构以及相应列的数据类型
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        // +1 是为了和ResultSet中索引同步（ResultSet索引从1开始），统一循环的起始索引为1
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        RsKit.bindLabelNamesAndTypes(rsmd, labelNames, types);

        while (rs.next()) {
            M model = modelClass.newInstance();
            RsKit.fetch(rs, columnCount, labelNames, types, model.getAttrs());
            list.add(model);
        }
        return list;
    }


}
