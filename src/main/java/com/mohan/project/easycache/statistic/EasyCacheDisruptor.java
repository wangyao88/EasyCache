package com.mohan.project.easycache.statistic;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.mohan.project.easycache.statistic.base.StatisticEvent;
import com.mohan.project.easycache.statistic.base.BaseEventFactory;
import com.mohan.project.easycache.statistic.base.BaseEventHandler;
import com.mohan.project.easycache.statistic.call.fail.CallFailEventTranslator;
import com.mohan.project.easycache.statistic.call.success.CallSuccessfullyEventTranslator;
import com.mohan.project.easycache.statistic.eviction.EvictionEventTranslator;
import com.mohan.project.easycache.statistic.get.GetEventTranslator;
import com.mohan.project.easycache.statistic.put.PutEventTranslator;

import java.util.concurrent.Executors;

/**
 * EasyCache统计信息事件生产消费模型
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class EasyCacheDisruptor<Key> {

    private RingBuffer<? extends StatisticEvent<Key>> ringBuffer;

    private EasyCacheDisruptor() {}

    private static class Singleton {
        private static final EasyCacheDisruptor EASY_CACHE_DISRUPTOR = new EasyCacheDisruptor();
    }

    public static EasyCacheDisruptor getInstance() {
        return Singleton.EASY_CACHE_DISRUPTOR;
    }

    public void start() {
        EventFactory<? extends StatisticEvent<Key>> eventFactory = new BaseEventFactory<>();
        //环形队列长度，必须是2的N次方
        int bufferSize = 1024 * 1024;
        Disruptor<? extends StatisticEvent<Key>> disruptor = new Disruptor<>(eventFactory, bufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new BlockingWaitStrategy());
        disruptor.handleEventsWith(new BaseEventHandler<>());
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publishCallFailEvent(Statistic<Key> statistic) {
        this.ringBuffer.publishEvent(new CallFailEventTranslator<>(), statistic);
    }

    public void publishCallSuccessfullyEvent(Statistic<Key> statistic, Long loadTime) {
        this.ringBuffer.publishEvent(new CallSuccessfullyEventTranslator<>(), statistic, loadTime);
    }

    public void publishEvictionEvent(Statistic<Key> statistic) {
        this.ringBuffer.publishEvent(new EvictionEventTranslator<>(), statistic);
    }

    public void publishGetEvent(Statistic<Key> statistic, Key key, boolean present) {
        this.ringBuffer.publishEvent(new GetEventTranslator<>(), statistic, key, present);
    }

    public void publishPutEvent(Statistic<Key> statistic, Key key) {
        this.ringBuffer.publishEvent(new PutEventTranslator<>(), statistic, key);
    }
}