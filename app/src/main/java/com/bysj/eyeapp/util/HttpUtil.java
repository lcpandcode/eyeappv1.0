package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/7.
 */

import android.util.Log;


import com.bysj.eyeapp.exception.HttpException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

/**
 * http请求工具类，用于请求后台数据，如果网络io读取出错（即无网络或者网络超时），统一返回对应的错误信息
 *
 */

public class HttpUtil {
    private static final String HOST = "http://120.78.69.141:8080";//远程主机地址
    public static final String ERROR_IO = "网络IO出错，请检查网络";
    public static final String ERROR_TIMEOUT = "网络超时";
    public static final int CONNECT_TIME_LIMIT = 3;//连接请求最大时间：默认为3秒为超时
    public static final int READ_TIME_LIMIT = 5;//读数据请求最大时间：默认为5秒为超时

    /**
     * 同步get请求方法，用于同步查询数据
     * @param path 路径
     * @return String:json 字符结果
     */
    public static String synGet(String path) throws HttpException {
        return synGet(path,null);
    }

    /**
     * 同步get请求方法，用于同步查询数据
     * @param path 路径
     * @param param 请求的参数
     * @return String:json 字符结果
     */
    public static String synGet(final String path,final Map<String,String> param) throws HttpException {
        final StringBuffer result = new StringBuffer();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = HOST + path ;
                    if(param!=null) {
                        url = urlAddParam(url,param);
                    }
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIME_LIMIT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIME_LIMIT, TimeUnit.SECONDS)
                            .build();
                    Request request = new Request.Builder().url(url).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        result.append(response.body().string());
                    } else {
                        Log.i(TAG, "okHttp 请求超时");
                    }
                } catch (IOException e) {
                    Log.i("网络请求，请求路径：" + path + "，出错" ,e.getMessage());
                }
            }
        });
        thread.start();
        //等待线程请求完毕
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if("".equals(result.toString())){
            throw new HttpException(ERROR_IO);
        }
        return result.toString();
    }


    /**
     * 同步的post方法
     * @param path 路径
     * @param param 参数
     * @return map:代表结果信息，主要有两个字段，status：success-fail;info:失败的原因，成功则无该字段；data：json字符
     */
    public static String synPost(final String path,final Map<String,String> param) throws HttpException {
        final StringBuffer result = new StringBuffer();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = HOST + path ;
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIME_LIMIT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIME_LIMIT, TimeUnit.SECONDS)
                            .build();
                    FormBody.Builder builder = new FormBody.Builder();
                    //添加参数
                    for(Map.Entry<String,String> entry : param.entrySet()){
                        builder.add(entry.getKey(),entry.getValue());
                    }
                    FormBody body = builder.build();
                    Request request = new Request
                            .Builder()
                            .url(url)
                            .post(body)
                            .build();
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        result.append(response.body().string());
                    } else {
                        Log.i(TAG, "okHttp 请求错误，网络有误");
                    }
                } catch (IOException e) {
                    Log.i("网络请求，请求路径：" + path + "，出错" ,e.getMessage());
                }
            }
        });
        thread.start();
        //等待线程请求完毕
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if("".equals(result.toString())){
            throw new HttpException(ERROR_IO);
        }
        return result.toString();

    }

    /**
     * get请求中，url加上参数的方法
     * @param path 路径
     * @param param 参数
     * @return
     */
    public static String urlAddParam(String path,Map<String,String> param){
        StringBuffer sb = new StringBuffer(path);
        //迭代map
        sb.append("?");
        boolean sign = true;
        for(Map.Entry<String,String> entry : param.entrySet()){
            if(sign){
                sign = false;
            }else {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",entry.getKey(),entry.getValue()));
        }
        return sb.toString();
    }


}
