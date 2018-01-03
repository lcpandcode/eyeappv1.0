package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2017/12/29.
 */

import com.bysj.eyeapp.vo.TestVisionQuestionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 散光测试核心服务逻辑类，该类封装了散光测试相关的核心代码：包括提交本地数据库代码
 * 请求服务端以及数据组装等核心业务逻辑功能
 */
public class TestVisionService {

    /**
     * 获取num个问题并返回问题列表
     * @param num ：需要获取问题的个数
     * @return 问题列表
     */
    public List<TestVisionQuestionVO> getVisionQustion(int num){
        //代码待完善，测试需要直接返回
        List<TestVisionQuestionVO> questions = new ArrayList<>();
        TestVisionQuestionVO q1 = new TestVisionQuestionVO();
        q1.setImgUrl("@mipmap/test_btn_vision");
        q1.setTrueOption(3);
        questions.add(q1);
        TestVisionQuestionVO q2 = new TestVisionQuestionVO();
        q2.setImgUrl("@mipmap/test_btn_vision");

        q2.setTrueOption(3);
        questions.add(q2);
        return questions;
    }
}
