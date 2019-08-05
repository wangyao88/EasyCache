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
    private Map<Key, ExpireRecorder> expireRecorder = new ConcurrentHashMap<>();

    public Statistic(boolean enableExpireAfterWrite, boolean enableExpireAfterAccess) {
        this.enableExpireAfterWrite = enableExpireAfterWrite;
        this.enableExpireAfterAccess = enableExpireAfterAccess;
    }

    private static class ExpireRecorder {

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
