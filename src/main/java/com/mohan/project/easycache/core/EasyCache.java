package com.mohan.project.easycache.core;

import com.mohan.project.easycache.exception.GetValueByCallableException;
import com.mohan.project.easycache.statistic.Statistic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EasyCache核心类，对外提供缓存API
 * @param <Key> 缓存key
 * @param <Value> 缓存value
 * @author mohan
 * @date 2019-08-01 10:19:12
 */
public class EasyCache<Key, Value> implements Cache<Key, Value> {

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
    public Map<Key, Value> getAll(Iterable<Key> keys) {
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
    public synchronized void clearUp() {
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
}

