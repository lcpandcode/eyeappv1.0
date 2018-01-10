package com.bysj.eyeapp.view;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.util.JavaBeanUtil;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by lcplcp on 2018/1/7.
 */

public class HttpUtilTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String test = HttpUtil.synGet("/eyeapp/knowledge/detail.do?id=2");
        System.out.println(JavaBeanUtil.objToJson(test));
    }

    @Test
    public void makeUrlTest(){
        Map<String,String> map = new HashMap<>();
        map.put("fuck1","ff");
        map.put("funck2","fff");
        System.out.println(HttpUtil.urlAddParam("url",map));
    }
    @Test
    public void synPostTest(){
        String path = "/eyeapp/user/login.do";
        Map<String,String> params = new HashMap<>();
        params.put("phone","15521195093");
        params.put("password","123456");
        String result;
        try {
            result = HttpUtil.synPost(path,params);
            System.out.println(result);
        } catch (HttpException e) {
            e.printStackTrace();
        }
        int a = 3;
    }
}
