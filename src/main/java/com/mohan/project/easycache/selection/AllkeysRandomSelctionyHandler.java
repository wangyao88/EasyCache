package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 缓存数据淘汰实现类
 * SelctionStrategyEnum.ALLKEYS_RANDOM
 * 从数据集中任意选择数据淘汰
 * @author mohan
 * @date 2018-08-06 23:23:30
 */
public class AllkeysRandomSelctionyHandler extends SelctionStrategyHandler{

    @Override
    protected <Key, Value> List<Key> doHandle(long selectionNum, EasyCache<Key, Value> easyCache) {
        List<Key> allKeys = easyCache.getAllKeys();
        Collections.shuffle(allKeys);
        return allKeys.stream().limit(selectionNum).collect(Collectors.toList());
    }
}