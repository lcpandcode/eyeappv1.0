package com.bysj.eyeapp.view;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.KnowledgeService;
import com.bysj.eyeapp.vo.KnowledePaperVO;
import com.bysj.eyeapp.vo.KnowledgeAdvisoryQuestionVO;

import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2018/1/8.
 */

public class KnowledgeServiceTest {
    KnowledgeService service = new KnowledgeService();

    @Test
    public void advisoryQuestionTest(){
        KnowledgeAdvisoryQuestionVO questionVO = new KnowledgeAdvisoryQuestionVO();
        questionVO.setContent("contentTest");
        questionVO.setTitle("titleTest");
//        try {
//            System.out.println(service.advisorySubmitQUestion(questionVO));
//        } catch (HttpException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void getPaperByTypeTest(){
        try {
            List<KnowledePaperVO> list = service.getPaperByType(KnowledgeFragment.PAPER_TYPE_LECTURE,1,5);
            int i = 0;
        } catch (HttpException e) {
            e.printStackTrace();
        }

    }
}
