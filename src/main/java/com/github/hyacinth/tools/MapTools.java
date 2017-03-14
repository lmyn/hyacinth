package com.github.hyacinth.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/8/4
 * Time: 2:21
 */
public class MapTools {

    private static Logger LOGGER = LoggerFactory.getLogger(MapTools.class);

    /**
     * java Bean 转 Map
     *
     * @param bean
     * @return
     */
    public static Map convert(Object bean) {
        Class type = bean.getClass();
        Map map = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            //获取类所有属性
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                //获取get方法
                Method readMethod = descriptor.getReadMethod();
                map.put(propertyName, readMethod.invoke(bean));
            }
        } catch (InvocationTargetException e) {
            LOGGER.error("convert Error!", e);
        } catch (IntrospectionException e) {
            LOGGER.error("convert Error!", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("convert Error!", e);
        }
        return map;
    }

    /**
     * Map<String, String[]> 转 Map<String, Object>
     *
     * @param map
     * @return
     */
    public static Map<String, Object> convert(Map<String, String[]> map) {
        if (map == null) return null;

        Set<String> keys = map.keySet();
        for (String key : keys) {

        }
        return null;
    }

    /**
     * 合并多个Map,如果有相同的Key则后面的会覆盖前面的
     *
     * @param maps maps
     * @param <K>  K
     * @param <V>  V
     * @return one Map
     */
    public static <K, V> Map<K, V> merge(Map<K, V>... maps) {
        Map<K, V> result = null;
        for (Map<K, V> map : maps) {
            if (result == null) {
                result = map;
            } else {
                result.putAll(map);
            }

        }
        return result;
    }

    /**
     * 合并多个Map, 将多个Map合并到一个Map<String, Map<String, Object>>
     *
     * @param keys keys
     * @param maps maps
     * @return one map
     */
    public static Map<String, Map<Object, Object>> join(String[] keys, Map<Object, Object>... maps) {
        if (keys == null || maps == null || keys.length != maps.length) {
            LOGGER.error("Invalid Parameter!", maps);
            throw new InvalidParameterException("Invalid Parameter!");
        }
        Map<String, Map<Object, Object>> result = new HashMap<String, Map<Object, Object>>();
        for (int i = 0; i < keys.length; i++) {
            result.put(keys[i], maps[i]);
        }
        return result;
    }

}
