package com.github.hyacinth.sql.jetx;

import com.github.hyacinth.sql.SqlParameterizeRender;
import com.github.hyacinth.sql.SqlCache;
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
public class JetxRender {

    private static Logger LOGGER = LoggerFactory.getLogger(JetxRender.class);

    /**
     * 普通SQL
     *
     * @param key    标识
     * @param params 参数
     * @return SqlParams
     */
    public static String render(String key, Map<String, Object> params, List<Object> paramsList) {
        //模板引擎渲染
        StringBuilder sql = new StringBuilder(jetEngineRender(key, params));
        //二次渲染
        SqlParameterizeRender.render(params, sql, paramsList);

        return sql.toString();
    }

    /**
     * 引擎渲染
     *
     * @param key    模板标识
     * @param params 渲染参数
     * @return 引擎渲染结果
     */
    private static String jetEngineRender(String key, Map<String, Object> params) {
        String result;
        //获取模板缓存
        JetTemplate jetTemplate = SqlCache.templateCache.get(key);

        StringWriter stringWriter = new StringWriter();
        if (jetTemplate != null) {
            jetTemplate.render(params, stringWriter);
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
