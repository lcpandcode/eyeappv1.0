package com.bysj.eyeapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {
    //常量定义处
    private static final String MAN = "男";
    private static final String WOMAN = "女";

    //相关控件的变量
    private EditText nickNameEditText;
    private EditText phoneEditText;
    private EditText pwdEditText;
    private EditText checkCodeEditText;
    private RadioGroup sexRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    /**
     * 初始化方法，主要初始化控件相关变量
     */
    private void init(){
        nickNameEditText = findViewById(R.id.register_nickName);
        phoneEditText = findViewById(R.id.register_phone);
        pwdEditText = findViewById(R.id.register_pwd);
        checkCodeEditText = findViewById(R.id.register_checkCode);
        sexRadioGroup = findViewById(R.id.register_sex);
    }

    /**
     * 注册方法
     */
    public void register(View view){
        //获取注册信息
        String nickName = nickNameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String pwd = pwdEditText.getText().toString();
        String checkCode = checkCodeEditText.getText().toString();
        int sexId = sexRadioGroup.getCheckedRadioButtonId();
        String sex = null;
        if(sexId==R.id.register_rbtn_man){
            sex = MAN;
        }else if(sexId==R.id.register_rbtn_woman){
            sex = WOMAN;
        }
        //进行是否为空验证
        if(nickName==null || "".equals(nickName)){
            Toast.makeText(getApplicationContext(),"请输入昵称",Toast.LENGTH_SHORT).show();
            return ;
        }else if(phone==null || "".equals(phone)){
            Toast.makeText(getApplicationContext(),"请输入手机号",Toast.LENGTH_SHORT).show();;
            return ;
        }else if(pwd==null || "".equals(pwd)){
            Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();;
            return ;
        }else if(checkCode==null || "".equals(checkCode)){
            Toast.makeText(getApplicationContext(),"请输入验证码",Toast.LENGTH_SHORT).show();;
            return ;
        }else if(sex==null || "".equals(sex)){
            Toast.makeText(getApplicationContext(),"请选择性别",Toast.LENGTH_SHORT).show();;
            return ;
        }

        Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();

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

    public void serviceRule(View view){

    }
}
