package com.bysj.eyeapp.view;

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

import com.bysj.eyeapp.util.RegularUtil;

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
            Toast.makeText(getApplicationContext(),REGISTER_REMIND_NICKNAME,Toast.LENGTH_SHORT).show();
            return ;
        }else if(phone==null || "".equals(phone)){
            Toast.makeText(getApplicationContext(),REGISTER_REMIND_PHONE,Toast.LENGTH_SHORT).show();
            return ;
        }else if(pwd==null || "".equals(pwd)){
            Toast.makeText(getApplicationContext(),REGISTER_REMIND_PWD,Toast.LENGTH_SHORT).show();
            return ;
        }else if(checkCode==null || "".equals(checkCode)){
            Toast.makeText(getApplicationContext(),REGISTER_REMIND_CHECKCODE,Toast.LENGTH_SHORT).show();
            return ;
        }else if(sex==null || "".equals(sex)){
            Toast.makeText(getApplicationContext(),REGISTER_REMIND_SEX,Toast.LENGTH_SHORT).show();
            return ;
        }

        //检验合法性
        if(!RegularUtil.phoneIsTrue(phone)){
            Toast.makeText(getApplicationContext(),REGISTER_REMIND_PHONE_ILLEGAL,Toast.LENGTH_SHORT).show();
            return ;
        }else if(!RegularUtil.checkCodeIsTrue(checkCode)){
            Toast.makeText(getApplicationContext(),REGISTER_REMIND_CHECKCODE_ILLEGAL,Toast.LENGTH_SHORT).show();
            return ;
        }
        //通过验证，吧字符组装为json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nickName",nickName);
            jsonObject.put("phone",phone);
            jsonObject.put("password",pwd);
            jsonObject.put("checkCode",checkCode);
            jsonObject.put("sex",sex);
        } catch (JSONException e) {
            Log.e("系统错误" , "相关注册字段无法转化为Json数组",e);
        }
        String registerInfo = jsonObject.toString();
        Toast.makeText(getApplicationContext(),registerInfo,Toast.LENGTH_SHORT).show();
        //下面进行登录验证通讯

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
}
