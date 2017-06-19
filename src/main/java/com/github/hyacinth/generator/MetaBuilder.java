package com.github.hyacinth.generator;

import com.github.hyacinth.dialect.Dialect;
import com.github.hyacinth.dialect.MysqlDialect;
import com.github.hyacinth.dialect.OracleDialect;
import com.github.hyacinth.tools.StringTools;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * MetaBuilder
 */
public class MetaBuilder {

    protected DataSource dataSource;
    protected Dialect dialect = new MysqlDialect();
    protected Set<String> excludedTables = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    protected Set<String> excludedTablePrefixes = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

    protected Connection conn = null;
    protected DatabaseMetaData dbMeta = null;

    protected String[] removedTableNamePrefixes = null;

    protected TypeMapping typeMapping = new TypeMapping();

    public MetaBuilder(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null.");
        }
        this.dataSource = dataSource;
    }

    public void setDialect(Dialect dialect) {
        if (dialect != null) {
            this.dialect = dialect;
        }
    }

    public void addExcludedTable(String... excludedTables) {
        if (excludedTables != null) {
            for (String table : excludedTables) {
                this.excludedTables.add(table);
            }
        }
    }

    public void addExcludedTablePrefixes(String... excludedTablePrefixes) {
        if (excludedTablePrefixes != null) {
            for (String tablePrefix : excludedTablePrefixes) {
                this.excludedTablePrefixes.add(tablePrefix);
            }
        }
    }

    /**
     * 设置需要被移除的表名前缀，仅用于生成 modelName 与  baseModelName
     * 例如表名  "osc_account"，移除前缀 "osc_" 后变为 "account"
     */
    public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
        this.removedTableNamePrefixes = removedTableNamePrefixes;
    }

    public void setTypeMapping(TypeMapping typeMapping) {
        if (typeMapping != null) {
            this.typeMapping = typeMapping;
        }
    }

    public List<TableMeta> build() {
        System.out.println("Build TableMeta ...");
        try {
            conn = dataSource.getConnection();
            dbMeta = conn.getMetaData();

            List<TableMeta> ret = new ArrayList<TableMeta>();
            buildTableNames(ret);
            for (TableMeta tableMeta : ret) {
                buildPrimaryKey(tableMeta);
                buildColumnMetas(tableMeta);
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 通过继承并覆盖此方法，跳过一些不希望处理的 table，定制更加灵活的 table 过滤规则
     *
     * @return 返回 true 时将跳过当前 tableName 的处理
     */
    protected boolean isSkipTable(String tableName) {
        return false;
    }

    /**
     * 构造 modelName，mysql 的 tableName 建议使用小写字母，多单词表名使用下划线分隔，不建议使用驼峰命名
     * oracle 之下的 tableName 建议使用下划线分隔多单词名，无论 mysql还是 oralce，tableName 都不建议使用驼峰命名
     */
    protected String buildModelName(String tableName) {
        // 移除表名前缀仅用于生成 modelName、baseModelName，而 tableMeta.name 表名自身不能受影响
        if (removedTableNamePrefixes != null) {
            for (String prefix : removedTableNamePrefixes) {
                if (tableName.startsWith(prefix)) {
                    tableName = tableName.replaceFirst(prefix, "");
                    break;
                }
            }
        }

        // 将 oralce 大写的 tableName 转成小写，再生成 modelName
        if (dialect instanceof OracleDialect) {
            tableName = tableName.toLowerCase();
        }

        return StringTools.firstCharToUpperCase(StringTools.toCamelCase(tableName));
    }

    /**
     * 使用 modelName 构建 baseModelName
     */
    protected String buildBaseModelName(String modelName) {
        return "Base" + modelName;
    }

    /**
     * 不同数据库 dbMeta.getTables(...) 的 schemaPattern 参数意义不同
     * 1：oracle 数据库这个参数代表 dbMeta.getUserName()
     * 2：postgresql 数据库中需要在 jdbcUrl中配置 schemaPatter，例如：
     * jdbc:postgresql://localhost:15432/djpt?currentSchema=public,sys,app
     * 最后的参数就是搜索schema的顺序，DruidPlugin 下测试成功
     * 3：开发者若在其它库中发现工作不正常，可通过继承 MetaBuilder并覆盖此方法来实现功能
     */
    protected ResultSet getTablesResultSet() throws SQLException {
        String schemaPattern = dialect instanceof OracleDialect ? dbMeta.getUserName() : null;
//        return dbMeta.getTables(conn.getCatalog(), schemaPattern, null, new String[]{"TABLE", "VIEW"});
        return dbMeta.getTables(conn.getCatalog(), schemaPattern, null, new String[]{"TABLE"});
    }

    protected void buildTableNames(List<TableMeta> ret) throws SQLException {
        ResultSet rs = getTablesResultSet();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");

            if(!tableNameVerification(tableName)) {
                continue;
            }
            if (isSkipTable(tableName)) {
                System.out.println("Skip table :" + tableName);
                continue;
            }

            TableMeta tableMeta = new TableMeta();
            tableMeta.name = tableName;
            tableMeta.remarks = rs.getString("REMARKS");

            tableMeta.modelName = buildModelName(tableName);
            tableMeta.baseModelName = buildBaseModelName(tableMeta.modelName);
            ret.add(tableMeta);
        }
        rs.close();
    }

    protected boolean tableNameVerification(String tableName){
        if (excludedTables.contains(tableName)) {
            System.out.println("Skip table :" + tableName);
            return false;
        }
        if(excludedTablePrefixes != null && !excludedTablePrefixes.isEmpty()){
            for (String prefix : excludedTablePrefixes) {
                if(tableName.startsWith(prefix)){
                    System.out.println("Skip table :" + tableName);
                    return false;
                }
            }
        }

        return true;
    }

    protected void buildPrimaryKey(TableMeta tableMeta) throws SQLException {
        ResultSet rs = dbMeta.getPrimaryKeys(conn.getCatalog(), null, tableMeta.name);

        String primaryKey = "";
        int index = 0;
        while (rs.next()) {
            if (index++ > 0) {
                primaryKey += ",";
            }
            primaryKey += rs.getString("COLUMN_NAME");
        }
        if (StringTools.isBlank(primaryKey)) {
            throw new RuntimeException("PrimaryKey required by active record pattern; table:" + tableMeta.name);
        }
        tableMeta.primaryKey = primaryKey;
        rs.close();
    }

    /**
     * 文档参考：
     * http://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
     * <p>
     * JDBC 与时间有关类型转换规则，mysql 类型到 java 类型如下对应关系：
     * DATE				java.sql.Date
     * DATETIME			java.sql.Timestamp
     * TIMESTAMP[(M)]	java.sql.Timestamp
     * TIME				java.sql.Time
     * <p>
     * 对数据库的 DATE、DATETIME、TIMESTAMP、TIME 四种类型注入 new java.util.Date()对象保存到库以后可以达到“秒精度”
     * 为了便捷性，getter、setter 方法中对上述四种字段类型采用 java.util.Date，可通过定制 TypeMapping 改变此映射规则
     */
    protected void buildColumnMetas(TableMeta tableMeta) throws SQLException {
        String sql = dialect.forTableBuilderDoBuild(tableMeta.name);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();

        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            ColumnMeta cm = new ColumnMeta();
            cm.name = rsmd.getColumnName(i);

            String colClassName = rsmd.getColumnClassName(i);
            String typeStr = typeMapping.getType(colClassName);
            if (typeStr != null) {
                cm.javaType = typeStr;
            } else {
                int type = rsmd.getColumnType(i);
                if (type == Types.BINARY || type == Types.VARBINARY || type == Types.BLOB) {
                    cm.javaType = "byte[]";
                } else if (type == Types.CLOB || type == Types.NCLOB) {
                    cm.javaType = "java.lang.String";
                } else {
                    cm.javaType = "java.lang.String";
                }
            }

            // 构造字段对应的属性名 attrName
            cm.attrName = buildAttrName(cm.name);

            tableMeta.columnMetas.add(cm);
        }

        rs.close();
        stm.close();
    }

    /**
     * @param tableMetas
     */
    protected void buildColumnMetasDetail(List<TableMeta> tableMetas) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData dbMeta = conn.getMetaData();
            for (TableMeta tableMeta : tableMetas) {
                // 重建整个 TableMeta.columnMetas
                // 通过查看 dbMeta.getColumns(...) 源码注释，还可以获取到更多 meta data
                ResultSet rs = dbMeta.getColumns(conn.getCatalog(), null, tableMeta.name, null);
                while (rs.next()) {
                    ColumnMeta columnMeta = null;
                    String name = rs.getString("COLUMN_NAME");            // 名称
                    for (ColumnMeta meta : tableMeta.columnMetas) {
                        if (!StringTools.isBlank(name) && name.equals(meta.name)) {
                            columnMeta = meta;
                        }
                    }
                    if (columnMeta == null) {
                        continue;
                    }
                    columnMeta.type = rs.getString("TYPE_NAME");            // 类型
                    if (columnMeta.type == null) {
                        columnMeta.type = "";
                    }

                    int columnSize = rs.getInt("COLUMN_SIZE");                // 长度
                    if (columnSize > 0) {
                        columnMeta.type = columnMeta.type + "(" + columnSize;
                        int decimalDigits = rs.getInt("DECIMAL_DIGITS");    // 小数位数
                        if (decimalDigits > 0) {
                            columnMeta.type = columnMeta.type + "," + decimalDigits;
                        }
                        columnMeta.type = columnMeta.type + ")";
                    }

                    columnMeta.isNullable = rs.getString("IS_NULLABLE");    // 是否允许 NULL 值
//					if (columnMeta.isNullable == null) {
//						columnMeta.isNullable = "";
//					}
                    if ("YES".equals(columnMeta.isNullable)) {
                        columnMeta.isNullable = "TRUE";
                    } else if ("NO".equals(columnMeta.isNullable)) {
                        columnMeta.isNullable = "FALSE";
                    }

                    columnMeta.isPrimaryKey = "FALSE";
                    String[] keys = tableMeta.primaryKey.split(",");
                    for (String key : keys) {
                        if (key.equalsIgnoreCase(columnMeta.name)) {
                            columnMeta.isPrimaryKey = "TRUE";
                            break;
                        }
                    }

                    columnMeta.defaultValue = rs.getString("COLUMN_DEF");    // 默认值
                    if (columnMeta.defaultValue == null) {
                        columnMeta.defaultValue = "null";
                    }

                    columnMeta.remarks = rs.getString("REMARKS");            // 备注
                    if (columnMeta.remarks == null) {
                        columnMeta.remarks = "";
                    }

                    if (tableMeta.colNameMaxLen < columnMeta.name.length()) {
                        tableMeta.colNameMaxLen = columnMeta.name.length();
                    }
                    if (tableMeta.colTypeMaxLen < columnMeta.type.length()) {
                        tableMeta.colTypeMaxLen = columnMeta.type.length();
                    }
                    if (tableMeta.colDefaultValueMaxLen < columnMeta.defaultValue.length()) {
                        tableMeta.colDefaultValueMaxLen = columnMeta.defaultValue.length();
                    }

                }
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 构造 colName 所对应的 attrName，mysql 数据库建议使用小写字段名或者驼峰字段名
     * Oralce 反射将得到大写字段名，所以不建议使用驼峰命名，建议使用下划线分隔单词命名法
     */
    protected String buildAttrName(String colName) {
        if (dialect instanceof OracleDialect) {
            colName = colName.toLowerCase();
        }
        return StringTools.toCamelCase(colName);
    }
}







