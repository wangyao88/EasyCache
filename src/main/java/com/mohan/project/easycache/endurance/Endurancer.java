package com.mohan.project.easycache.endurance;

import com.mohan.project.easycache.core.EasyCache;

/**
 * EasyCache持久器接口
 *
 * @param <Key> 缓存key
 * @param <Value> 缓存value
 * @author mohan
 * @date 2018-08-29 16:11:39
 */
public interface Endurancer<Key, Value> {

    /**
     * 缓存持久化
     * @param easyCache 缓存对象
     * @return 持久化是否成功
     */
    boolean write(EasyCache<Key, Value> easyCache);

    /**
     * 恢复缓存
     * @return 恢复缓存
     */
    EasyCache<Key, Value> read();
}