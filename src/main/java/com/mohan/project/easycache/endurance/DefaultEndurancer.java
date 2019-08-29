package com.mohan.project.easycache.endurance;

import com.mohan.project.easycache.core.EasyCache;

/**
 * EasyCache持久器默认实现类
 *
 * @param <Key> 缓存key
 * @param <Value> 缓存value
 * @author mohan
 * @date 2018-08-29 16:11:39
 */
public class DefaultEndurancer<Key, Value> implements Endurancer<Key, Value> {

    @Override
    public boolean write(EasyCache<Key, Value> easyCache) {
        return false;
    }

    @Override
    public EasyCache<Key, Value> read() {
        return null;
    }
}