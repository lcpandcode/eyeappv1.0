package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/8.
 */

/**
 * 全局常量类
 */
public class GlobalConst {
    //系统常量
    public static final String DATABASE_NAME = "eyeapp";
    public static final int DATABASE_VERSION = 1;
    public static final String HOST = "http://120.78.69.141:8080/eyeapp2";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    //普通提示常量
    public static final String REMIND_NET_ERROR = "网络错误！";
    public static final String SYSTEM_ERROR = "系统错误";
    public static final String REMIND_NOT_LOGIN = "您未登录，请先登录";
    public static final String REMIND_SUBMIT_SUCCESS = "提交成功";
    public static final String REMIND_BACKSTAGE_ERROR = "后台错误";
    public static final String TEST_TYPE_COLORBIND = "色盲";
    public static final String TEST_TYPE_ASTIGMATISM = "散光";
    public static final String TEST_TYPE_VISION = "明视";
    public static final String TEST_TYPE_SENSITIVITY = "敏感度";
    public static final String REMIND_REFRESH_SUCCESS = "刷新成功";
    public static final String APPLICATION_EYEDATA_TAG = "用眼数据";
    public static final String LOGIN_TO_OTHER_UI_TAG = "登录完成跳转的页面";
    public static final String PAPER_ID_ERROR = "文章id错误";

    //tab常量
    public static final String VIEW_PAPER_ID_KEY = "文章id";
    public static final String TEST_UI = "测试界面";
}
