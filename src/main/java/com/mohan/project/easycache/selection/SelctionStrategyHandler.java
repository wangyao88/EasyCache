package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;

/**
 * 缓存数据淘汰抽象类
 * @author mohan
 * @date 2018-08-06 23:22:24
 */
public abstract class SelctionStrategyHandler {

    /**
     * 淘汰数据
     * @param easyCache 缓存核心类
     * @param <Key> 缓存key
     * @param <Value> 缓存value
     */
    public <Key, Value> void handle(EasyCache<Key, Value> easyCache) {
        long selectionNum = getSelectionNum(easyCache);
        doHandle(selectionNum, easyCache);
    }

    protected abstract <Key, Value> void doHandle(long selectionNum, EasyCache<Key, Value> easyCache);

    private <Key, Value> long getSelectionNum(EasyCache<Key, Value> easyCache) {
        return easyCache.getMaxSize()/10;
    }
}
