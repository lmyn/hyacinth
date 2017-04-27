package com.github.hyacinth.sql;

import com.github.hyacinth.tools.StringTools;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import jetbrick.template.resolver.GlobalResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * jetbrick-template编译器
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/7/30
 * Time: 14:39
 */
public class DefaultCompiler implements TemplateCompiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCompiler.class);

    //模板引擎
    private static JetEngine jetEngine = null;

    /**
     * 编译模板 （有缓存，并且在有相同name的时候会覆盖）
     *
     * @param name     模板缓存标识
     * @param template 模板源码
     */
    @Override
    public void compile(String name, String template) {
        //获取引擎对象
        JetEngine jetEngine = getJetEngine();
        //编译模板
        JetTemplate jetTemplate = jetEngine.createTemplate(name, template);
        //将编译后的模板放入缓存
        SqlCache.jetbrickTemplate.put(name, jetTemplate);
//        LOGGER.debug("Compiled sql sql:{} --> \n sql sql: {}", name, template);
    }

    /**
     * 默认配置创建
     */
    private static JetEngine getJetEngine() {
        if (jetEngine == null) {
//            Properties config = new Properties();
//            config.setProperty("jetx.import.classes", "com.github.hyacinth.tools.StringTools");
            jetEngine = JetEngine.create();
            GlobalResolver resolver = jetEngine.getGlobalResolver();
            resolver.registerFunctions(StringTools.class);
        }
        return jetEngine;
    }
}
