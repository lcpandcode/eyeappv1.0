package com.bysj.eyeapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.service.UserService;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.vo.UserVO;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PersonInfo extends BaseActivity{
    private Button backward;
    private Button changePersonInfo;
    private UserVO user;
    private UserService service;

    private EditText password;
    private EditText nickName;
    private EditText sex;
    private EditText phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcentre_info);
        init();

    }


    private void init() {
        service = new UserService();
        backward=findViewById(R.id.btn_backward);
        changePersonInfo=findViewById(R.id.btn_changePersonInfo);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        GlobalApplication globalApplication = (GlobalApplication)getApplication().getApplicationContext();
        user = (UserVO)globalApplication.getGlobalVar(GlobalConst.TAG_USER);
        if(user==null){
            //未登录，跳转登录界面
            CustomToast.showToast(getApplicationContext(),"您尚未登录！");
            goToLogin();
            return;
        }
        password = findViewById(R.id.personcentre_password);
        nickName = findViewById(R.id.personcentre_nickname);
        sex = findViewById(R.id.personcentre_sex);
        phone = findViewById(R.id.personcentre_phone);
        initUserInfo();
    }

    private void initUserInfo(){
        //password.setText(user.getPassword());
        nickName.setText(user.getNickName());
        sex.setText(user.getSex());
        phone.setText(user.getPhone());
    }

    private void goToLogin(){
        Intent intent = new Intent();
        //设置标志数据，登录完成跳回原来的提问界面
        //intent.putExtra(GlobalConst.LOGIN_TO_OTHER_UI_TAG,"PersonInfo");
        intent.setClass(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    public void updatePersonInfo(View view){
        String pass = password.getText().toString();
        String nick = nickName.getText().toString();
        String sex = this.sex.getText().toString();
        if(!"男".equals(sex) && !"女".equals(sex)){
            CustomToast.showToast(getApplicationContext(),"性别输入有误，请输入男或女");
            return;
        }
        if("".equals(pass)){
            CustomToast.showToast(getApplicationContext(),"请输入密码");
            return;
        }
        if("".equals(nick)){
            CustomToast.showToast(getApplicationContext(),"请输入昵称");
            return;
        }
        try {
            UserVO userVO = new UserVO();
            userVO.setSex(sex);
            userVO.setPassword(pass);
            userVO.setNickName(nick);
            service.updateUserInfo(userVO);
        } catch (HttpException e) {
            e.printStackTrace();
            CustomToast.showToast(getApplicationContext(),GlobalConst.REMIND_NET_ERROR);
            return;
        }catch (UserException e){
            CustomToast.showToast(getApplicationContext(),e.getMessage());
            return;
        }
        CustomToast.showToast(getApplicationContext(),"更新成功");
        finish();
    }

}