package com.bysj.eyeapp.view;

import com.bysj.eyeapp.util.HttpUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by lcplcp on 2018/1/7.
 */

public class HttpUtilTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String test = HttpUtil.synGet("/eyeapp/knowledge/detail.do?id=2");
        System.out.println(test);
    }
}
