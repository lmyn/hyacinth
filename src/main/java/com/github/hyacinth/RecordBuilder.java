package com.github.hyacinth;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将ResultSet封装成Record
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 10:48
 */
public class RecordBuilder {

    /**
     * 用于处理返回结果为单条记录或取结果集第一条记录应用场景
     *
     * @param rs     结果集
     * @param config
     * @return Record
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Map<String, Object> build(Config config, ResultSet rs) throws SQLException {
        Map<String, Object> data = null;
        // 获取结果集数据结构以及相应列的数据类型
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        // +1 是为了和ResultSet中索引同步（ResultSet索引从1开始），统一循环的起始索引为1
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        RsKit.bindLabelNamesAndTypes(rsmd, labelNames, types);

        if (rs.next()) {
            data = new HashMap<String, Object>();
            RsKit.fetch(rs, columnCount, labelNames, types, data);

        }
        return data;
    }

    /**
     * 用于处理需要获取多条结果集
     *
     * @param rs     结果集
     * @param config
     * @return List<Record>
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static List<Map<String, Object>> buildList(Config config, ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 获取结果集数据结构以及相应列的数据类型
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        // +1 是为了和ResultSet中索引同步（ResultSet索引从1开始），统一循环的起始索引为1
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        RsKit.bindLabelNamesAndTypes(rsmd, labelNames, types);

        while (rs.next()) {
            Map<String, Object> record = new HashMap<String, Object>();
            RsKit.fetch(rs, columnCount, labelNames, types, record);
            list.add(record);
        }
        return list;
    }
}
