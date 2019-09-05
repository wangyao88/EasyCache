package com.mohan.project.easycache.statistic.eviction;

import com.mohan.project.easycache.statistic.base.BaseEventHandler;
import com.mohan.project.easycache.statistic.base.EventEnum;
import com.mohan.project.easycache.statistic.base.StatisticEvent;

/**
 * EasyCache统计信息驱逐缓存事件处理类
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class EvictionEventHandler<Key> extends BaseEventHandler<Key, StatisticEvent<Key>> {

    @Override
    protected void doHandler(StatisticEvent event) {
        event.getStatistic().doEviction();
    }

    @Override
    protected String getEventName() {
        return EventEnum.EvictionEvent.name();
    }
}