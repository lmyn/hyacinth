package com.github.hyacinth.sql;

import com.github.hyacinth.tools.StringTools;

import java.util.List;
import java.util.Map;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/13
 * Time: 10:43
 */
public class SqlParameterizeRender {

    /**
     * 模板参数化处理
     *
     * @param paras
     * @param sql
     * @param parasList
     */
    public static void render(Map<String, Object> paras, StringBuilder sql, List<Object> parasList) {
        int start, end = -1; //#index
        while (true) {
            //获取动态参数‘{’，‘}’索引位置
            start = sql.indexOf("{", ++end);
            end = sql.indexOf("}", start + 1);

            if (start == -1 || end == -1) break;
            //下一个动态参数扫描的起始位置

            //获取参数变量值
            String keyStr = sql.substring(start + 1, end).trim();
            Object value = getParamsValue(keyStr, paras);

            //参数值处理
            char sign = sql.charAt(start - 1);
            if (sign == '#') {
                parasList.add(value);
                //将参数化表达式替换成sql参数占位符
                sql.replace(start - 1, end + 1, "?");
            } else if (sign == '@') {
                StringBuilder multipleBuilder = new StringBuilder();
                //处理数组
                if (value != null && value.getClass().isArray()) {
                    Object[] objArray = (Object[]) value;
                    for (int i = 0; i < objArray.length; i++) {
                        parasList.add(objArray[i]);
                        multipleBuilder.append("?,");
                    }
                }
                if (multipleBuilder.length() > 0) {
                    multipleBuilder.deleteCharAt(multipleBuilder.length() - 1);
                }
                sql.replace(start - 1, end, multipleBuilder.toString());
            } else {

            }
        }
    }

    /**
     * 根据key从参数map集合中获取对应的值
     *
     * @param key
     * @param params
     * @return
     */
    private static Object getParamsValue(String key, Map<String, Object> params) {
        Object value = params.get(key);
        if (value == null) {
            String[] keys = StringTools.split(key, '.');
            int keysIndexs = keys.length - 1;

            if (keysIndexs == 0) return null;
            Map temp = params;
            for (int i = 0; i < keys.length; i++) {
                if (keysIndexs != i) {
                    temp = (Map) temp.get(keys[i]);
                    if (temp == null) break;
                } else {
                    value = temp.get(keys[i]);
                }
            }
        }
        return value;
    }

}
