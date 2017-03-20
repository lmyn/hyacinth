package com.github.hyacinth.sql;

import java.util.List;
import java.util.Map;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/10
 * Time: 17:30
 */
public interface SqlBuilder {

    /**
     * sql渲染方法
     *
     * @param key       缓存中的key name
     * @param paras     用于渲染的参数集
     * @param parasList 参数化列表，原模板中的参数表达式 将采用 ? 代替
     * @return
     */
    String build(String key, Map<String, Object> paras, List<Object> parasList);

}
