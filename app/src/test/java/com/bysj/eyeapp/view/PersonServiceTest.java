package com.bysj.eyeapp.view;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.PersonService;
import com.bysj.eyeapp.service.UserService;
import com.bysj.eyeapp.vo.ExpertCommunicationVO;
import com.bysj.eyeapp.vo.MessageVO;
import com.bysj.eyeapp.vo.UserVO;

import org.junit.Test;

import java.util.List;

/**
 * Created by lcplcp on 2018/2/7.
 */

public class PersonServiceTest {
    UserVO user ;
    PersonService service = null;
    public void init(){
        try {
            user = new UserService().login("15521228172","123456");
            service = new PersonService(user);
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getExpertCommunicationListTest(){
        try {
            init();
            List<ExpertCommunicationVO> list = service.getExpertCommunicationList(10,1);
            int a = 0;
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getMessageListTest(){
        try {
            init();
            List<MessageVO> list = service.getMessageList(1,10,1);
            int a = 0;
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void readMessageTest(){
        try {
            init();
            service.readMessage(1);
            int a = 0;
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void sendMessageTest(){
        try {
            init();
            service.sendMessage(5,"草拟");
            int a = 0;
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }
}
