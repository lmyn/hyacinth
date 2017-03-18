package com.github.hyacinth;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.Map;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/17
 * Time: 17:16
 */
public class RsKit {
    /**
     * 从ResultSet获取数据
     *
     * @param rs          ResultSet
     * @param columnCount 列总数
     * @param labelNames  列名集合（别名）
     * @param types       列的数据类型
     * @param attrs       装载map
     */
    static void fetch(ResultSet rs, int columnCount, String[] labelNames, int[] types, Map<String, Object> attrs) throws SQLException {
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
    static final void bindLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
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
