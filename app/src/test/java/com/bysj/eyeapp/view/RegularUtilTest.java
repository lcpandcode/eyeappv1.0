package com.bysj.eyeapp.view;

import com.bysj.eyeapp.util.RegularUtil;

import org.junit.Test;

/**
 * Created by lcplcp on 2018/1/24.
 */


public class RegularUtilTest {

    @Test
    public void isNumTest(){
        System.out.println(RegularUtil.numberIsTrue("1.22"));
        System.out.println(RegularUtil.numberIsTrue("1"));
        System.out.println(RegularUtil.numberIsTrue("-2"));
        System.out.println(RegularUtil.numberIsTrue("200000000000000000000"));
        System.out.println(RegularUtil.numberIsTrue("-1.2"));
    }
}
