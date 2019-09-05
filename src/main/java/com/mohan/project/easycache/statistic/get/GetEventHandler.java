package com.mohan.project.easycache.statistic.get;

import com.mohan.project.easycache.statistic.base.BaseEventHandler;
import com.mohan.project.easycache.statistic.base.EventEnum;
import com.mohan.project.easycache.statistic.base.StatisticEvent;

/**
 * EasyCache统计信息查询缓存事件处理类
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class GetEventHandler<Key> extends BaseEventHandler<Key, StatisticEvent<Key>>{

    @Override
    protected void doHandler(StatisticEvent event) {
        event.getStatistic().doGet(event.getKey(), event.isPresent());
    }

    @Override
    protected String getEventName() {
        return EventEnum.GetEvent.name();
    }
}