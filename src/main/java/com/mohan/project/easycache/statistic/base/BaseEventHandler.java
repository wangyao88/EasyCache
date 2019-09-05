package com.mohan.project.easycache.statistic.base;

import com.lmax.disruptor.EventHandler;

/**
 * EasyCache统计信息事件处理及基类
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class BaseEventHandler<Key, Event extends StatisticEvent<Key>> implements EventHandler<Event> {

    @Override
    public void onEvent(Event event, long sequence, boolean endOfBatch) {
        BaseEventHandlerManager.get(event.getEventType().name()).ifPresent(baseEventHandler -> baseEventHandler.doHandler(event));
    }

    protected void doHandler(Event event) {

    }

    protected String getEventName() {
        return StatisticEvent.class.getSimpleName();
    }
}