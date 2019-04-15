package com.dlong.rep.dlsimpleweathermanager.exception;

/**
 * 自定义异常消息
 * @author  dlong
 * created at 2019/4/15 11:54 AM
 */
public class DLSimpleWeatherException extends RuntimeException{

    public DLSimpleWeatherException(String detailMessage) {
        super(detailMessage);
    }

    public DLSimpleWeatherException(Throwable throwable) {
        super(throwable);
    }

    public DLSimpleWeatherException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
