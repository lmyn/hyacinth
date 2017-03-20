package com.github.hyacinth.sql;

import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编译模板
 *
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/7/30
 * Time: 14:39
 */
public class DefaultCompiler implements Compile{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCompiler.class);

    //模板引擎
    private static JetEngine jetEngine = null;

    /**
     * 编译模板 （有缓存，并且在有相同name的时候会覆盖）
     * @param name      模板缓存标识
     * @param template  模板源码
     */
    @Override
    public void make(String name, String template) {
        //获取引擎对象
        JetEngine jetEngine = getJetEngine();
        //编译模板
        JetTemplate jetTemplate = jetEngine.createTemplate(name, template);
        //将编译后的模板放入缓存
        SqlCache.sqlTemplate.put(name, jetTemplate);
//        LOGGER.debug("Compiled sql sql:{} --> \n sql sql: {}", name, template);
    }

    /**
     * 默认配置创建
     */
    private static JetEngine getJetEngine() {
        if(jetEngine == null){
            jetEngine = JetEngine.create();
        }
        return jetEngine;
    }
}
