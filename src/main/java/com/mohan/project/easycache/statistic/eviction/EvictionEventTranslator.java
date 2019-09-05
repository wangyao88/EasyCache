package com.mohan.project.easycache.statistic.eviction;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.mohan.project.easycache.statistic.Statistic;
import com.mohan.project.easycache.statistic.base.EventEnum;
import com.mohan.project.easycache.statistic.base.StatisticEvent;
import lombok.Data;

/**
 * EasyCache统计信息驱逐缓存事件转换器
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
@Data
public class EvictionEventTranslator<Key, Event extends StatisticEvent<Key>> implements EventTranslatorOneArg<Event, Statistic<Key>> {

    @Override
    public void translateTo(Event event, long sequence, Statistic<Key> statistic) {
        event.setStatistic(statistic);
        event.setEventType(EventEnum.EvictionEvent);
    }
}