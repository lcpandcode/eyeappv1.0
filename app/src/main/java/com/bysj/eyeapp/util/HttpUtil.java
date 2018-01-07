package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/7.
 */

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

/**
 * http请求工具类，用于请求后台数据，如果网络io读取出错（即无网络或者网络超时），统一返回对应的错误信息
 */

public class HttpUtil {
    private static final String HOST = "http://120.78.69.141:8080";//远程主机地址
    public static final String ERROR_IO = "网络IO出错，请检查网络";
    public static final String ERROR_TIMEOUT = "网络超时";
    public static final int CONNECT_TIME_LIMIT = 3;//连接请求最大时间：默认为3秒为超时
    public static final int READ_TIME_LIMIT = 5;//读数据请求最大时间：默认为5秒为超时

    public static void main(String[] args){
        System.out.println("heoo");
    }
    /**
     * 同步get请求方法，用于同步查询数据
     * @param path
     * @return
     */
    // HttpGet方式请求
    public static String synGet(String path) {
        String result = null;
        try {
            String url = HOST + path ;
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIME_LIMIT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIME_LIMIT, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder().url(url).build();
            okhttp3.Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
               return response.body().string();
            } else {
                Log.i(TAG, "okHttp is request error");
                result =  ERROR_TIMEOUT;
            }
        } catch (IOException e) {
            Log.i("网络请求，请求路径：" + path + "，出错" ,e.getMessage());
            result = ERROR_IO;
        }
        return result;
    }


}
