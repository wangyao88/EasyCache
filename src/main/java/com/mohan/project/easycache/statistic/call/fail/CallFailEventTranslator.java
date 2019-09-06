package com.mohan.project.easycache.statistic.call.fail;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.mohan.project.easycache.statistic.Statistic;
import com.mohan.project.easycache.statistic.base.EventEnum;
import com.mohan.project.easycache.statistic.base.StatisticEvent;

/**
 * EasyCache统计信息加载缓存失败事件转换器
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class CallFailEventTranslator<Key, Event extends StatisticEvent<Key>> implements EventTranslatorOneArg<Event, Statistic<Key>> {

    @Override
    public void translateTo(Event event, long sequence, Statistic<Key> statistic) {
        event.setStatistic(statistic);
        event.setEventType(EventEnum.CallFailEvent);
    }
}