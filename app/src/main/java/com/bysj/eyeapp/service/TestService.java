package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2017/12/29.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bysj.eyeapp.exception.BackstageException;
import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.SystemException;
import com.bysj.eyeapp.exception.TestException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.util.TestSensitivityUtil;
import com.bysj.eyeapp.util.TestVisionUtil;
import com.bysj.eyeapp.vo.TestAstigmatismQuestionVO;
import com.bysj.eyeapp.vo.TestColorbindQuestionVO;
import com.bysj.eyeapp.vo.TestQuestionVO;
import com.bysj.eyeapp.vo.TestResultVO;
import com.bysj.eyeapp.vo.TestSensitivityQuestionVO;
import com.bysj.eyeapp.vo.TestVisionQuestionVO;

import java.text.SimpleDateFormat;
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
    private static final String SUBMIT_TEST_RESULT = "/eyetest/submitquestion.do";
    private static final String GET_TEST_RESULT = "/eyetest/gettest.do";

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

    public List<TestColorbindQuestionVO> getColorBindTestQuestions(int num, String type) throws HttpException {
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
        List<TestColorbindQuestionVO> questions = new ArrayList<>();
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
            questions.add(mapToTestColorBindQuestion(m));
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
     * @param distance ：视力测试距离
     * @param num ：每个视力值对应的图标个数
     * @return 问题列表
     */
    public List<TestVisionQuestionVO> getVisionQuestions(float distance,int num){
        //代码待完善，测试需要直接返回
        return TestVisionUtil.getTestVisionQuestionVOs(distance,num);
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

    private TestColorbindQuestionVO mapToTestColorBindQuestion(Map<String,Object> map){
        TestColorbindQuestionVO question = new TestColorbindQuestionVO();
        question.setId((Integer)map.get("id"));
        question.setImgUrl((String)map.get("imgUrl"));
        question.setCorrectOption((Integer)map.get("correctOption"));
        //分割option中的说明文字
        String [] op1 = ((String)map.get("option1")).split("\\|");
        String [] op2 = ((String)map.get("option2")).split("\\|");
        String [] op3 = ((String)map.get("option3")).split("\\|");
        String [] op4 = ((String)map.get("option4")).split("\\|");
        question.setOption1(op1[0]);
        question.setOption2(op2[0]);
        question.setOption3(op3[0]);
        question.setOption4(op4[0]);

        question.setOption1Detail(op1[1]);
        question.setOption2Detail(op2[1]);
        question.setOption3Detail(op3[1]);
        question.setOption4Detail(op4[1]);

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


    /**
     * 提交测试结果，提交失败抛异常,异常中包含相应的失败原因信息
     * @param testResult 测试结果对象
     */
    public void submitTestResult(TestResultVO testResult) throws HttpException {
        Map<String,String> params = new HashMap<>();
        params.put("correctRate",testResult.getCorrectRate() + "");
        params.put("eye",testResult.getEye());
        params.put("testResult",testResult.getTestResult() + "");
        params.put("eye",testResult.getEye());
        params.put("type",testResult.getType());
        String result = HttpUtil.synPost(SUBMIT_TEST_RESULT,params);
        Map<String,Object> resultMap = (Map<String,Object>)JavaBeanUtil.jsonToObj(result);
        Integer status = (Integer)resultMap.get("status");
        if(status==1){
            //失败，抛异常并设置失败相关信息
            throw new UserException(GlobalConst.REMIND_NOT_LOGIN);
        }else if(status!=0){
            throw new TestException(GlobalConst.REMIND_BACKSTAGE_ERROR);
        }
    }

    public List<TestResultVO> getTestResult(int page,int limit) throws HttpException {
        if( page <=0 || limit<=0){
            throw new SystemException("参数错误");
        }
        Map<String,String> params = new HashMap<>();
        params.put("pageNum",page+"");
        params.put("pageSize",limit + "");
        String result = HttpUtil.synGet(GET_TEST_RESULT,params);
        Map<String,Object> resultMap = (Map<String,Object>)JavaBeanUtil.jsonToObj(result);
        Integer status = (Integer)resultMap.get("status");
        if(status==1){
            //失败，抛异常并设置失败相关信息
            throw new UserException(GlobalConst.REMIND_NOT_LOGIN);
        }
        Map<String,Object> dataMap = (Map<String,Object>)resultMap.get("data");
        List<Map<String,Object>> dataList = (List<Map<String,Object>>)dataMap.get("list") ;
        List<TestResultVO> list = new ArrayList<>();
        for(Map<String,Object> map : dataList){
            TestResultVO vo = mapToTestResultVO(map);
            list.add(vo);
        }
        return list;
    }

    private TestResultVO mapToTestResultVO(Map<String,Object> map){
        TestResultVO result = new TestResultVO();
        result.setTestResult(map.get("testResult").toString());
        result.setCorrectRate((Integer) map.get("correctRate"));
        result.setType((String)map.get("type"));
        String date = new SimpleDateFormat(GlobalConst.DATE_PATTERN).format(map.get("date"));
        result.setDate(date);
        result.setEye((String)map.get("eye"));
        result.setId((Integer)map.get("id"));
        return result;
    }


}
