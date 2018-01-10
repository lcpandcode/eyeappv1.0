package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2017/12/27.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则判断（手机号是否正确，姓名输入是否合法等）工具类，该类下，所有方法均为static
 */
public class RegularUtil {
    private final static int PHONE_LENGTH = 11;
    private final static String NICK_NAME_ERROR_LENGTH = "昵称过长";

//    public static void main(String [] args){
//        String strTest = "15521228888";
//        System.out.println(phoneIsTrue(strTest));
//    }

    /**
     * 判断手机号码是否合法的正则方法
     */
    public static boolean phoneIsTrue(String phone){
        if(phone.length()!=PHONE_LENGTH){
            return false;
        }
        String regx = "/^1[3|4|5|7|8][0-9]{9}$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(phone);
         return matcher.find();
    }
    /**
     * 昵称是否合法判断，非法返回非法信息
     */
//    public static String nickNameIsTrue(String nickName){
//        if(nickName.length()>6){
//            return NICK_NAME_ERROR_LENGTH;
//        }
//    }
    /**
     * 验证码是否合法
     */
    public static boolean checkCodeIsTrue(String checkCode){
        String regx = "^[1-9]\\d*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(checkCode);
        return matcher.find();
    }

    /**
     * 判断是否是数字
     */
    public static boolean numberIsTrue(String number){
        String regx = "^[1-9]\\d*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(number);
        return matcher.find();
    }

    /**
     * 判断字符是否为空
     */
    public static boolean strIsEmpty(String str){
        if(str==null || "".equals(str)){
            return true;
        }
        //其他类似于空格也要过滤，这里待扩展
        return false;
    }
}
