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

    /**
     * 从Model类中获取Model与Table的映射
     *
     * @param modelSubList 类集合
     */
    public void build(List<Class<? extends Model<?>>> modelSubList, Config config) {
        List<com.github.hyacinth.Table> tableList = new ArrayList<com.github.hyacinth.Table>();
        for (Class<? extends Model<?>> subClass : modelSubList) {
            Class<? extends Model<?>> superClass = (Class<? extends Model<?>>) subClass.getSuperclass();
            //获取数据库表名
            Table tableClass = superClass.getAnnotation(Table.class);
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
            Method[] methods = superClass.getMethods();
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

}
