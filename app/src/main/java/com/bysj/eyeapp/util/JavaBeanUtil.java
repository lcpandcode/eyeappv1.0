package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/5.
 */


import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * java bean相关工具类，主要处理java bean相关的序列化、反序列话、obj转map、obj转list等java bean相关问题
 */
public class JavaBeanUtil {
    /**
     * 把java bean转换为map的方法
     * @param javaBean bean对象，该对象必须定义了合法的setter和getter方法
     * @return map结果
     */
    public static Map ObjtoMap(Object javaBean) {

        Map result = new HashMap();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {

            try {

                if (method.getName().startsWith("get")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());

                }

            } catch (Exception e) {
                throw new RuntimeException("java bean 定义不合法，无发转换，只有java bean对象合法定义了getter和setter方法才可进行转换！");
            }

        }
        return result;
    }

    /**
     * 解析json的方法：如果结果非法，返回null
     * @param json json字符
     * @return 解析的map:包含两部分数据，status和data，分别是一个string和一个Object(这个Object其实也是个Map)
     */
    public static Map<String,Object> jsonToObj(String json){
        Map<String,Object> map = null;
        try {
            map =  (Map<String, Object>) JSON.parse(json);
        } catch (JSONException e) {
            Log.e("Json解析错误：" ,e.getMessage());
            return null;
        }
        return map;
    }



}
