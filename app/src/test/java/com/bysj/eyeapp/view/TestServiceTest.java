package com.bysj.eyeapp.view;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.PersonService;
import com.bysj.eyeapp.service.TestService;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.vo.KnowledgeAdvisoryQuestionVO;
import com.bysj.eyeapp.vo.TestAstigmatismQuestionVO;
import com.bysj.eyeapp.vo.TestQuestionVO;
import com.bysj.eyeapp.vo.TestResultVO;
import com.bysj.eyeapp.vo.TestVisionQuestionVO;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2018/1/7.
 */

public class TestServiceTest {
    @Test
    public void testSbmitQestionTest()  {

    }

    @Test
    public void testGetDataQestionTest()  {
        TestService service = new TestService();
        try {
            List<TestQuestionVO> questions = service.getTestQuestions(2,"散光");
            TestQuestionVO vo = questions.get(0);
            System.out.println(vo.getId());
            int a = 0;
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void submitTestResultTest(){
        new PersonServiceTest().init();
        new HttpUtilTest().synPostTest();
        TestService service = new TestService();
        TestResultVO resultVO = new TestResultVO();
        resultVO.setCorrectRate(50);
        resultVO.setTestResult("色盲33");
        resultVO.setType(GlobalConst.TEST_TYPE_COLORBIND);
        resultVO.setEye("");
        try {
            service.submitTestResult(resultVO);
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getVisionQuestions(){
        TestService service = new TestService();
        List<TestVisionQuestionVO> questions = service.getVisionQuestions(500,1);
        int a;
    }


    @Test
    public void testGetResult() throws HttpException {
       new PersonServiceTest().init();
       TestService service = new TestService();
       service.getTestResult(1,5);

    }

}
