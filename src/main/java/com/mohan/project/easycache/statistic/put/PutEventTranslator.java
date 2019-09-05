package com.mohan.project.easycache.statistic.put;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.mohan.project.easycache.statistic.Statistic;
import com.mohan.project.easycache.statistic.base.EventEnum;
import com.mohan.project.easycache.statistic.base.StatisticEvent;

/**
 * EasyCache统计信息添加缓存事件转换器
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class PutEventTranslator<Key, Event extends StatisticEvent<Key>> implements EventTranslatorTwoArg<Event, Statistic<Key>, Key> {

    @Override
    public void translateTo(Event event, long sequence, Statistic<Key> statistic, Key key) {
        event.setStatistic(statistic);
        event.setKey(key);
        event.setEventType(EventEnum.PutEvent);
    }
}