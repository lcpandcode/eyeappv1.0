package com.bysj.eyeapp.view;

import com.bysj.eyeapp.util.JavaBeanUtil;

import org.junit.Test;

import java.util.Map;

/**
 * Created by lcplcp on 2018/1/8.
 */

public class JavaBeanUtilTest {
    @Test
    public void jsonToObj(){
        String json = "{\"status\":0,\"data\":{\"id\":2,\"title\":\"著名专家梁灿培将来华师开展讲座\",\"content\":\"讲座很水\",\"viewCount\":1020,\"date\":1514340761000,\"type\":\"护眼讲座\"}}";

        Map<String ,Object> map = JavaBeanUtil.jsonToObj(json);
        System.out.println(map.get("status"));
        System.out.println(((Map<String,String>)map.get("data")).get("title"));
    }
}
