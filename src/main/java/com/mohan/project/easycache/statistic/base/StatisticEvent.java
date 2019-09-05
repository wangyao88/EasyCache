package com.mohan.project.easycache.statistic.base;

import com.mohan.project.easycache.statistic.Statistic;
import lombok.Data;

/**
 * EasyCache统计信息事件基类
 * @param <Key> 缓存key
 * @author mohan
 * @date 2018-09-05 14:15:38
 */
@Data
public class StatisticEvent<Key> {

    private Key key;
    private boolean present;
    private long loadTime;
    private Statistic<Key> statistic;
    private EventEnum eventType;
}