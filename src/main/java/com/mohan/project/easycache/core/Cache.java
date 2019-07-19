package com.mohan.project.easycache.core;

import com.mohan.project.easycache.exception.GetValueByCallableException;
import com.mohan.project.easycache.statistic.Statistic;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

public interface Cache<Key, Value> {

    Optional<Value> get(Key key);

    Optional<Value> get(Key key, Callable<? extends Value> callable) throws GetValueByCallableException;;

    Optional<Value> getIfPresent(Key key);

    Map<Key, Value> getAllPresent(Iterable<Key> keys);

    void put(Key key, Value value);

    void putAll(Map<? extends Key, ? extends Value> map);

    void invalidate(Key key);

    void invalidateAll(Iterable<Key> keys);

    void clearUp();

    long size();

    Statistic statistic();
}
