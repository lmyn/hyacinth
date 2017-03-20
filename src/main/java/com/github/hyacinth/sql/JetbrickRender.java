package com.github.hyacinth.sql;

import jetbrick.template.JetTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/12/29
 * Time: 22:18
 */
public class JetbrickRender implements Render {

    private static Logger LOGGER = LoggerFactory.getLogger(JetbrickRender.class);

    /**
     * 普通SQL
     *
     * @param key    标识
     * @param paras 参数
     * @return SqlParams
     */
    @Override
    public String make(String key, Map<String, Object> paras, List<Object> parasList) {
        //模板引擎渲染
        StringBuilder sql = new StringBuilder(jetEngineRender(key, paras));
        //二次渲染
        SqlBuilder.parameterizedRender(paras, sql, parasList);

        return sql.toString();
    }

    /**
     * 引擎渲染
     *
     * @param key    模板标识
     * @param paras 渲染参数
     * @return 引擎渲染结果
     */
    private String jetEngineRender(String key, Map<String, Object> paras) {
        String result;
        //获取模板缓存
        JetTemplate jetTemplate = SqlCache.sqlTemplate.get(key);

        StringWriter stringWriter = new StringWriter();
        if (jetTemplate != null) {
            jetTemplate.render(paras, stringWriter);
        }
        result = stringWriter.toString();
        try {
            stringWriter.close();
        } catch (IOException e) {
            LOGGER.error("Can not close stringWriter! SQL sql rendering failed.", e);
        }
        return result;
    }

}
