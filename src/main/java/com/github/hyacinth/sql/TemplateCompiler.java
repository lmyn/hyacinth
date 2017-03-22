package com.github.hyacinth.sql;

/**
 * 模板编译器
 *
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/20
 * Time: 10:31
 */
public interface TemplateCompiler {

    void compile(String key, String template);
}
