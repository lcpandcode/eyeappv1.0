package com.bysj.eyeapp.service;

import android.app.Application;
import android.content.Intent;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.MessageException;
import com.bysj.eyeapp.exception.PersonException;
import com.bysj.eyeapp.exception.SystemException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.ExpertCommunicationVO;
import com.bysj.eyeapp.vo.MessageVO;
import com.bysj.eyeapp.vo.PersonTrainVO;
import com.bysj.eyeapp.vo.TestResultVO;
import com.bysj.eyeapp.vo.UserVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2018/2/3.
 */

public class PersonService {

    private static final String EXPERT_LIST_PATH = "/message/specialist.do";
    private static final String MSG_LIST_PATH = "/message/list.do";
    private static final String MSG_READ = "/message/readMessage.do";
    private static final String MSG_SEND = "/message/sendmessage.do";
    private static final String TODAY = GlobalConst.TODAY;
    private static final String YESTERDAY = GlobalConst.YESTERDAY;
    private static final String MONTH = GlobalConst.MONTH;
    private static final String DAY = GlobalConst.DAY;

    //数据变量
    public static int MAX_PAGE_EXPERT_COMMUNICATION_LIST = 0;//专家交流记录最大页数,每次请求都更新该数据
    //聊天记录记录最大页数,是一个map，对应关系：专家id-该专家聊天记录最大页数。每次请求都更新该数据
    public static Map<Integer,Integer> MAX_PAGE_EXPERT_MSG = new HashMap<>();
    private GlobalApplication application;
    private UserVO user;//用户相关数据对象
    public PersonService(UserVO user){
        this.user = user;
    }


    /**
     * 查看交流过的专家列表
     * @param limit 本次请求的数据个数
     * @param page 第几页
     * @return 专家列表
     */
    public List<ExpertCommunicationVO> getExpertCommunicationList(int limit,int page) throws HttpException {
        if(page<=0 || limit<=0){
            limit = GlobalConst.LIMIT;
            page = 1;
        }
        if( MAX_PAGE_EXPERT_COMMUNICATION_LIST>page){
            throw new SystemException(GlobalConst.OUT_OF_PAGE);
        }
        List<ExpertCommunicationVO> experts = new ArrayList<>();
        Map<String,String> params = new HashMap<>();
        params.put("pageNum",page + "");
        params.put("pageSize",limit + "");
        String result = HttpUtil.synGet(EXPERT_LIST_PATH,params);
        Map<String,Object> resultMap = (Map<String,Object>) JavaBeanUtil.jsonToObj(result);
        int status = (Integer)resultMap.get("status");
        if(status==10 || status==1){
            //未登录，提示登录
            throw new UserException(GlobalConst.REMIND_NOT_LOGIN);
        }
        if(status!=0){
            throw new PersonException(GlobalConst.SYSTEM_ERROR);
        }
        Map<String,Object> data = (Map<String,Object>)resultMap.get("data");
        //更新最大页码
        MAX_PAGE_EXPERT_COMMUNICATION_LIST = (Integer) data.get("lastPage");
        //获取列表消息
        List<Map<String,Object>> lists = (List<Map<String,Object>>)data.get("list");
        for(Map<String,Object> i : lists){
            ExpertCommunicationVO expert = new ExpertCommunicationVO();
            expert.setUnReadCount((Integer) i.get("count"));
            expert.setName((String)i.get("name"));
            expert.setMessage((String) i.get("message"));
            long date = (Long) i.get("date");
            expert.setDate(dateFormat(date));
            expert.setId((Integer) i.get("expertId"));
            experts.add(expert);
        }
        return experts;
    }

    /**
     * long转换为date字符
     * @param d
     * @return
     */
    private String dateFormat(long d){
        Date date = new Date(d);
        String dateStr = new SimpleDateFormat(GlobalConst.DATETIME_PATTERN).format(date);
        Date now = new Date();
        int day = now.getDay() - date.getDay();
        String time = date.getHours() + ":" + date.getMinutes();
        if(day==0){
            return TODAY + time;
        }else if(day==1){
            return YESTERDAY + time;
        } else {
            return dateStr;
        }
    }

    /**
     * 根据专家id获取某个专家的详细聊天记录
     * @param expertId 专家id
     * @param limit 记录条数
     * @param page 页数
     * @return 专家交流详细记录
     */
    public List<MessageVO> getMessageList(int expertId, int limit, int page) throws HttpException {
        if(page<=0 || limit<=0){
            limit = GlobalConst.LIMIT;
            page = 1;
        }
        if(MAX_PAGE_EXPERT_MSG.get(expertId)!=null && MAX_PAGE_EXPERT_MSG.get(expertId)>page){
            throw new SystemException(GlobalConst.OUT_OF_PAGE);
        }
        List<MessageVO> msgs = new ArrayList<>();
        Map<String,String> params = new HashMap<>();
        params.put("pageNum",String.valueOf(page));
        params.put("pageSize",String.valueOf(limit));
        params.put("interviewerId",String.valueOf(expertId));
        String result = HttpUtil.synGet(MSG_LIST_PATH,params);
        Map<String,Object> resultMap = (Map<String,Object>)JavaBeanUtil.jsonToObj(result);
        int status = (Integer) resultMap.get("status");
        if(status!=0){
            throw new UserException(GlobalConst.REMIND_NOT_LOGIN);
        }


        Map<String,Object> data = (Map<String,Object>) resultMap.get("data");
        int lastPage = (Integer) data.get("lastPage");
        MAX_PAGE_EXPERT_MSG.put(expertId,lastPage);//更新最大聊天记录
        //获取、组装消息
        List<Map<String,Object>> list = (List<Map<String,Object>>)data.get("list");
        UserVO expert = null;
        for (Map<String,Object> map : list){
            MessageVO msg = new MessageVO();
            //查看是否是对方发给你消息
            Map<String,Object> fromMap = (Map<String,Object>) map.get("from");
            int fromId = (Integer)fromMap.get("id");
            msg.setCome(fromId==user.getId()?false:true);
            //查看是否已读
            String isRead = (String) map.get("is_read") ;
            msg.setRead("否".equals(isRead)?false:true);

            msg.setContent((String)map.get("message"));
            msg.setDate((Long) map.get("date"));
            msgs.add(msg);
        }
        return msgs;
    }

    /**
     * 查看新消息列表
     * @param limit
     * @param page
     * @return
     */
    public List<ExpertCommunicationVO> getExpertCommunicationNewMsgList(int limit,int page){
        List<ExpertCommunicationVO> experts = new ArrayList<>();
        for(int i = 0;i<10;i++){
            ExpertCommunicationVO expert = new ExpertCommunicationVO();
            expert.setDate("2015-02-02 08:20");
            expert.setMessage("消息" + i + ":多喝热水");
            expert.setName("专家" + i);
            expert.setUnReadCount(i);
            expert.setId(i);
            experts.add(expert);
        }
        return experts;
    }

    /**
     * 读取未读的消息
     * @param expertId 专家id
     */
    public void readMessage(int expertId) throws HttpException {
        Map<String,String> params = new HashMap<>();
        params.put("expertId",String.valueOf(expertId));
        String result = HttpUtil.synPost(MSG_READ,params);
        Map<String,Object> resultMap = (Map<String,Object>) JavaBeanUtil.jsonToObj(result);
        int status = (Integer) resultMap.get("status");
        if(status==10){
            throw new UserException(GlobalConst.REMIND_NOT_LOGIN);
        }
        if(status!=0){
            throw new SystemException("消息状态更新失败" + GlobalConst.SYSTEM_ERROR);
        }
    }

    /**
     * 发送消息方法
     * @param toId 发送给谁
     */
    public void sendMessage(int toId,String message) throws HttpException {
        Map<String,String> params = new HashMap<>();
        params.put("fromId",toId + "");
        params.put("message",message);
        String result = HttpUtil.synPost(MSG_SEND,params);
        Map<String,Object> resultMap = (Map<String,Object>)JavaBeanUtil.jsonToObj(result);
        int status = (Integer) resultMap.get("status");
        if(status!=0){
           throw new MessageException(GlobalConst.SYSTEM_ERROR + "," +GlobalConst.MESSAGE_SEND_FAIL);
        }
    }

    public List<PersonTrainVO> getTrainList(){
        List<PersonTrainVO> trains = new ArrayList<>();
        for(int i=1;i<11;i++){
            PersonTrainVO t1 = new PersonTrainVO();
            t1.setTrainDate("2017-08-08 08:20");
            if(i%4==1){
                t1.setType("色盲");
            }else if(i%4==2){
                t1.setType("散光");
            }else if(i%4==3){
                t1.setType("敏感度");
            }else {
                t1.setType("视力");
            }
            t1.setTrainId(1);
            trains.add(t1);
        }
        return trains;
    }

    /**
     * 查询测试记录详情
     * @param id 测试记录id
     * @return
     */
    public TestResultVO getTrainDetail(int id){
        TestResultVO result = new TestResultVO();
        result.setEye("左");
        result.setType("色盲");
        result.setCorrectRate(20);
        result.setTestResult(50.2 + "");
        return result;
    }

}
