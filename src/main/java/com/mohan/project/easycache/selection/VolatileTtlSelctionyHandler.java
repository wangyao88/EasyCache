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
 * SelctionStrategyEnum.VOLATILE_TTL
 * 从已设置过期时间的数据集中挑选将要过期的数据淘汰
 * @author mohan
 * @date 2018-08-06 23:23:30
 */
public class VolatileTtlSelctionyHandler extends SelctionStrategyHandler{

    @Override
    protected <Key, Value> void doHandle(long selectionNum, EasyCache<Key, Value> easyCache) {
        Map<Key, Statistic.ExpireRecorder<Key> > expireRecorderMap = easyCache.getStatistic().getExpireRecorderMap();
        Collection<Statistic.ExpireRecorder<Key>> expireRecorders = expireRecorderMap.values();
        List<Key> needSelectedKeys =
                expireRecorders.stream()
                               .sorted(Comparator.comparing(Statistic.ExpireRecorder::getEearliestTime))
                               .limit(selectionNum)
                               .map(Statistic.ExpireRecorder::getKey)
                               .collect(Collectors.toList());
        easyCache.invalidateAll(needSelectedKeys);
    }
}
