package com.bysj.eyeapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lcplcp on 2017/12/25.
 */
public class LoginActivity extends BaseActivity {
    //页面控件的相关变量
    private EditText userNameEditText ;
    private EditText pwdEditText ;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置标题栏相关信息
        setupViews((TextView)findViewById(R.id.login_title),null,null);

        //初始化变量
        userNameEditText = (EditText)findViewById(R.id.login_userName);
        pwdEditText = (EditText)findViewById(R.id.login_pwd);
    }

    /**
     * 登录点击事件触发的方法
     * @param view
     */
    public void login(View view){
        //输入的逻辑判断
        String inputUserName = userNameEditText.getText().toString();
        String inputPwd = pwdEditText.getText().toString();
        if(inputPwd == null || inputPwd == null || "".equals(inputPwd) || "".equals(inputUserName)){
            Toast.makeText(getApplicationContext(),"账号或密码不能为空！",Toast.LENGTH_SHORT).show();
            return ;
        }
        //提交数据
        boolean loginResult = inputUserName.equals("1") && inputPwd.equals("1");
        if(!loginResult){
            Toast.makeText(getApplicationContext(),"账号或密码错误！账号为1，密码为1！",Toast.LENGTH_SHORT).show();
            return ;
        }

        //通过验证，跳转至主界面
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * 注册点击事件触发的方法
     * @param view
     */
    public void register(View view){
        Toast.makeText(getApplicationContext(),"register",Toast.LENGTH_SHORT).show();
    }

    /**
     * 忘记密码点击事件触发的方法
     * @param view
     */
    public void findPwd(View view){
        Toast.makeText(getApplicationContext(),"findPwd",Toast.LENGTH_SHORT).show();
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
