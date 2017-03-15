package com.github.hyacinth;

import com.github.hyacinth.annotation.PrimaryKey;
import com.github.hyacinth.annotation.Table;
import com.github.hyacinth.annotation.Column;
import com.github.hyacinth.tools.ClassTools;
import com.github.hyacinth.tools.PathTools;
import com.github.hyacinth.tools.StringTools;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/5
 * Time: 16:30
 */
public class TableBuilder {

    private JavaType javaType = new JavaType();

    public void build(String basePackage, Config config) throws IOException {
        buildTable(getBaseModelClass(basePackage), config);
    }

    /**
     * 从Model类中获取Model与Table的映射
     *
     * @param modelSubList 类集合
     */
    private void buildTable(List<Class<? extends Model<?>>> modelSubList, Config config) {
        List<com.github.hyacinth.Table> tableList = new ArrayList<com.github.hyacinth.Table>();
        for (Class<? extends Model<?>> subClass : modelSubList) {
            //获取数据库表名
            Table tableClass = subClass.getAnnotation(Table.class);
            if(tableClass == null){
                continue;
            }
            String tableName = tableClass.name();
            com.github.hyacinth.Table table = new com.github.hyacinth.Table(tableName, subClass);
            List<String> primaryKeyList = new ArrayList<String>();
            tableList.add(table);
            TableMapping.me().addMapping(table);
            DbKit.addModelToConfigMapping(subClass, config);

            //FIXME 获取列信息class.getMethods 获取当前类的所有的方法
            Method[] methods = subClass.getMethods();
            //获取方法的返回值
            for (Method method : methods) {
                //FIXME 只读取get方法上的注释信息 *
                String methodName = method.getName();
                if (methodName.startsWith("get")) {
                    String field = StringTools.firstCharLowerCase(methodName.substring(3));
                    //返回值类型
                    Class<?> javaType = method.getReturnType();

                    Column columnAnno = method.getAnnotation(Column.class);
                    if(columnAnno == null){
                        continue;
                    }
                    //列名
                    String columnName = columnAnno.name();
                    //数据库类型
                    String sqlType = columnAnno.type();
                    //是否为空
                    boolean nullAble = columnAnno.nullAble();
                    //注释
                    String comment = columnAnno.comment();

                    com.github.hyacinth.Column column =
                            new com.github.hyacinth.Column(columnName, sqlType, nullAble, comment);
                    /* 获取主键 */
                    // FIXME 如果有PrimaryKey注释，则表示为主键
                    PrimaryKey primaryKey = method.getAnnotation(PrimaryKey.class);
                    if (primaryKey != null) {
                        primaryKeyList.add(columnName);
                    }
                    table.setColumn(field, column);
                    table.setColumnType(columnName, javaType);
                }
            }
            String[] primaryKeyArr = new String[primaryKeyList.size()];
            primaryKeyList.toArray(primaryKeyArr);
            table.setPrimaryKey(primaryKeyArr);
        }
    }

    /**
     * 获取modelclass
     *
     * @param basePackage
     * @return
     * @throws IOException
     */
    private List<Class<? extends Model<?>>> getBaseModelClass(String basePackage) throws IOException {
        List<Class<? extends Model<?>>> classes = new ArrayList<Class<? extends Model<?>>>();

        String classPattern = "classpath*:" + basePackage.replace(".", "/") + "/*.class";
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(classPattern);

        //获取类路径
        for (Resource resource : resources) {
            URL url = resource.getURL();
            String protocol = url.getProtocol(),
                    className = null;
            if (protocol.equals("file")) {
                //获取classpath路径
                String rootClassPath = PathTools.getRootClassPath(),
                        classFilePath = resource.getFile().getAbsolutePath();
                if (!classFilePath.contains(rootClassPath)){
                    //处理测试路径
                    rootClassPath = rootClassPath.replace("test-classes", "classes");
                }
                className = classFilePath.substring(0, classFilePath.length() - 6).replace(rootClassPath + File.separator, "").replace(File.separator, ".");
            } else if (protocol.equals("jar")) {
                String classUrlPath = url.getPath();
                //从jar url中截图类名
                className = classUrlPath.substring(classUrlPath.indexOf(".jar!/") + 6, classUrlPath.length() - 6).replace("/", ".");
            }
            if(className != null && !className.contains("$")){
                try {
                    Class<?> clazz = Class.forName(className);
                    if(Model.class.isAssignableFrom(clazz)){
                        classes.add((Class<? extends Model<?>>) clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

    public void build(List<com.github.hyacinth.Table> tableList, Config config) {
        com.github.hyacinth.Table temp = null;
        Connection conn = null;
        try {
            conn = config.dataSource.getConnection();
            TableMapping tableMapping = TableMapping.me();
            for (com.github.hyacinth.Table table : tableList) {
                temp = table;
                doBuild(table, conn, config);
                tableMapping.equals(table);
                DbKit.addModelToConfigMapping(table.getModelClass(), config);
            }
        } catch (Exception e) {
            if (temp != null) {
                System.err.println("Can not create Table object, maybe the table " + temp.getName() + " is not exists.");
            }
            throw new HyacinthException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * 获取Model类的所有子类
     *
     * @param packages 查找包
     * @return 子类class集合
     */
    private List<Class<? extends Model<?>>> getModelClass(List<String> packages, boolean isRecursion) {
        //需要扫描的类集合
        Set<String> classNameSet = null;
        //如果集合为空，则扫描classpath下所有类
        if (packages == null || packages.isEmpty()) {
            classNameSet = ClassTools.getClassName();
        } else {
            classNameSet = new HashSet<String>();
            for (String packageName : packages) {
                classNameSet.addAll(ClassTools.getClassName(packageName, isRecursion));
            }
        }
        List<Class<? extends Model<?>>> classList = new ArrayList<Class<? extends Model<?>>>();
        Class<?> modelClass = Model.class;
        for (String className : classNameSet) {
            try {
                Class<?> clazz = Class.forName(className);
                //FIXME:isAssignableFrom 判断一个类是否为另一个类的子类
                if (modelClass.isAssignableFrom(clazz)) {
                    classList.add((Class<? extends Model<?>>) clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classList;
    }

    private void doBuild(com.github.hyacinth.Table table, Connection conn, Config config) throws SQLException {
        table.setColumnTypeMap(config.containerFactory.getAttrsMap());
        if (table.getPrimaryKey() == null) {
            table.setPrimaryKey(config.dialect.getDefaultPrimaryKey());
        }

        String sql = config.dialect.forTableBuilderDoBuild(table.getName());
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();

        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String colName = rsmd.getColumnName(i);
            String colClassName = rsmd.getColumnClassName(i);

            Class<?> clazz = javaType.getType(colClassName);
            if (clazz != null) {
                table.setColumnType(colName, clazz);
            } else {
                int type = rsmd.getColumnType(i);
                if (type == Types.BINARY || type == Types.VARBINARY || type == Types.BLOB) {
                    table.setColumnType(colName, byte[].class);
                } else if (type == Types.CLOB || type == Types.NCLOB) {
                    table.setColumnType(colName, String.class);
                } else {
                    table.setColumnType(colName, String.class);
                }
            }
        }

        rs.close();
        stm.close();
    }
}
