package com.github.hyacinth.annotation;

import java.lang.annotation.*;

/**
 * 修饰Model属性，提供属性与数据库字段的映射关系
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/5
 * Time: 17:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Column {
    //列名
    String name() default "";

    //列类型
    String type();

    //是否允许为空
    boolean nullAble() default true;

    //列注释
    String comment() default "";

    //默认值
    String defaultValue();

}
