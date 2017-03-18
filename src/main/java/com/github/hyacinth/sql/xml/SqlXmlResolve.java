package com.github.hyacinth.sql.xml;

import com.github.hyacinth.sql.jetx.JetbrickTemplateCompiler;
import com.github.hyacinth.sql.SqlCache;
import com.github.hyacinth.sql.xml.structure.SqlGroup;
import com.github.hyacinth.sql.xml.structure.SqlItem;
import com.github.hyacinth.tools.JaxbTools;
import com.github.hyacinth.tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析sql xml
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/7/26
 * Time: 14:28
 */
public class SqlXmlResolve {

    private static Logger logger = LoggerFactory.getLogger(SqlXmlResolve.class);

    private List<String> keys = new ArrayList<String>();

    /**
     * 解析从指定文件夹下文件名符合要求的文件
     *
     * @param dir          dir
     * @param fileWildcard wildcard
     */
    public void read(String dir, final String fileWildcard) {
        File folder = new File(dir);
        //获取到所有sqlxml文件
        File[] files = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return StringTools.wildcardMatch(fileWildcard, file.getName());
            }
        });
        if (files == null) {
            logger.error("SQL sql file not found!");
            return;
        }
        for (File file : files) {
            read(file, false);
        }
        keys.clear();
    }

    /**
     * 解析文件
     *
     * @param file xml file
     */
    public void read(File file, boolean needClear) {
        SqlGroup group = JaxbTools.unmarshal(file, SqlGroup.class);
        resolve(group);
        if (needClear) keys.clear();
    }

    /**
     * 解析源码
     *
     * @param sqlXmlSource
     */
    public void read(String sqlXmlSource, boolean needClear) {
        SqlGroup group = JaxbTools.unmarshal(sqlXmlSource, SqlGroup.class);
        resolve(group);
        if (needClear) keys.clear();
    }

    /**
     * 解析
     *
     * @param group
     */
    private void resolve(SqlGroup group) {
        if (group == null) {
            logger.error("Xml parsing failed.");
            return;
        }
        //获取组名
        String name = group.name;
        //获取数据库类型
        String dbType = group.dbType;
        if (StringTools.isBlank(name)) {
            logger.error("[{}] The group name is not defined.", name);
            return;
        }
        if (keys.contains(name)) {
            logger.error("[{}] This group already exists.", name);
            return;
        }
        keys.add(name);
        logger.debug("added group:{}", name);
        for (SqlItem sqlItem : group.sqlItems) {
            String sqlKey = name + "." + sqlItem.id;
            if (keys.contains(sqlKey)) {
                logger.error("[{}] This sql already exists.", sqlKey);
                return;
            }
            String sql = sqlItem.value;
            if (isStableSql(sqlItem.stable, sqlKey, sql)) return;
            JetbrickTemplateCompiler.compile(sqlKey, sql);
            keys.add(sqlKey);
            logger.debug("added sqlKey:{}", sqlKey);
        }
    }

    /**
     * 静态sql处理
     *
     * @param stable 是否静态
     * @param sqlKey sqlkey
     * @param sql    sql
     * @return bool
     */
    private boolean isStableSql(String stable, String sqlKey, String sql) {
        //处理静态sql
        if ("true".equals(stable) || (StringTools.isBlank(stable) && !sql.contains("#{") && !sql.contains("${") && !sql.contains("@{"))) {
            SqlCache.fixed.put(sqlKey, sql);
            return true;
        }
        return false;
    }

}
