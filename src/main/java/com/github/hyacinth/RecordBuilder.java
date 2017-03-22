package com.github.hyacinth;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public static Record build(Config config, ResultSet rs) throws SQLException {
        Record record = null;
        // 获取结果集数据结构以及相应列的数据类型
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        // +1 是为了和ResultSet中索引同步（ResultSet索引从1开始），统一循环的起始索引为1
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        RsKit.bindLabelNamesAndTypes(rsmd, labelNames, types);

        if (rs.next()) {
            record = new Record();
            record.setColumnsMap(config.container.getColumnsMap());
            RsKit.fetch(rs, columnCount, labelNames, types, record.getColumns());

        }
        return record;
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
    public static List<Record> buildList(Config config, ResultSet rs) throws SQLException {
        List<Record> list = new ArrayList<Record>();
        // 获取结果集数据结构以及相应列的数据类型
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        // +1 是为了和ResultSet中索引同步（ResultSet索引从1开始），统一循环的起始索引为1
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        RsKit.bindLabelNamesAndTypes(rsmd, labelNames, types);

        while (rs.next()) {
            Record record = new Record();
            record.setColumnsMap(config.container.getColumnsMap());
            RsKit.fetch(rs, columnCount, labelNames, types, record.getColumns());
            list.add(record);
        }
        return list;
    }
}
