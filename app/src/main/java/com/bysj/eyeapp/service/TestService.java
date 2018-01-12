package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2017/12/29.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bysj.eyeapp.exception.BackstageException;
import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.util.TestSensitivityUtil;
import com.bysj.eyeapp.vo.TestAstigmatismQuestionVO;
import com.bysj.eyeapp.vo.TestColorbindQuestionVO;
import com.bysj.eyeapp.vo.TestQuestionVO;
import com.bysj.eyeapp.vo.TestSensitivityQuestionVO;
import com.bysj.eyeapp.vo.TestVisionQuestionVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 散光测试核心服务逻辑类，该类封装了散光测试相关的核心代码：包括提交本地数据库代码
 * 请求服务端以及数据组装等核心业务逻辑功能
 */
public class TestService {
    private static final String GET_QUESTION_PATH = "/eyetest/randomquestion.do";

    /**
     * 获取num个问题并返回问题列表
     * @param num ：需要获取问题的个数
     * @return 问题列表
     */
    public List<TestQuestionVO> getTestQuestions(int num, String type) throws HttpException {
        if(num<1){
            num = 1;
        }
        if(type==null){
            throw new RuntimeException("type ：类型不能为空");
        }
        if(GlobalConst.TEST_TYPE_VISION.equals(type)){
            //调用生成视力测试题目的方法。待完善

        }

        //代码待完善，测试需要直接返回
        List<TestQuestionVO> questions = new ArrayList<>();
        Map<String,String> params = new HashMap<>();
        params.put("type",type);
        params.put("questionsize",num + "");
        String result = HttpUtil.synGet(GET_QUESTION_PATH,params);
        Map<String,Object> resultMap = (Map<String,Object>)JavaBeanUtil.jsonToObj(result);
        int status = (Integer) resultMap.get("status");
        if(status==10){
            throw new UserException(GlobalConst.REMIND_NOT_LOGIN);
        }
        if(status==1){
            throw new BackstageException(GlobalConst.REMIND_BACKSTAGE_ERROR);
        }
        List<Map<String,Object>> data = (List<Map<String,Object>>)resultMap.get("data");
        for(Map<String,Object> m : data){
            questions.add(mapToQuestion(m));
        }
        return questions;
    }



    /**
     * 获取num个问题并返回问题列表
     * @param num ：需要获取问题的个数
     * @return 问题列表
     */
    public List<TestSensitivityQuestionVO> getSensitivityQuestions(int num){
        //代码待完善，测试需要直接返回
        return TestSensitivityUtil.createQuestions(2,50);
    }

    /**
     * 默认获取问题列表方法，一般客户端直接调用该方法获取问题列表即可
     * @return 默认问题列表
     */
    public List<TestSensitivityQuestionVO> getDefaultSensitivityQuestions(){
        return TestSensitivityUtil.defaultCreateQuestions();
    }

    /**
     * 获取num个问题并返回问题列表
     * @param num ：需要获取问题的个数
     * @return 问题列表
     */
    public List<TestVisionQuestionVO> getVisionQuestions(int num){
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

    private TestQuestionVO mapToQuestion(Map<String,Object> map){
        TestQuestionVO question = new TestQuestionVO();
        question.setId((Integer)map.get("id"));
        question.setImgUrl((String)map.get("imgUrl"));
        question.setCorrectOption((Integer)map.get("correctOption"));
        question.setOption1((String)map.get("option1"));
        question.setOption2((String)map.get("option2"));
        question.setOption3((String)map.get("option3"));
        question.setOption4((String)map.get("option4"));
        question.setTitle((String)map.get("title"));
        question.setCorrectOption((Integer) map.get("correctOption"));
        return question;
    }

    /**
     * 获取num个问题并返回问题列表
     * @param num ：需要获取问题的个数
     * @return 问题列表
     */
//    public List<TestQuestionVO> getVisionQuestions(int num){
//        //代码待完善，测试需要直接返回
//        List<TestQuestionVO> questions = new ArrayList<>();
//
//        return questions;
//    }
}
