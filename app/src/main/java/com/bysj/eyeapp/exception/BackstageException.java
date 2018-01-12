package com.bysj.eyeapp.exception;

/**
 * Created by lcplcp on 2018/1/10.
 */

/**
 * 后台错异常类
 */
public class BackstageException extends RuntimeException {
    public BackstageException(String message){
        super(message);
    }
}
