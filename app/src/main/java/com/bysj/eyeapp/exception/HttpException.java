package com.bysj.eyeapp.exception;

/**
 * Created by lcplcp on 2018/1/8.
 */

import java.io.IOException;

/**
 * 网络请求异常对象
 */
public class HttpException extends IOException {
    //无其他代码，仅仅标记该异常用，以备扩展
    public  HttpException(String message){
        super(message);
    }
}
