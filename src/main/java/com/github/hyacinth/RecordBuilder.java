package com.github.hyacinth;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 10:48
 */
public class RecordBuilder {

    public static final List<Record> build(Config config, ResultSet rs) throws SQLException {
        List<Record> result = new ArrayList<Record>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        while (rs.next()) {
            Record record = new Record();
            record.setColumnsMap(config.containerFactory.getColumnsMap());
            Map<String, Object> columns = record.getColumns();
            for (int i = 1; i <= columnCount; i++) {
                Object value;
                if (types[i] < Types.BLOB)
                    value = rs.getObject(i);
                else if (types[i] == Types.CLOB)
                    value = ModelBuilder.handleClob(rs.getClob(i));
                else if (types[i] == Types.NCLOB)
                    value = ModelBuilder.handleClob(rs.getNClob(i));
                else if (types[i] == Types.BLOB)
                    value = ModelBuilder.handleBlob(rs.getBlob(i));
                else
                    value = rs.getObject(i);

                columns.put(labelNames[i], value);
            }
            result.add(record);
        }
        return result;
    }

    private static final void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
        for (int i = 1; i < labelNames.length; i++) {
            labelNames[i] = rsmd.getColumnLabel(i);
            types[i] = rsmd.getColumnType(i);
        }
    }
}
