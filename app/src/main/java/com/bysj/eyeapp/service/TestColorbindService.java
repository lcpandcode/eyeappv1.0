package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2017/12/29.
 */

import com.bysj.eyeapp.vo.TestColorbindQuestionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 色盲测试核心服务逻辑类，该类封装了色盲测试相关的核心代码：包括提交本地数据库代码
 * 请求服务端以及数据组装等核心业务逻辑功能
 */
public class TestColorbindService {

    /**
     * 获取num个问题并返回问题列表
     * @param num ：需要获取问题的个数
     * @return 问题列表
     */
    public List<TestColorbindQuestionVO> getColorbindQustion(int num){
        //代码待完善，测试需要直接返回
        List<TestColorbindQuestionVO> questions = new ArrayList<>();
        TestColorbindQuestionVO q1 = new TestColorbindQuestionVO();
        q1.setImgUrl("@mipmap/colorbind_demo");
        q1.setOption1("数字1");
        q1.setOption2("数字2");
        q1.setOption3("数字74");
        q1.setOption4("数字4");
        q1.setTrueOption(3);
        questions.add(q1);
        TestColorbindQuestionVO q2 = new TestColorbindQuestionVO();
        q2.setImgUrl("@mipmap/colorbind_demo");
        q2.setOption1("数字8");
        q2.setOption2("数字8");
        q2.setOption3("数字74");
        q2.setOption4("数字8");
        q2.setTrueOption(3);
        questions.add(q2);
        return questions;
    }
}
