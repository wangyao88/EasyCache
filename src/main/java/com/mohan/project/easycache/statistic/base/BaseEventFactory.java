package com.mohan.project.easycache.statistic.base;

import com.lmax.disruptor.EventFactory;

/**
 * EasyCache统计信息事件基类工厂类
 * @param <Key> 缓存key
 * @author mohan
 * @date 2018-09-05 14:15:38
 */
public class BaseEventFactory<Key> implements EventFactory<StatisticEvent<Key>> {

    @Override
    public StatisticEvent<Key> newInstance() {
        return new StatisticEvent<>();
    }
}