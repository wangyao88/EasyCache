package com.mohan.project.easycache.statistic.call.success;

import com.mohan.project.easycache.statistic.base.BaseEventHandler;
import com.mohan.project.easycache.statistic.base.EventEnum;
import com.mohan.project.easycache.statistic.base.StatisticEvent;

/**
 * EasyCache统计信息加载缓存成功事件处理类
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class CallSuccessfullyEventHandler<Key> extends BaseEventHandler<Key, StatisticEvent<Key>> {

    @Override
    protected void doHandler(StatisticEvent event) {
        event.getStatistic().doCallSuccessfully(event.getLoadTime());
    }

    @Override
    protected String getEventName() {
        return EventEnum.CallSuccessfullyEvent.name();
    }
}