package com.mohan.project.easycache.exception;

/**
 * 调用回调函数获取value时发生异常
 * @author mohan
 * @date 2018-08-05 22:43:38
 */
public class GetValueByCallableException extends Exception{

    private static final String DEFAULT_MESSAGE = "通过Callable产生缓存时发生异常";

    public GetValueByCallableException() {
        super(DEFAULT_MESSAGE);
    }

    public GetValueByCallableException(String message) {
        super(message);
    }

    public GetValueByCallableException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetValueByCallableException(Throwable cause) {
        super(cause);
    }

    protected GetValueByCallableException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}