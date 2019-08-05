package com.mohan.project.easycache.core;

import com.mohan.project.easycache.exception.GetValueByCallableException;
import com.mohan.project.easycache.statistic.Statistic;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * EasyCache接口类，定义对外提供缓存的API
 * @param <Key> 缓存key
 * @param <Value> 缓存value
 * @author mohan
 * @date 2019-08-01 10:19:12
 */
public interface Cache<Key, Value> {

    /**
     * 获取缓存中key对应的value
     * @param key 缓存key
     * @return 缓存value, 由Optional包裹
     */
    Optional<Value> get(Key key);

    /**
     * 获取缓存中key对应的value, 如果不包含此key，则调用callable获取value
     * 抵用成功后，将value发放入缓存，同事返回该value
     * @param key 存key
     * @param callable 获取value的线程
     * @return key对应的value
     * @throws GetValueByCallableException 调用callable失败的异常
     */
    Optional<Value> get(Key key, Callable<? extends Value> callable) throws GetValueByCallableException;;

    /**
     * 批量获取缓存中keys对应的value集合
     * @param keys 缓存中的key集合
     * @return keys对应缓存中的value集合
     */
    Map<Key, Value> getAll(Iterable<Key> keys);

    /**
     * 增加缓存
     * @param key 缓存key
     * @param value 缓存value
     */
    void put(Key key, Value value);

    /**
     * 批量添加缓存
     * @param map 待添加的缓存map
     */
    void putAll(Map<? extends Key, ? extends Value> map);

    /**
     * 删除key对应的缓存
     * @param key 缓存key
     */
    void invalidate(Key key);

    /**
     * 欧亮删除缓存
     * @param keys 缓存key集合
     */
    void invalidateAll(Iterable<Key> keys);

    /**
     * 清空缓存
     */
    void clearUp();

    /**
     * 获取缓存键值对个数
     * @return
     */
    long size();

    /**
     * 获取缓存统计信息
     * @return
     */
    Statistic statistic();
}
