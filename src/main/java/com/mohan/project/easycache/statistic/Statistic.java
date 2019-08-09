package com.mohan.project.easycache.statistic;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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


    public Statistic(boolean enableExpireAfterWrite, boolean enableExpireAfterAccess) {
        this.enableExpireAfterWrite = enableExpireAfterWrite;
        this.enableExpireAfterAccess = enableExpireAfterAccess;
    }

    public void doGet(Key key, boolean present) {
        if(present && enableExpireAfterAccess) {
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
    }

    public void doWrite(Key key) {
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

    public void doWirteAndGet(Key key) {
        if(enableExpireAfterWrite) {
            doWrite(key);
        }
        if(enableExpireAfterAccess) {
            doGet(key, enableExpireAfterWrite);
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

    public Boolean getEnableExpireAfterWrite() {
        return enableExpireAfterWrite;
    }

    public Boolean getEnableExpireAfterAccess() {
        return enableExpireAfterAccess;
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
}
