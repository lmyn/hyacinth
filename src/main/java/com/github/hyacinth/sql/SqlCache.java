package com.github.hyacinth.sql;

import com.github.hyacinth.cache.Cache;
import com.github.hyacinth.cache.PureCache;
import jetbrick.template.JetTemplate;

/**
 * 缓存编译后的sqlTemplate
 *
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/7/30
 * Time: 11:22
 */
public class SqlCache {

    //静态sql缓存
    public static final Cache<String, String> fixed = new PureCache<String, String>("fixed");

    //编译后模板缓存
    static final Cache<String, JetTemplate> jetbrickTemplate = new PureCache<String, JetTemplate>("jetbrick-template");

}
