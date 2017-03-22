package com.github.hyacinth.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 简单实现的一个线程安全的缓存类
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/7/15
 * Time: 11:01
 */
public class PureCache<K, V> implements Cache<K, V> {
    //标识
    private final String name;
    private final HashMap<K, V> cache;
    //读写锁保证线程安全
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public PureCache(String name) {
        this.name = name;
        cache = new HashMap<K, V>();
    }

    public PureCache(String name, int initialCapacity) {
        this.name = name;
        cache = new HashMap<K, V>(initialCapacity);
    }

    /**
     * 获取缓存标识
     *
     * @return 缓存标识
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 通过单个key，从缓存获取数据
     *
     * @param key key
     * @return obj
     */
    @Override
    public V get(K key) {
        readLock.lock();
        try {
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 通过一组key，从缓存获取数据
     *
     * @param keys keys
     * @return map
     */
    @Override
    public Map<? extends K, ? extends V> getAll(Iterator<? extends K> keys) {
        readLock.lock();
        try {
            Map<K, V> map = new HashMap<K, V>();
            while (keys.hasNext()) {
                K key = keys.next();
                if (containsKey(key)) {
                    map.put(key, cache.get(key));
                }
            }

            return map;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 添加单个缓存
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void put(K key, V value) {
        writeLock.lock();
        try {
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 一次添加多个缓存
     *
     * @param entries map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> entries) {
        writeLock.lock();
        try {
            cache.putAll(entries);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 判断key值是否存在
     *
     * @param key key
     * @return boolean
     */
    @Override
    public boolean containsKey(K key) {
        readLock.lock();
        try {
            return cache.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 删除单个缓存
     *
     * @param key key
     */
    @Override
    public void invalidate(K key) {
        writeLock.lock();
        try {
            if (containsKey(key)) {
                cache.remove(key);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 删除多个缓存
     *
     * @param keys keys
     */
    @Override
    public void invalidateAll(Iterator<? extends K> keys) {
        writeLock.lock();
        try {
            while (keys.hasNext()) {
                K key = keys.next();
                invalidate(key);
            }

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 删除所有缓存
     */
    @Override
    public void invalidateAll() {
        writeLock.lock();
        try {
            cache.clear();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 获取缓存个数
     *
     * @return 个数
     */
    @Override
    public int size() {
        readLock.lock();
        try {
            return cache.size();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        writeLock.lock();
        try {
            cache.clear();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 获取所有缓存
     *
     * @return map
     */
    @Override
    public Map<? extends K, ? extends V> asMap() {
        readLock.lock();
        try {
            return new ConcurrentHashMap<K, V>(cache);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 验证缓存是否为空
     *
     * @return boolean
     */
    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return cache.isEmpty();
        } finally {
            readLock.unlock();
        }
    }
}
