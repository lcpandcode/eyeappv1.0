package com.bysj.eyeapp.exception;

/**
 * Created by lcplcp on 2018/2/9.
 */

/**
 * 系统异常：主要是参数问题：例如请求页码超过最大页码，id传递错误等等
 */
public class SystemException extends RuntimeException{
    public SystemException(String msg){
        super(msg);
    }
}
