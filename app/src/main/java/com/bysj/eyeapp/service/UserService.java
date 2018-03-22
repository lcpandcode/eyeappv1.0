package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2018/1/10.
 */

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.UserVO;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务类
 */
public class UserService {
    private final static String REMIND_PHONE_ERROR = "手机号输入不合法";
    private final static String REMIND_PASS_ERROR = "密码输入不合法";
    private final static String LOGIN_PATH = "/user/login.do";
    private final static  String REGISTER_PATH = "/user/register.do";
    private final static  String UPDATEINFO_PATH = "/user/update_information.do";


    public UserVO login(String phone,String password) throws HttpException {
        UserVO user = new UserVO();
        if(RegularUtil.strIsEmpty(phone)){
            throw new UserException(REMIND_PHONE_ERROR);
        }
        if(RegularUtil.strIsEmpty(password)){
            throw new UserException(REMIND_PASS_ERROR);
        }
        Map<String,String> params = new HashMap<>();
        params.put("phone",phone);
        params.put("password",password);
        String result = HttpUtil.synPost(LOGIN_PATH,params);
        //判断请求状态
        Map<String,Object> resultMap = (Map<String,Object>)JavaBeanUtil.jsonToObj(result);
        int status = (Integer) resultMap.get("status");
        if(status!=0){
            //说明登录失败
            String msg = (String)resultMap.get("msg");
            throw new UserException(msg);
        }
        //登录成功
        Map<String,Object> data = (Map<String,Object>)resultMap.get("data");
        int id = (Integer) data.get("id");
        String nickName = (String)data.get("nickName");
        phone = (String) data.get("phone");
        String sex = (String)data.get("sex");
        String type = (String)data.get("type");
        user.setId(id).setNickName(nickName).setSex(sex).setType(type).setPhone(phone);
        user.setToken(HttpUtil.getToken());
        return user;
    }

    /**
     * 注册方法
     * @param user
     * @throws HttpException
     */
    public void register(UserVO user ) throws HttpException {
        Map<String,String> params = new HashMap<>();
        params.put("nickName",user.getNickName());
        params.put("phone",user.getPhone());
        params.put("sex",user.getSex());
        params.put("password",user.getPassword());
        params.put("type",user.getType());
        String result = HttpUtil.synPost(REGISTER_PATH,params);
        Map<String,Object> resultMap = (Map<String,Object>) JavaBeanUtil.jsonToObj(result);
        int status = (Integer) resultMap.get("status");
        if(status!=0){
            throw new UserException("注册失败：" + resultMap.get("msg").toString());
        }
    }

    public void updateUserInfo(UserVO user) throws HttpException {
        Map<String,String> params = new HashMap<>();
        params.put("nickName",user.getNickName());
        params.put("sex",user.getSex());
        params.put("password",user.getPassword());
        String result = HttpUtil.synPost(UPDATEINFO_PATH,params);
        Map<String,Object> resultMap = (Map<String,Object>) JavaBeanUtil.jsonToObj(result);
        int status = (Integer) resultMap.get("status");
        if(status!=0){
            throw new UserException("更新失败：" + resultMap.get("msg").toString());
        }
    }

}
