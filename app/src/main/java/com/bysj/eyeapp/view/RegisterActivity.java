package com.bysj.eyeapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.service.UserService;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.UserVO;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity {
    //常量定义处
    private static final String MAN = "男";
    private static final String WOMAN = "女";
    private static final String SYSTEM_ERROR = "系统错误";
    private static final String JSON_ERROR = "注册字段无法转换为JSON对象";
    private static final String REGISTER_REMIND_NICKNAME = "请输入昵称";
    private static final String REGISTER_REMIND_PWD = "请输入密码";
    private static final String REGISTER_REMIND_PHONE = "请输入手机号";
    private static final String REGISTER_REMIND_CHECKCODE= "请输入验证码";
    private static final String REGISTER_REMIND_SEX = "请选择性别";
    private static final String REGISTER_REMIND_PHONE_ILLEGAL = "手机号非法";
    private static final String REGISTER_REMIND_CHECKCODE_ILLEGAL = "验证码非法";
    private static final String REGISTER_REMIND_SUCCESS = "注册成功，将跳转到登录界面";

    //相关控件的变量
    private EditText nickNameEditText;
    private EditText phoneEditText;
    private EditText pwdEditText;
    private EditText checkCodeEditText;
    private RadioGroup sexRadioGroup;

    private UserService service;


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
        service = new UserService();
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
        if(RegularUtil.strIsEmpty(nickName)){
            CustomToast.showToast(getApplicationContext(),REGISTER_REMIND_NICKNAME);
            return ;
        }else if(RegularUtil.strIsEmpty(phone)){
            CustomToast.showToast(getApplicationContext(),REGISTER_REMIND_PHONE);
            return ;
        }else if(RegularUtil.strIsEmpty(pwd)){
            CustomToast.showToast(getApplicationContext(),REGISTER_REMIND_PWD);
            return ;
        }else if(RegularUtil.strIsEmpty(checkCode)){
            CustomToast.showToast(getApplicationContext(),REGISTER_REMIND_CHECKCODE);
            return ;
        }else if(RegularUtil.strIsEmpty(sex)){
            CustomToast.showToast(getApplicationContext(),REGISTER_REMIND_SEX);
            return ;
        }

        //检验合法性
        if(!RegularUtil.phoneIsTrue(phone)){
            CustomToast.showToast(getApplicationContext(),REGISTER_REMIND_PHONE_ILLEGAL);
            return ;
        }else if(!RegularUtil.checkCodeIsTrue(checkCode)){
            CustomToast.showToast(getApplicationContext(),REGISTER_REMIND_CHECKCODE_ILLEGAL);
            return ;
        }

        //下面进行注册验证通讯
        UserVO user = new UserVO();
        user.setSex(sex);
        user.setType("用户");
        user.setNickName(nickName);
        user.setPassword(pwd);
        user.setPhone(phone);
        try {
            service.register(user);
        } catch (HttpException e) {
            e.printStackTrace();
            CustomToast.showToast(getApplicationContext(), GlobalConst.REMIND_NET_ERROR);
            return ;
        } catch (UserException e){
            e.printStackTrace();
            CustomToast.showToast(getApplicationContext(), e.getMessage());
            return ;
        }
        CustomToast.showToast(getApplicationContext(), REGISTER_REMIND_SUCCESS);
        //跳转到登录界面
        showLogin();
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

    /**
     * 跳转到服务条框界面
     * @param view 点击按钮对象
     */
    public void serviceRule(View view){
        //待完善
    }

    /**
     * 跳转到登录界面
     */
    private void showLogin(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 获取验证码
     */
    public void getCheckCode(View view){
        CustomToast.showToast(getApplicationContext(),"验证码短信平台没搞好，随便输个数字吧");

    }
}
