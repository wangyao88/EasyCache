package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存数据淘汰管理类
 * @author mohan
 * @date 2018-08-06 23:16:53
 */
public class SelctionStrategyManager {

    private static Map<SelctionStrategyEnum, SelctionStrategyHandler> handlers = new HashMap<>();

    static {
        handlers.put(SelctionStrategyEnum.VOLATILE_LRU, new VolatileLruSelctionyHandler());
        handlers.put(SelctionStrategyEnum.VOLATILE_TTL, new VolatileTtlSelctionyHandler());
        handlers.put(SelctionStrategyEnum.VOLATILE_RANDOM, new VolatileRandomSelctionyHandler());
        handlers.put(SelctionStrategyEnum.ALLKEYS_LRU, new AllkeysLruSelctionyHandler());
        handlers.put(SelctionStrategyEnum.ALLKEYS_RANDOM, new AllkeysRandomSelctionyHandler());
        handlers.put(SelctionStrategyEnum.NO_ENVICTION, new NoEnvictionSelctionyHandler());
    }

    public static <Key, Value> void doSelection(EasyCache<Key, Value> easyCache) {
        handlers.getOrDefault(easyCache.getSelctionStrategy(), new NoEnvictionSelctionyHandler()).handle(easyCache);
    }
}
