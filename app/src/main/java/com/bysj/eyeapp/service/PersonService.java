package com.bysj.eyeapp.service;

import com.bysj.eyeapp.vo.ExpertCommunicationVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcplcp on 2018/2/3.
 */

public class PersonService {

    /**
     *
     * @param limit 本次请求的数据个数
     * @param page 第几页
     * @return 专家列表
     */
    public List<ExpertCommunicationVO> getExpertCommunicationList(int limit,int page){
        List<ExpertCommunicationVO> experts = new ArrayList<>();
        for(int i = 0;i<10;i++){
            ExpertCommunicationVO expert = new ExpertCommunicationVO();
            expert.setDate("2015-02-02 08:20");
            expert.setMessage("消息" + i + ":多喝热水");
            expert.setName("专家" + i);
            expert.setId(i);
            experts.add(expert);
        }
        return experts;
    }
}
