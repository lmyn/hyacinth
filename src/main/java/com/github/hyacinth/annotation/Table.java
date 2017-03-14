package com.github.hyacinth.annotation;

import java.lang.annotation.*;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/5
 * Time: 16:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface Table {

    //表名
    String name() default "";
}
