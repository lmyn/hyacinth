package com.github.hyacinth.sql.md;

import com.github.hyacinth.sql.jetx.JetbrickTemplateCompiler;
import com.github.hyacinth.sql.SqlCache;
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

    /**
     * 解析Markdown模板文件，将文件中的sql id以及对于sql逐条解析并缓存起来
     *
     * @param file Markdown file path
     */
    public void resolve(File file) {
        //获取文件名称当做sql组名
        String group = file.getName().substring(0, file.getName().lastIndexOf(".") + 1);
        //用于存储文本文件中的sql
        LinkedList<String> list = new LinkedList<String>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            String tempSqlKey;
            while ((line = bufferedReader.readLine()) != null) {
                //===，--- 为sql分割符，读取到分割符之后，则获取一条完整sql
                if (line.startsWith("===") || line.startsWith("---")) {
                    //获取下一条sqlKey先存起来
                    tempSqlKey = list.pollLast().trim();
                    //取出当前sqlKey
                    String key = list.pollFirst();
                    //如果key的格式是*xxx*则表示当前sql是静态sql
                    if (key != null) {
                        //处理静态sql
                        if (key.startsWith("*")) {
                            key = key.substring(1, key.length() - 1).trim();
                            SqlCache.sql.put(group + key, buildSql(list));
                        } else {
                            //动态sql交给模板引擎
                            JetbrickTemplateCompiler.compile(group + key, buildSql(list));
                        }
                    }
                    //将下一条sqlKey再放入list
                    list.addLast(tempSqlKey);
                } else {
                    //如果是注释行或空行 则忽略
                    if (!(line.trim().equals("") || line.startsWith(">"))) {
                        list.addLast(line);
                    }
                }
            }
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

}
