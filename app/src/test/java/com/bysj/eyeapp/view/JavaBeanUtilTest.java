package com.bysj.eyeapp.view;

import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.vo.KnowledgeAdvisoryQuestionVO;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2018/1/8.
 */

public class JavaBeanUtilTest {
    @Test
    public void jsonToObj(){
        //String json = "{\"status\":0,\"data\":{\"id\":2,\"title\":\"著名专家梁灿培将来华师开展讲座\",\"content\":\"讲座很水\",\"viewCount\":1020,\"date\":1514340761000,\"type\":\"护眼讲座\"}}";
        String json = "[{\"id\":2,\"title\":\"著名专家梁灿培将来华师开展讲座\",\"content\":\"讲座很水\",\"viewCount\":1020,\"date\":1514340761000,\"type\":\"护眼讲座\"},{\"id\":3,\"title\":\"著名专家魏启生将来华师开展讲座\",\"content\":\"讲座水\",\"viewCount\":1030,\"date\":1513131200000,\"type\":\"公益讲座\"},{\"id\":1,\"title\":\"著名专家李彦鹏将来华师开展讲座\",\"content\":\"讲座有点水\",\"viewCount\":1000,\"date\":1493913600000,\"type\":\"护眼讲座\"}]";
       List<Map<String,String>> obj = (List<Map<String,String>>) JavaBeanUtil.jsonToObj(json);
        //System.out.println(map.get("status"));
        System.out.println();
    }

    @Test
    public void mapToJson(){
        Map<String,String> map = new HashMap<>();
        map.put("t1","ttt");
        map.put("t2","ttttt");
        System.out.println("t1:" + JavaBeanUtil.objToJson(map));
        KnowledgeAdvisoryQuestionVO questionVO = new KnowledgeAdvisoryQuestionVO();
        questionVO.setContent("contentTest");
        questionVO.setTitle("titleTest");
        System.out.println("t2:" + JavaBeanUtil.objToJson(questionVO));
    }
}
