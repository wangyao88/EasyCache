package com.mohan.project.easycache.core;

import com.mohan.project.easycache.selection.SelctionStrategyEnum;
import com.mohan.project.easycache.selection.SelctionStrategyManager;
import com.mohan.project.easycache.statistic.Statistic;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    private Map<SeverKey, EasyCache<Key, Value>> easyCacheMap = new ConcurrentHashMap<>();
    private static ScheduledExecutorService THREAD_POOL_EXECUTOR = null;

    private EasyCacheServer() {
        THREAD_POOL_EXECUTOR = Executors.newScheduledThreadPool(10);
        THREAD_POOL_EXECUTOR.scheduleAtFixedRate(this::process, 0, 1, TimeUnit.SECONDS);
        THREAD_POOL_EXECUTOR.scheduleAtFixedRate(this::doSelection, 0, 1, TimeUnit.SECONDS);
    }

    private static class Singleton {
        private static final EasyCacheServer EASY_CACHE_SERVER = new EasyCacheServer();
    }

    public static EasyCacheServer getInstance() {
        return Singleton.EASY_CACHE_SERVER;
    }

    private static class SeverKey {
        private String key;
        private Boolean enableExpireAfterWrite;
        private Boolean enableExpireAfterAccess;

        public SeverKey(String key, Boolean enableExpireAfterWrite, Boolean enableExpireAfterAccess) {
            this.key = key;
            this.enableExpireAfterWrite = enableExpireAfterWrite;
            this.enableExpireAfterAccess = enableExpireAfterAccess;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Boolean getEnableExpireAfterWrite() {
            return enableExpireAfterWrite;
        }

        public void setEnableExpireAfterWrite(Boolean enableExpireAfterWrite) {
            this.enableExpireAfterWrite = enableExpireAfterWrite;
        }

        public Boolean getEnableExpireAfterAccess() {
            return enableExpireAfterAccess;
        }

        public void setEnableExpireAfterAccess(Boolean enableExpireAfterAccess) {
            this.enableExpireAfterAccess = enableExpireAfterAccess;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }
            SeverKey severKey = (SeverKey) o;
            return Objects.equals(key, severKey.key) &&
                    Objects.equals(enableExpireAfterWrite, severKey.enableExpireAfterWrite) &&
                    Objects.equals(enableExpireAfterAccess, severKey.enableExpireAfterAccess);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, enableExpireAfterWrite, enableExpireAfterAccess);
        }
    }

    public void server(EasyCache<Key, Value> easyCache) {
        Statistic<Key> statistic = easyCache.getStatistic();
        SeverKey severKey = new SeverKey(easyCache.getId(), statistic.getEnableExpireAfterWrite(), statistic.getEnableExpireAfterAccess());
        easyCacheMap.put(severKey, easyCache);
    }

    private void process() {
        easyCacheMap.forEach((severKey, easyCache) -> {
            if(severKey.getEnableExpireAfterWrite()) {
                doExpireAfterWrite(easyCache);
            }
            if(severKey.getEnableExpireAfterAccess()) {
                doExpireAfterAccess(easyCache);
            }
        });
    }

    private void doExpireAfterWrite(EasyCache<Key, Value> easyCache) {
        Statistic<Key> statistic = easyCache.getStatistic();
        Map<Key, Statistic.ExpireRecorder<Key>> expireRecorderMap = statistic.getExpireRecorderMap();
        Long expireAfterWriteTime = easyCache.getExpireAfterWriteTime();
        ChronoUnit expireAfterWriteChronoUnit = easyCache.getExpireAfterWriteChronoUnit();
        doExpire(easyCache, expireRecorderMap, expireAfterWriteTime, expireAfterWriteChronoUnit, true);
    }

    private void doExpireAfterAccess(EasyCache<Key, Value> easyCache) {
        Statistic<Key> statistic = easyCache.getStatistic();
        Map<Key, Statistic.ExpireRecorder<Key>> expireRecorderMap = statistic.getExpireRecorderMap();
        Long expireAfterAccessTime = easyCache.getExpireAfterAccessTime();
        ChronoUnit expireAfterAccessChronoUnit = easyCache.getExpireAfterAccessChronoUnit();
        doExpire(easyCache, expireRecorderMap, expireAfterAccessTime, expireAfterAccessChronoUnit, false);
    }

    private void doExpire(EasyCache<Key, Value> easyCache, Map<Key, Statistic.ExpireRecorder<Key>> expireRecorderMap, Long expireTime, ChronoUnit expireChronoUnit, boolean isWrite) {
        Set<Key> keys = expireRecorderMap.keySet();
        for (Key key : keys) {
            Statistic.ExpireRecorder<Key> expireRecorder = expireRecorderMap.get(key);
            Long time = isWrite ? expireRecorder.getWriteTime() : expireRecorder.getAccessTime();
            if(isExpired(time, expireTime, expireChronoUnit)) {
                easyCache.invalidate(key);
            }
        }
    }

    private boolean isExpired(Long time, Long expireTime, ChronoUnit expireChronoUnit) {
        LocalDateTime writeLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        LocalDateTime checkDateTime = writeLocalDateTime.plus(expireTime, expireChronoUnit);
        return checkDateTime.isBefore(LocalDateTime.now());
    }

    private void doSelection() {
        easyCacheMap.forEach((severKey, easyCache) -> {
            if(easyCache.size() >= easyCache.getMaxSize()) {
                SelctionStrategyManager.doSelection(easyCache);
            }
        });
    }

}