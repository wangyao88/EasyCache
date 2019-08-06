package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;

/**
 * 缓存数据淘汰实现类
 * SelctionStrategyEnum.ALLKEYS_RANDOM
 * 从数据集中任意选择数据淘汰
 * @author mohan
 * @date 2018-08-06 23:23:30
 */
public class AllkeysRandomSelctionyHandler implements SelctionStrategyHandler{

    @Override
    public <Key, Value> void handle(EasyCache<Key, Value> easyCache) {

    }
}
