package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;
import com.mohan.project.easycache.statistic.Statistic;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 缓存数据淘汰实现类
 * SelctionStrategyEnum.ALLKEYS_LRU
 * 从数据集中挑选最近最少使用的数据淘汰
 * @author mohan
 * @date 2018-08-06 23:23:30
 */
public class AllkeysLruSelctionyHandler extends SelctionStrategyHandler{

    @Override
    protected <Key, Value> List<Key> doHandle(long selectionNum, EasyCache<Key, Value> easyCache) {
        Map<Key, Statistic.Counter<Key>> allKeysCounter = easyCache.getStatistic().getAllKeysCounter();
        Collection<Statistic.Counter<Key>> counters = allKeysCounter.values();
        return counters.stream()
                       .sorted(Comparator.comparing(Statistic.Counter<Key>::getAccessTime).thenComparingInt(Statistic.Counter::getCount))
                       .limit(selectionNum)
                       .map(Statistic.Counter::getKey)
                       .collect(Collectors.toList());
    }
}