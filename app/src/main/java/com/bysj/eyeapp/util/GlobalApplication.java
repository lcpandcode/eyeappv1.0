package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/10.
 */

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局Application，用于存储全局性的信息：包括用户本身的相关的信息
 */
public class GlobalApplication extends Application {
    //全局map容器
    Map<String,Object> globalMap ;

    public GlobalApplication(){
        super();
        globalMap = new HashMap<>();
    }

    public void putGlobalVar (String tag,Object var){
        globalMap.put(tag,var);
    }

    public void removeGlobalVar(String tag){
        globalMap.remove(tag);
    }
}
