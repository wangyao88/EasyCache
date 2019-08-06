package com.mohan.project.easycache.exception;

public class ReachingMaxSizeException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "EasyCache混存数量达到maxSize，无法继续添加数据！可通过调用invalidate或invalidateAll方法显式清除数据";

    public ReachingMaxSizeException() {
        super(DEFAULT_MESSAGE);
    }

    public ReachingMaxSizeException(String message) {
        super(message);
    }

    public ReachingMaxSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReachingMaxSizeException(Throwable cause) {
        super(cause);
    }

    protected ReachingMaxSizeException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}