package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;

/**
 * 缓存数据淘汰实现类
 * SelctionStrategyEnum.VOLATILE_TTL
 * 从已设置过期时间的数据集中挑选将要过期的数据淘汰
 * @author mohan
 * @date 2018-08-06 23:23:30
 */
public class VolatileTtlSelctionyHandler implements SelctionStrategyHandler{

    @Override
    public <Key, Value> void handle(EasyCache<Key, Value> easyCache) {

    }
}
