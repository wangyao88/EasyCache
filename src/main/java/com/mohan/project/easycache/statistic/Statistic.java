package com.mohan.project.easycache.statistic;

import java.util.Map;
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
    private Map<Key, ExpireRecorder> expireRecorderMap = new ConcurrentHashMap<>();

    public Statistic(boolean enableExpireAfterWrite, boolean enableExpireAfterAccess) {
        this.enableExpireAfterWrite = enableExpireAfterWrite;
        this.enableExpireAfterAccess = enableExpireAfterAccess;
    }

    public void doGet(Key key, boolean present) {
        if(present && enableExpireAfterAccess) {
            long now = System.currentTimeMillis();
            if(expireRecorderMap.containsKey(key)) {
               ExpireRecorder expireRecorder = this.expireRecorderMap.get(key);
               expireRecorder.setAccessTime(now);
           }else {
               ExpireRecorder expireRecorder = new ExpireRecorder();
               expireRecorder.setAccessTime(now);
               expireRecorderMap.put(key, expireRecorder);
           }
        }
    }

    public void doWrite(Key key) {
        if(enableExpireAfterWrite) {
            long now = System.currentTimeMillis();
            if(expireRecorderMap.containsKey(key)) {
                ExpireRecorder expireRecorder = this.expireRecorderMap.get(key);
                expireRecorder.setWriteTime(now);
            }else {
                ExpireRecorder expireRecorder = new ExpireRecorder();
                expireRecorder.setWriteTime(now);
                expireRecorderMap.put(key, expireRecorder);
            }
        }
    }

    public void doInvalidate(Key key) {
        expireRecorderMap.remove(key);
    }

    public void doClearUp() {
        expireRecorderMap.clear();
    }

    public Map<Key, ExpireRecorder> getExpireRecorderMap() {
        return expireRecorderMap;
    }

    public Boolean getEnableExpireAfterWrite() {
        return enableExpireAfterWrite;
    }

    public Boolean getEnableExpireAfterAccess() {
        return enableExpireAfterAccess;
    }

    public static class ExpireRecorder {

        private Long writeTime;
        private Long accessTime;

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
    }
}
