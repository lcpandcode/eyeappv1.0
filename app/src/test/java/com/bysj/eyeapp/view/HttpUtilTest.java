package com.bysj.eyeapp.view;

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
}
