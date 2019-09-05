package com.mohan.project.easycache.statistic.call.success;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.mohan.project.easycache.statistic.Statistic;
import com.mohan.project.easycache.statistic.base.EventEnum;
import com.mohan.project.easycache.statistic.base.StatisticEvent;
import lombok.Data;

/**
 * EasyCache统计信息加载缓存成功事件转换器
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
@Data
public class CallSuccessfullyEventTranslator<Key, Event extends StatisticEvent<Key>> implements EventTranslatorTwoArg<Event, Statistic<Key>, Long> {

    @Override
    public void translateTo(Event event, long sequence, Statistic<Key> statistic, Long loadTime) {
        event.setStatistic(statistic);
        event.setLoadTime(loadTime);
        event.setEventType(EventEnum.CallSuccessfullyEvent);
    }
}