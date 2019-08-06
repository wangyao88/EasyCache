package com.mohan.project.easycache.core;

import com.mohan.project.easycache.exception.GetValueByCallableException;
import com.mohan.project.easycache.exception.ReachingMaxSizeException;
import com.mohan.project.easycache.selection.SelctionStrategyEnum;
import com.mohan.project.easycache.statistic.Statistic;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EasyCache核心类
 * @param <Key> 缓存key
 * @param <Value> 缓存value
 * @author mohan
 * @date 2018-08-05 22:43:38
 */
public class EasyCache<Key, Value> implements Cache<Key, Value> {

    /**
     * EasyCache唯一ID
     */
    private String id;

    /**
     * 写入后过期时间
     */
    private Long expireAfterWriteTime;

    /**
     * 写入后过期时间单位
     */
    private ChronoUnit expireAfterWriteChronoUnit;

    /**
     * 访问后过期时间
     */
    private Long expireAfterAccessTime;

    /**
     * 访问后过期时间单位
     */
    private ChronoUnit expireAfterAccessChronoUnit;

    /**
     * 缓存中最大元素数量
     */
    private Long maxSize;

    /**
     * 数据淘汰策略
     */
    private SelctionStrategyEnum selctionStrategy = SelctionStrategyEnum.VOLATILE_LRU;

    /**
     * 缓存数据统计信息
     */
    private Statistic<Key> statistic;

    private Map<Key, Value> cache = new ConcurrentHashMap<>();

    @Override
    public Optional<Value> get(Key key) {
        Optional<Value> valueOptional = Optional.ofNullable(cache.get(key));
        statistic.doGet(key, valueOptional.isPresent());
        return valueOptional;
    }

    @Override
    public Optional<Value> get(Key key, Callable<? extends Value> callable) throws GetValueByCallableException {
        if(cache.containsKey(key)) {
            Optional<Value> valueOptional = Optional.of(cache.get(key));
            statistic.doGet(key, valueOptional.isPresent());
            return valueOptional;
        }
        try {
            Value value = callable.call();
            cache.put(key, value);
            Optional<Value> valueOptional = Optional.of(value);
            statistic.doGet(key, true);
            return valueOptional;
        } catch (Exception e) {
            throw new GetValueByCallableException(e);
        }
    }

    @Override
    public Optional<Value> getIfPresent(Key key) {
        if(cache.containsKey(key)) {
            Optional<Value> valueOptional = Optional.of(cache.get(key));
            statistic.doGet(key, valueOptional.isPresent());
            return valueOptional;
        }
        return Optional.empty();
    }

    @Override
    public Map<Key, Value> getAll(Iterable<Key> keys) {
        Map<Key, Value> result = new HashMap<>();
        for (Key key : keys) {
            if(cache.containsKey(key)) {
                statistic.doGet(key, true);
                result.put(key, cache.get(key));
            }
        }
        return result;
    }

    @Override
    public void put(Key key, Value value) {
        if(size() > getMaxSize()) {
            throw new ReachingMaxSizeException();
        }
        cache.put(key, value);
        statistic.doWrite(key);
    }

    @Override
    public void putAll(Map<? extends Key, ? extends Value> map) {
        if(size() > getMaxSize()) {
            throw new ReachingMaxSizeException();
        }
        map.forEach((k, v) -> {
            cache.put(k, v);
            statistic.doWrite(k);
        });
    }

    @Override
    public void invalidate(Key key) {
        cache.remove(key);
        statistic.doInvalidate(key);
    }

    @Override
    public void invalidateAll(Iterable<Key> keys) {
        for (Key key : keys) {
            cache.remove(key);
            statistic.doInvalidate(key);
        }
    }

    @Override
    public void clearUp() {
        cache.clear();
        statistic.doClearUp();
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public Statistic statistic() {
        return statistic;
    }

    public String getId() {
        return id;
    }

    public Long getExpireAfterWriteTime() {
        return expireAfterWriteTime;
    }

    public ChronoUnit getExpireAfterWriteChronoUnit() {
        return expireAfterWriteChronoUnit;
    }

    public Long getExpireAfterAccessTime() {
        return expireAfterAccessTime;
    }

    public ChronoUnit getExpireAfterAccessChronoUnit() {
        return expireAfterAccessChronoUnit;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    public SelctionStrategyEnum getSelctionStrategy() {
        return selctionStrategy;
    }

    public Statistic<Key> getStatistic() {
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
        private ChronoUnit expireAfterWriteChronoUnit = ChronoUnit.SECONDS;

        /**
         * 访问后过期时间
         */
        private Long expireAfterAccessTime;

        /**
         * 访问后过期时间单位
         */
        private ChronoUnit expireAfterAccessChronoUnit = ChronoUnit.SECONDS;

        /**
         * 缓存中最大元素数量
         */
        private Long maxSize;

        /**
         * 数据淘汰策略
         */
        private SelctionStrategyEnum selctionStrategy = SelctionStrategyEnum.VOLATILE_LRU;

        public static EasyCacheBuilder<Object, Object> newBuilder() {
            return new EasyCacheBuilder<Object, Object>();
        }

        public EasyCacheBuilder<Key, Value> expireAfterWriteTime(Long expireAfterWriteTime) {
            this.expireAfterWriteTime = expireAfterWriteTime;
            return this;
        }

        public EasyCacheBuilder<Key, Value> expireAfterWriteChronoUnit(ChronoUnit expireAfterWriteTimeUnit) {
            this.expireAfterWriteChronoUnit = expireAfterWriteTimeUnit;
            return this;
        }

        public EasyCacheBuilder<Key, Value> expireAfterAccessTime(Long expireAfterAccessTime) {
            this.expireAfterAccessTime = expireAfterAccessTime;
            return this;
        }

        public EasyCacheBuilder<Key, Value> expireAfterAccessChronoUnit(ChronoUnit expireAfterAccessTimeUnit) {
            this.expireAfterAccessChronoUnit = expireAfterAccessTimeUnit;
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
            easyCache.expireAfterWriteChronoUnit = Objects.isNull(this.expireAfterWriteChronoUnit) ? ChronoUnit.SECONDS : this.expireAfterWriteChronoUnit;
            easyCache.expireAfterAccessTime = this.expireAfterAccessTime;
            easyCache.expireAfterAccessChronoUnit = Objects.isNull(this.expireAfterAccessChronoUnit) ? ChronoUnit.SECONDS : this.expireAfterAccessChronoUnit;
            easyCache.maxSize = Objects.isNull(this.maxSize) ? Integer.MAX_VALUE : this.maxSize;
            easyCache.selctionStrategy = this.selctionStrategy;
            Statistic<SubKey> statistic = new Statistic<>(Objects.isNull(easyCache.expireAfterWriteTime), Objects.isNull(easyCache.expireAfterAccessTime));
            easyCache.statistic = statistic;
            easyCache.id = UUID.randomUUID().toString();
            EasyCacheServer.getInstance().server(easyCache);
            return easyCache;
        }
    }
}

