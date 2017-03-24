package com.github.hyacinth.sql.markdown;

import com.github.hyacinth.HyacinthException;
import com.github.hyacinth.sql.RawSqls;
import com.github.hyacinth.sql.SqlCache;
import com.github.hyacinth.sql.TemplateCompiler;
import com.github.hyacinth.cache.Cache;
import com.github.hyacinth.cache.PureCache;
import com.github.hyacinth.tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Markdown文件解析器
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/9
 * Time: 9:33
 */
public class MdResolve {

    private static final Logger LOGGER = LoggerFactory.getLogger(MdResolve.class);

    String lineSeparator = System.getProperty("line.separator", "\n");

    //Sql模板编译器
    private static TemplateCompiler templateCompiler;

    /**
     * 解析Markdown模板文件，将文件中的sql id以及对于sql逐条解析并缓存起来
     *
     * @param file Markdown file path
     */
    private void resolveRawSqls(File file) {
        //获取文件名称当做sql组名
        String group = file.getName().substring(0, file.getName().lastIndexOf(".")).trim();
        //用于缓存读取到的主体sql行和key行
        LinkedList<String> lineSqlList = new LinkedList<String>();
        //用于缓存读取到的注释行
        LinkedList<String> lineCommentList = new LinkedList<String>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            String tempSqlKey;
            while ((line = bufferedReader.readLine()) != null) {
                //===，--- 为sql分割符，读取到分割符之后，则获取一条完整sql
                if (line.startsWith("===") || line.startsWith("---")) {
                    //获取下一条sqlKey先存起来
                    tempSqlKey = lineSqlList.pollLast().trim();
                    //取出当前sqlKey
                    String key = lineSqlList.pollFirst();
                    buildRawSql(group, key, lineSqlList, lineCommentList);
                    //将下一条sqlKey再放入list
                    lineSqlList.addLast(tempSqlKey);
                } else {
                    //注释行
                    if (line.startsWith(">")) {
                        //缓存注释行
                        lineCommentList.addLast(line.substring(1).trim());
                    } else {
                        //缓存sql行、key行
                        lineSqlList.addLast(line);
                    }

                }
            }
            //处理最后一条SQL
            buildRawSql(group, lineSqlList.pollFirst().trim(), lineSqlList, lineCommentList);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 用于系统初始化，从Md文件中，解析出sql语句并放入rawSqls集合里
     *
     * @param files 文件集
     */
    public void resolve(List<File> files) {
        for (File file : files) {
            resolveRawSqls(file);
        }
        buildSql();
    }

    /**
     * 用于监听中文件变动后重新加载文件
     *
     * @param file 变更后的文件
     */
    public void resolve(File file) {
        resolveRawSqls(file);
        buildSql();
    }

    /**
     * 创建Sql
     * 1、加载子块
     * 2、放入缓存
     */
    private void buildSql() {
        //循环 取所有原Sql
        for (String key : SqlCache.rawSqls.asMap().keySet()) {
            StringBuilder sqlBuilder = SqlCache.rawSqls.get(key).getSql();
            //处理sql字块(对原Sql进行处理)
            int start, end = -1;
            while (true) {
                if ((start = sqlBuilder.indexOf("{{", ++end)) == -1) break;
                end = sqlBuilder.indexOf("}}");
                if (end < start) break;

                String refKey = StringTools.firstCharToUpperCase(sqlBuilder.substring(start + 2, end));
                StringBuilder subSqlBlock = SqlCache.rawSqls.get(refKey) == null
                        ? SqlCache.rawSqls.get("*" + refKey).getSql() : SqlCache.rawSqls.get(refKey).getSql();
                if (subSqlBlock == null) {
                    LOGGER.error("The key:{} is unknown at the {}", refKey, key);
                    throw new HyacinthException("The key is unknown");
                }
                sqlBuilder.replace(start, end + 2, subSqlBlock.toString());
            }

            String sql = sqlBuilder.toString().trim().replaceAll("\\s+", " ");

            //处理静态sql
            if (key.startsWith("*")) {
                SqlCache.fixed.put(key.replace("*", ""), sql);
            } else {
                //动态sql交给模板引擎
                templateCompiler.compile(key, sql);
            }
        }
    }

    /**
     * 获取原Sql
     *
     * @param group 组名
     * @param key   key
     * @param list  读取到的多行Sql集
     */
    private void buildRawSql(String group, String key, LinkedList<String> list, LinkedList<String> lineCommentList) {
        //如果key的格式是*xxx*则表示当前sql是静态sql
        if (key != null) {
            //缓存用的key
            String usefulKey;
            key = key.trim();
            group = StringTools.firstCharToUpperCase(group);
            //处理静态sql
            if (key.startsWith("*") && key.endsWith("*")) {
                usefulKey = new StringBuilder("*").append(group).append("_").append(key.replaceAll("\\*", "")).toString();
            } else {
                usefulKey = new StringBuilder(group).append("_").append(key).toString();
            }
            StringBuilder sqlBuilder = new StringBuilder();
            String comment = null;
            lineListToSql(list, sqlBuilder);
            //如果写有注释，则解析获取注释
            if (lineCommentList.size() > 0) {
                StringBuilder commentBuilder = new StringBuilder();
                lineListToComment(lineCommentList, commentBuilder);
                comment = commentBuilder.toString();
            }
            SqlCache.rawSqls.put(usefulKey, new RawSqls(group, key, sqlBuilder, comment));
        }
    }

    /**
     * 将从模板里读取到的多行sql拼接成整条
     *
     * @param list
     */
    private void lineListToSql(LinkedList<String> list, StringBuilder sqlBuilder) {
        while (!list.isEmpty()) {
            sqlBuilder.append(list.pollFirst()).append(lineSeparator);
        }
    }

    /**
     * 获取注释缓存list
     *
     * @return 注释
     */
    private String lineListToComment(LinkedList<String> lineCommentList, StringBuilder comment) {
        while (!lineCommentList.isEmpty()) {
            comment.append(lineCommentList.pollFirst()).append(lineSeparator);
        }
        return comment.toString();
    }

    /**
     * 设置Sql模板编译器
     *
     * @param templateCompiler
     */
    public static void setTemplateCompiler(TemplateCompiler templateCompiler) {
        MdResolve.templateCompiler = templateCompiler;
    }
}
