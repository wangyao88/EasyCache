package com.mohan.project.easycache.statistic;

import com.mohan.project.easytools.file.FileTools;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * EasyCache统计信息类
 * @param <Key> 缓存key
 * @author mohan
 * @date 2018-08-05 22:43:38
 */
public class Statistic<Key> {

    private long hitCount;
    private long missCount;
    private long loadSuccessCount;
    private long loadExceptionCount;
    private long totalLoadTime;
    private long evictionCount;
    private Boolean enableExpireAfterWrite;
    private Boolean enableExpireAfterAccess;
    private Map<Key, ExpireRecorder<Key>> expireRecorderMap = new ConcurrentHashMap<>();
    private Map<Key, Counter<Key>> allKeysUsedCounter = new ConcurrentHashMap<>();


    public Statistic(boolean enableExpireAfterWrite, boolean enableExpireAfterAccess) {
        this.enableExpireAfterWrite = enableExpireAfterWrite;
        this.enableExpireAfterAccess = enableExpireAfterAccess;
    }

    public void doGet(Key key, boolean present) {
        if(present) {
            hitCount++;
            if(allKeysUsedCounter.containsKey(key)) {
                Counter<Key> counter = allKeysUsedCounter.get(key);
                counter.setAccessTime(System.currentTimeMillis());
                counter.increateCount();
            }else {
                allKeysUsedCounter.put(key, new Counter<>(key, System.currentTimeMillis(), 1));
            }

            if(enableExpireAfterAccess) {
                long now = System.currentTimeMillis();
                if(expireRecorderMap.containsKey(key)) {
                    ExpireRecorder<Key> expireRecorder = this.expireRecorderMap.get(key);
                    expireRecorder.setAccessTime(now);
                    expireRecorder.setUsedNum(1);
                }else {
                    ExpireRecorder<Key> expireRecorder = new ExpireRecorder<>();
                    expireRecorder.setAccessTime(now);
                    expireRecorder.setKey(key);
                    expireRecorder.increateUsedNum();
                    expireRecorderMap.put(key, expireRecorder);
                }
            }
        }else {
            missCount++;
        }
    }

    public void doCallSuccessfully(long loadTime) {
        loadSuccessCount++;
        totalLoadTime += loadTime;
    }

    public void doCallFail() {
        loadExceptionCount++;
    }

    public void doEviction() {
        evictionCount++;
    }

    public void doPut(Key key) {
        allKeysUsedCounter.put(key, new Counter<>(key, System.currentTimeMillis(), 1));
        if(enableExpireAfterWrite) {
            long now = System.currentTimeMillis();
            if(expireRecorderMap.containsKey(key)) {
                ExpireRecorder<Key> expireRecorder = this.expireRecorderMap.get(key);
                expireRecorder.setWriteTime(now);
                expireRecorder.setUsedNum(1);
            }else {
                ExpireRecorder<Key> expireRecorder = new ExpireRecorder<>();
                expireRecorder.setWriteTime(now);
                expireRecorder.setKey(key);
                expireRecorder.increateUsedNum();
                expireRecorderMap.put(key, expireRecorder);
            }
        }
    }

    public void doPutAndGet(Key key) {
        if(enableExpireAfterWrite) {
            EasyCacheDisruptor.<Key>getInstance().publishPutEvent(this, key);
        }
        if(enableExpireAfterAccess) {
            EasyCacheDisruptor.<Key>getInstance().publishGetEvent(this, key, enableExpireAfterWrite);
        }
    }

    public void doInvalidate(Key key) {
        expireRecorderMap.remove(key);
    }

    public void doClearUp() {
        expireRecorderMap.clear();
    }

    public Map<Key, ExpireRecorder<Key>> getExpireRecorderMap() {
        return expireRecorderMap;
    }

    public Map<Key, Counter<Key>> getAllKeysCounter() {
        return allKeysUsedCounter;
    }

    public Boolean getEnableExpireAfterWrite() {
        return enableExpireAfterWrite;
    }

    public Boolean getEnableExpireAfterAccess() {
        return enableExpireAfterAccess;
    }

    public String getCurrentStatisticInfo() {
        System.out.println(this.hitCount+this.missCount);
        StringBuilder info = new StringBuilder();
//        info.append(FileTools.getBanner());
        info.append("基本信息").append(FileTools.LF);
        info.append("命中次数：").append(hitCount).append(FileTools.LF)
            .append("未命中次数：").append(missCount).append(FileTools.LF)
            .append("成功加载次数：").append(loadSuccessCount).append(FileTools.LF)
            .append("未成功加载次数：").append(loadExceptionCount).append(FileTools.LF)
            .append("加载总耗时：").append(totalLoadTime).append(FileTools.LF)
            .append("淘汰数据次数：").append(evictionCount).append(FileTools.LF);
        info.append("---------------------------------------------").append(FileTools.LF);
        info.append("存储信息").append(FileTools.LF);
        info.append("缓存数量：").append(allKeysUsedCounter.size()).append(FileTools.LF);
        String sizeInRam = RamUsageEstimator.humanReadableUnits(RamUsageEstimator.shallowSizeOf(allKeysUsedCounter.values()));
        info.append("缓存大小：").append(sizeInRam).append(FileTools.LF);
//        if(getEnableExpireAfterWrite() || getEnableExpireAfterAccess()) {
//            info.append("---------------------------------------------").append(Constant.LF);
//            info.append("过期信息").append(Constant.LF);
//
//        }
        return info.toString();
    }

    public static class ExpireRecorder<Key> {

        private Long writeTime;
        private Long accessTime;
        private int usedNum;
        private Key key;

        public Long getEearliestTime() {
            if(Objects.isNull(writeTime)) {
                return accessTime;
            }
            if(Objects.isNull(accessTime)) {
                return writeTime;
            }
            return Math.min(writeTime, accessTime);
        }

        public Long getLatestTime() {
            if(Objects.isNull(writeTime)) {
                return accessTime;
            }
            if(Objects.isNull(accessTime)) {
                return writeTime;
            }
            return Math.max(writeTime, accessTime);
        }

        public Long getWriteTime() {
            return writeTime;
        }

        public void setWriteTime(Long writeTime) {
            this.writeTime = writeTime;
        }

        public Long getAccessTime() {
            return accessTime;
        }

        public void setAccessTime(Long accessTime) {
            this.accessTime = accessTime;
        }

        public int getUsedNum() {
            return usedNum;
        }

        public void setUsedNum(int usedNum) {
            this.usedNum = usedNum;
        }

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public void increateUsedNum() {
            setUsedNum(getUsedNum()+1);
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }
            ExpireRecorder<?> that = (ExpireRecorder<?>) o;
            return key.equals(that.key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    public static class Counter<Key> {

        private Key key;
        private Long accessTime;
        private int count;

        public Counter(Key key, Long accessTime, int count) {
            this.key = key;
            this.accessTime = accessTime;
            this.count = count;
        }

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public Long getAccessTime() {
            return accessTime;
        }

        public void setAccessTime(Long accessTime) {
            this.accessTime = accessTime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }
            Counter<?> counter = (Counter<?>) o;
            return key.equals(counter.key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        public void increateCount() {
            setCount(getCount()+1);
        }
    }
}