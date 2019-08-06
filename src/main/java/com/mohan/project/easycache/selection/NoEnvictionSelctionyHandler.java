package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;

/**
 * 缓存数据淘汰实现类
 * SelctionStrategyEnum.NO_ENVICTION
 * 禁止驱逐数据
 * @author mohan
 * @date 2018-08-06 23:23:30
 */
public class NoEnvictionSelctionyHandler implements SelctionStrategyHandler{

    @Override
    public <Key, Value> void handle(EasyCache<Key, Value> easyCache) {}
}
