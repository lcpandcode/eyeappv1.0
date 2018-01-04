package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2017/12/29.
 */

import com.bysj.eyeapp.util.TestSensitivityUtil;
import com.bysj.eyeapp.vo.TestSensitivityQuestionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 散光测试核心服务逻辑类，该类封装了散光测试相关的核心代码：包括提交本地数据库代码
 * 请求服务端以及数据组装等核心业务逻辑功能
 */
public class TestSensitivityService {

    /**
     * 获取num个问题并返回问题列表
     * @param num ：需要获取问题的个数
     * @return 问题列表
     */
    public List<TestSensitivityQuestionVO> getSensitivityQustion(int num){
        //代码待完善，测试需要直接返回
        return TestSensitivityUtil.createQuestions(2,50);
    }

    /**
     * 默认获取问题列表方法，一般客户端直接调用该方法获取问题列表即可
     * @return 默认问题列表
     */
    public List<TestSensitivityQuestionVO> getDefaultSensitivityQustions(){
        return TestSensitivityUtil.defaultCreateQuestions();
    }

}
