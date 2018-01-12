package com.bysj.eyeapp.view;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.service.UserService;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.UserVO;

/**
 * Created by lcplcp on 2017/12/25.
 */
public class LoginActivity extends BaseActivity {

    //页面控件的相关变量
    private EditText userNameEditText ;
    private EditText pwdEditText ;

    private UserService service;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置标题栏相关信息
        setupViews((TextView)findViewById(R.id.login_title),null,null);

        //初始化变量
        userNameEditText = (EditText)findViewById(R.id.login_userName);
        pwdEditText = (EditText)findViewById(R.id.login_pwd);
        service = new UserService();
    }

    /**
     * 登录点击事件触发的方法
     * @param view
     */
    public void login(View view){
        //输入的逻辑判断
        String inputUserName = userNameEditText.getText().toString();
        String inputPwd = pwdEditText.getText().toString();
        if(RegularUtil.strIsEmpty(inputUserName) || RegularUtil.strIsEmpty(inputPwd)){
            Toast.makeText(getApplicationContext(),"账号或密码不能为空！",Toast.LENGTH_SHORT).show();
            return ;
        }
        //提交数据
        UserVO user = null;
        try {
            user = service.login(inputUserName,inputPwd);
        } catch (HttpException e) {
            Log.e("httpException:" ,e.getMessage());
            CustomToast.showToast(getApplicationContext(),e.getMessage());
            return ;
        }catch (UserException e){
            CustomToast.showToast(getApplicationContext(),e.getMessage());
            return ;
        }

        //通过验证，跳转至主界面
        GlobalApplication application = (GlobalApplication)getApplication();
        application.putGlobalVar("user",user);
        CustomToast.showToast(getApplicationContext(),"登录成功，准备跳转页面");
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * 注册点击事件触发的方法
     * @param view
     */
    public void register(View view){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 忘记密码点击事件触发的方法
     * @param view
     */
    public void findPwd(View view){
        CustomToast.showToast(getApplicationContext(),"这个功能还没有开发！");
    }

    /**
     * 显示密码点击事件触发的方法
     * @param view
     */
    private boolean showPwd = false;
    public void showPwd(View view){
        if(!showPwd){
            pwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            pwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        showPwd = !showPwd;
        pwdEditText.postInvalidate();
    }





}
