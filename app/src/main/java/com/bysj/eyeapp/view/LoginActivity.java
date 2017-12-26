package com.bysj.eyeapp.view;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lcplcp on 2017/12/25.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setupViews();
    }

    @Override
    protected void setupViews() {
        mTitleTextView = (TextView) findViewById(R.id.text_title);
        mBackwardbButton = (Button) findViewById(R.id.button_backward);
        mForwardButton = (Button) findViewById(R.id.button_forward);
        showBackwardView(R.string.title_btn_return,false);
        showForwardView(R.string.title_btn_confirm,false);
    }

    /**
     * 登录点击事件触发的方法
     * @param view
     */
    public void login(View view){
        Toast.makeText(getApplicationContext(),"login",Toast.LENGTH_SHORT).show();
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
        EditText userNameEditText = findViewById(R.id.login_pwd);
        if(!showPwd){
            userNameEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            userNameEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        showPwd = !showPwd;
        userNameEditText.postInvalidate();
    }





}
