package com.bysj.eyeapp.view;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.TestService;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.vo.KnowledgeAdvisoryQuestionVO;
import com.bysj.eyeapp.vo.TestAstigmatismQuestionVO;
import com.bysj.eyeapp.vo.TestQuestionVO;

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


}
