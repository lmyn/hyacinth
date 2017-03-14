package com.github.hyacinth;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        bindLabelNamesAndTypes(rsmd, labelNames, types);

        if (rs.next()) {
            model = modelClass.newInstance();
            fetch(rs, columnCount, labelNames, types, model.getAttrs());
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
        bindLabelNamesAndTypes(rsmd, labelNames, types);

        while (rs.next()) {
            M model = modelClass.newInstance();
            fetch(rs, columnCount, labelNames, types, model.getAttrs());
            list.add(model);
        }
        return list;
    }

    /**
     * 从ResultSet获取数据
     *
     * @param rs          ResultSet
     * @param columnCount 列总数
     * @param labelNames  列名集合（别名）
     * @param types       列的数据类型
     * @param attrs       装载map
     */
    private static void fetch(ResultSet rs, int columnCount, String[] labelNames, int[] types, Map<String, Object> attrs) throws SQLException, IllegalAccessException, InstantiationException {
        for (int i = 1; i <= columnCount; i++) {
            if (types[i] < Types.BLOB) {
                attrs.put(labelNames[i], rs.getObject(i));
            } else if (types[i] == Types.CLOB) {
                attrs.put(labelNames[i], handleClob(rs.getClob(i)));
            } else if (types[i] == Types.NCLOB) {
                attrs.put(labelNames[i], handleClob(rs.getNClob(i)));
            } else if (types[i] == Types.BLOB) {
                attrs.put(labelNames[i], handleBlob(rs.getBlob(i)));
            }
        }
    }

    /**
     * 获取列名列表以及相应的数据类型
     *
     * @param rsmd       ResultSetMetaData
     * @param labelNames 列名数组
     * @param types      类型数组
     */
    private static final void bindLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
        for (int i = 1; i < labelNames.length; i++) {
            labelNames[i] = rsmd.getColumnLabel(i);
            types[i] = rsmd.getColumnType(i);
        }
    }

    /**
     * 处理二进制
     *
     * @param blob 二进制
     * @return byte数组
     */
    public static byte[] handleBlob(Blob blob) throws SQLException {
        if (blob == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = blob.getBinaryStream();
            if (is == null) {
                return null;
            }
            byte[] data = new byte[(int) blob.length()];        // byte[] data = new byte[is.available()];
            if (data.length == 0) {
                return null;
            }
            is.read(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    /**
     * 处理文本
     *
     * @param clob 文本
     * @return 文本字符串
     */
    public static String handleClob(Clob clob) throws SQLException {
        if (clob == null) {
            return null;
        }

        Reader reader = null;
        try {
            reader = clob.getCharacterStream();
            if (reader == null) {
                return null;
            }
            char[] buffer = new char[(int) clob.length()];
            if (buffer.length == 0) {
                return null;
            }
            reader.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}
