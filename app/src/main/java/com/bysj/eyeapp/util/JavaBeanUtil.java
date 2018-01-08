package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/5.
 */


import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bysj.eyeapp.exception.JavaBeanException;

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
    public static Map objToMap(Object javaBean) throws JavaBeanException{
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
                throw new JavaBeanException("java bean 定义不合法，无发转换，只有java bean对象合法定义了getter和setter方法才可进行转换！");
            }

        }
        return result;
    }

    /**
     * 解析json的方法：如果结果非法，返回null
     * @param json json字符
     * @return 解析的obj
     */
    public static Object jsonToObj(String json) throws JavaBeanException{
        Object obj = null;
        try {
            obj =  JSON.parse(json);
        } catch (JSONException e) {
            Log.e("Json解析错误：" ,e.getMessage());
            throw new JavaBeanException("Json解析出错,json无法转换为obj！");
        }
        return obj;
    }

    /**
     * map转换为json字符
     * @param object object对象：该对象是map<String>,list<String>等字符集合类或者是可序列化的java bean，
     * @return json字符
     */

    public static String objToJson(Object object){
        return JSONObject.toJSONString(object);
    }



}
