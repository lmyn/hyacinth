package com.github.hyacinth.sql;

import java.util.List;
import java.util.Map;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/10
 * Time: 17:30
 */
public interface Render {

    void render(String key, Map<String, Object> params, StringBuilder sql, List<Object> paramsList);

}
