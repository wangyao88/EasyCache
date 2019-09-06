package com.mohan.project.easycache.statistic.get;

import com.lmax.disruptor.EventTranslatorThreeArg;
import com.mohan.project.easycache.statistic.Statistic;
import com.mohan.project.easycache.statistic.base.EventEnum;
import com.mohan.project.easycache.statistic.base.StatisticEvent;

/**
 * EasyCache统计信息查询缓存事件转换器
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class GetEventTranslator<Key, Event extends StatisticEvent<Key>> implements EventTranslatorThreeArg<Event, Statistic<Key>, Key, Boolean> {

    @Override
    public void translateTo(Event event, long sequence, Statistic<Key> statistic, Key key, Boolean present) {
        event.setStatistic(statistic);
        event.setKey(key);
        event.setPresent(present);
        event.setEventType(EventEnum.GetEvent);
    }
}