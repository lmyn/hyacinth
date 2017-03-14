package com.github.hyacinth.annotation;

import java.lang.annotation.*;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/5
 * Time: 17:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface PrimaryKey {
}
