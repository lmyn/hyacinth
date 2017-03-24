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

    //缓存从Markdown文件中获取的Sql,用于热加载中，便于对Sql子块进行处理
    public static final Cache<String, RawSqls> rawSqls = new PureCache<String, RawSqls>("rawSqls");

    //静态sql缓存
    public static final Cache<String, String> fixed = new PureCache<String, String>("fixed");

    //编译后模板缓存
    static final Cache<String, JetTemplate> jetbrickTemplate = new PureCache<String, JetTemplate>("jetbrick-template");

}
