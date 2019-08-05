package com.mohan.project.easycache.core;

import com.mohan.project.easycache.exception.GetValueByCallableException;
import com.mohan.project.easycache.selection.SelctionStrategyEnum;
import com.mohan.project.easycache.statistic.Statistic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class EasyCache<Key, Value> implements Cache<Key, Value> {

    /**
     * 写入后过期时间
     */
    private Long expireAfterWriteTime;

    /**
     * 写入后过期时间单位
     */
    private TimeUnit expireAfterWriteTimeUnit;

    /**
     * 访问后过期时间
     */
    private Long expireAfterAccessTime;

    /**
     * 访问后过期时间单位
     */
    private TimeUnit expireAfterAccessTimeUnit;

    private Long maxSize;

    private SelctionStrategyEnum selctionStrategy = SelctionStrategyEnum.VOLATILE_LRU;

    private Map<Key, Value> cache = new ConcurrentHashMap<>();
    private Statistic statistic = new Statistic();

    @Override
    public Optional<Value> get(Key key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Optional<Value> get(Key key, Callable<? extends Value> callable) throws GetValueByCallableException {
        if(cache.containsKey(key)) {
            return Optional.of(cache.get(key));
        }
        try {
            Value value = callable.call();
            cache.put(key, value);
            return Optional.of(value);
        } catch (Exception e) {
            throw new GetValueByCallableException(e);
        }
    }

    @Override
    public Optional<Value> getIfPresent(Key key) {
        if(cache.containsKey(key)) {
            return Optional.of(cache.get(key));
        }
        return Optional.empty();
    }

    @Override
    public Map<Key, Value> getAllPresent(Iterable<Key> keys) {
        Map<Key, Value> result = new HashMap<>();
        for (Key key : keys) {
            if(cache.containsKey(key)) {
                result.put(key, cache.get(key));
            }
        }
        return result;
    }

    @Override
    public void put(Key key, Value value) {
        cache.put(key, value);
    }

    @Override
    public void putAll(Map<? extends Key, ? extends Value> map) {
        map.forEach((k, v) -> cache.put(k, v));
    }

    @Override
    public void invalidate(Key key) {
        cache.remove(key);
    }

    @Override
    public void invalidateAll(Iterable<Key> keys) {
        for (Key key : keys) {
            cache.remove(key);
        }
    }

    @Override
    public void clearUp() {
        cache.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public Statistic statistic() {
        return statistic;
    }

    /**
     * EasyCache建造者
     * @param <Key> 缓存key
     * @param <Value> 缓存value
     * @author mohan
     * @date 2018-08-05 22:03:11
     */
    public static class EasyCacheBuilder<Key, Value> {

        /**
         * 写入后过期时间
         */
        private Long expireAfterWriteTime;

        /**
         * 写入后过期时间单位
         */
        private TimeUnit expireAfterWriteTimeUnit;

        /**
         * 访问后过期时间
         */
        private Long expireAfterAccessTime;

        /**
         * 访问后过期时间单位
         */
        private TimeUnit expireAfterAccessTimeUnit;

        private Long maxSize;

        private SelctionStrategyEnum selctionStrategy = SelctionStrategyEnum.VOLATILE_LRU;


        public static EasyCacheBuilder<Object, Object> builder() {
            return new EasyCacheBuilder<Object, Object>();
        }

        public EasyCacheBuilder<Key, Value> expireAfterWriteTime(Long expireAfterWriteTime) {
            this.expireAfterWriteTime = expireAfterWriteTime;
            return this;
        }

        public EasyCacheBuilder<Key, Value> expireAfterWriteTimeUnit(TimeUnit expireAfterWriteTimeUnit) {
            this.expireAfterWriteTimeUnit = expireAfterWriteTimeUnit;
            return this;
        }

        public EasyCacheBuilder<Key, Value> expireAfterAccessTime(Long expireAfterAccessTime) {
            this.expireAfterAccessTime = expireAfterAccessTime;
            return this;
        }

        public EasyCacheBuilder<Key, Value> expireAfterAccessTimeUnit(TimeUnit expireAfterAccessTimeUnit) {
            this.expireAfterAccessTimeUnit = expireAfterAccessTimeUnit;
            return this;
        }

        public EasyCacheBuilder<Key, Value> maxSize(Long maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public EasyCacheBuilder selctionStrategy(SelctionStrategyEnum selctionStrategy) {
            this.selctionStrategy = selctionStrategy;
            return this;
        }

        public <SubKey extends Key, SubValue extends Value> EasyCache<SubKey, SubValue> build() {
            EasyCache<SubKey, SubValue> easyCache = new EasyCache<>();
            easyCache.expireAfterWriteTime = this.expireAfterWriteTime;
            easyCache.expireAfterWriteTimeUnit = this.expireAfterWriteTimeUnit;
            easyCache.expireAfterAccessTime = this.expireAfterAccessTime;
            easyCache.expireAfterAccessTimeUnit = this.expireAfterAccessTimeUnit;
            easyCache.maxSize = this.maxSize;
            easyCache.selctionStrategy = this.selctionStrategy;
            return easyCache;
        }
    }
}

