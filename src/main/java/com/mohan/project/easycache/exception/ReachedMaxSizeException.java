package com.mohan.project.easycache.exception;

public class ReachedMaxSizeException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "EasyCache混存数量达到maxSize，无法继续添加数据！可通过调用invalidate或invalidateAll方法显式清除数据";

    public ReachedMaxSizeException() {
        super(DEFAULT_MESSAGE);
    }

    public ReachedMaxSizeException(String message) {
        super(message);
    }

    public ReachedMaxSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReachedMaxSizeException(Throwable cause) {
        super(cause);
    }

    protected ReachedMaxSizeException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}