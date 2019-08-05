package com.mohan.project.easycache.core;

import com.mohan.project.easycache.statistic.Statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * EasyCache服务类
 * 后台执行数据淘汰，数据过期等操作
 * @param <Key> 缓存key
 * @param <Value> 缓存value
 * @author mohan
 * @date 2018-08-05 22:47:13
 */
public class EasyCacheServer<Key, Value> {

    private Map<String, EasyCache<Key, Value>> easyCacheMap = new ConcurrentHashMap<>();
    private static ScheduledExecutorService THREAD_POOL_EXECUTOR = null;

    private EasyCacheServer() {
        THREAD_POOL_EXECUTOR = Executors.newScheduledThreadPool(10);
        THREAD_POOL_EXECUTOR.scheduleAtFixedRate(this::doExpireAfterWrite, 0, 1, TimeUnit.SECONDS);
        THREAD_POOL_EXECUTOR.scheduleAtFixedRate(this::doExpireAfterAccess, 0, 1, TimeUnit.SECONDS);
        THREAD_POOL_EXECUTOR.scheduleAtFixedRate(this::doSelection, 0, 1, TimeUnit.SECONDS);
    }

    private static class Singleton {
        private static final EasyCacheServer EASY_CACHE_SERVER = new EasyCacheServer();
    }

    public static EasyCacheServer getInstance() {
        return Singleton.EASY_CACHE_SERVER;
    }

    public void server(EasyCache<Key, Value> easyCache) {
        easyCacheMap.put(easyCache.getId(), easyCache);
    }

    private void doExpireAfterWrite() {
        easyCacheMap.forEach((id, easyCache) -> {
            Statistic<Key> statistic = easyCache.getStatistic();
        });
    }

    private void doExpireAfterAccess() {

    }

    private void doSelection() {

    }

}
