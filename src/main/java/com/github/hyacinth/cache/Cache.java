package com.github.hyacinth.cache;

import java.util.Iterator;
import java.util.Map;

/**
 * 缓存接口
 *
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/7/26
 * Time: 13:15
 */
public interface Cache<K, V> {
    /**
     * 获取缓存标识
     *
     * @return 缓存标识
     */
    String getName();

    /**
     * 通过单个key，从缓存获取数据
     *
     * @param key key
     * @return obj
     */
    V get(K key);

    /**
     * 通过一组key，从缓存获取数据
     *
     * @param keys keys
     * @return map
     */
    Map<? extends K, ? extends V> getAll(Iterator<? extends K> keys);

    /**
     * 添加单个缓存
     *
     * @param key   key
     * @param value value
     */
    void put(K key, V value);

    /**
     * 一次添加多个缓存
     *
     * @param entries map
     */
    void putAll(Map<? extends K, ? extends V> entries);

    /**
     * 判断key值是否存在
     *
     * @param key key
     * @return boolean
     */
    boolean containsKey(K key);

    /**
     * 删除单个缓存
     *
     * @param key key
     */
    void invalidate(K key);

    /**
     * 删除多个缓存
     *
     * @param keys keys
     */
    void invalidateAll(Iterator<? extends K> keys);

    /**
     * 删除所有缓存
     */
    void invalidateAll();

    /**
     * 获取缓存个数
     *
     * @return 个数
     */
    int size();

    /**
     * 清空缓存
     */
    void clear();

    /**
     * 获取所有缓存
     *
     * @return map
     */
    Map<? extends K, ? extends V> asMap();

    /**
     * 验证缓存是否为空
     *
     * @return boolean
     */
    boolean isEmpty();
}
