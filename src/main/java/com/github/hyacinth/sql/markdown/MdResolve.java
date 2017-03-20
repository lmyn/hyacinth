package com.github.hyacinth.sql.markdown;

import com.github.hyacinth.sql.Compile;
import com.github.hyacinth.sql.SqlCache;
import com.github.hyacinth.tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/9
 * Time: 9:33
 */
public class MdResolve {

    private static final Logger LOGGER = LoggerFactory.getLogger(MdResolve.class);

    protected String lineSeparator = System.getProperty("line.separator", "\n");

    private static Compile compile;

    /**
     * 解析Markdown模板文件，将文件中的sql id以及对于sql逐条解析并缓存起来
     *
     * @param file Markdown file path
     */
    public void resolve(File file) {
        //获取文件名称当做sql组名
        String group = file.getName().substring(0, file.getName().lastIndexOf("."));
        //用于存储文本文件中的sql
        LinkedList<String> list = new LinkedList<String>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            String tempSqlKey = null;
            while ((line = bufferedReader.readLine()) != null) {
                //===，--- 为sql分割符，读取到分割符之后，则获取一条完整sql
                if (line.startsWith("===") || line.startsWith("---")) {
                    //获取下一条sqlKey先存起来
                    tempSqlKey = list.pollLast().trim();
                    //取出当前sqlKey
                    String key = list.pollFirst();
                    make(group, key, list);
                    //将下一条sqlKey再放入list
                    list.addLast(tempSqlKey);
                } else {
                    //如果是注释行或空行 则忽略
                    if (!(line.trim().equals("") || line.startsWith(">"))) {
                        list.addLast(line);
                    }
                }
            }
            //处理最后一条SQL
            make(group, list.pollFirst(), list);
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

    private void make(String group, String key, LinkedList<String> list) {
        //如果key的格式是*xxx*则表示当前sql是静态sql
        if (key != null) {
            String usefulKey = new StringBuilder(StringTools.firstCharToUpperCase(group)).append("_").append(StringTools.firstCharToUpperCase(key.replace("*", ""))).toString();
            //处理静态sql
            if (key.startsWith("*")) {
                SqlCache.fixed.put(usefulKey, buildSql(list));
            } else {
                //动态sql交给模板引擎
                compile.make(usefulKey, buildSql(list));
            }
        }
    }

    /**
     * 将从模板里读取到的多行sql拼接成整条
     *
     * @param list
     * @return
     */
    private String buildSql(LinkedList<String> list) {
        StringBuilder sql = new StringBuilder();
        while (!list.isEmpty()) {
            String s = list.pollFirst();
            sql.append(s).append(lineSeparator);
        }

        return sql.toString();
    }

    public static void setCompile(Compile compile) {
        MdResolve.compile = compile;
    }
}
