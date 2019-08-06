package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;

/**
 * 缓存数据淘汰接口
 * @author mohan
 * @date 2018-08-06 23:22:24
 */
public interface SelctionStrategyHandler {

    /**
     * 淘汰数据
     * @param easyCache 缓存核心类
     * @param <Key> 缓存key
     * @param <Value> 缓存value
     */
    <Key, Value> void handle(EasyCache<Key, Value> easyCache);
}
