package com.bysj.eyeapp.exception;

import com.alibaba.fastjson.JSONException;

/**
 * Created by lcplcp on 2018/1/8.
 */

/**
 * json解析异常
 */
public class JavaBeanException extends JSONException {
    public JavaBeanException(String message){
        super(message);
    }
}
