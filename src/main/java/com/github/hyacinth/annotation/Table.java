package com.github.hyacinth.annotation;

import java.lang.annotation.*;

/**
 * 修饰Model类本身，提供Model与数据库表的映射关系
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/5
 * Time: 16:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Table {

    //表名
    String name() default "";
}
