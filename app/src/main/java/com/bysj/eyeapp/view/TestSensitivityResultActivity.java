package com.bysj.eyeapp.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DecimalFormat;

public class TestSensitivityResultActivity extends BaseActivity {
    //控件相关变量
    private TextView trueRate;
    private TextView sensitivity;
    private TextView result;
    private Button submit;
    private Button retest;


    //数据相关变量
    private TestSensitivityResult testResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sensitivity_result);

        init();
    }

    private void init() {
        //初始化控件
        trueRate = findViewById(R.id.test_sensitivity_result_truerate);
        sensitivity = findViewById(R.id.test_sensitivity_result_sensitivity);
        result = findViewById(R.id.test_sensitivity_result_result);
        submit = findViewById(R.id.test_sensitivity_result_submit);
        retest = findViewById(R.id.test_sensitivity_result_retest);
        //获取传输过来的数据
        Intent intent = getIntent();
        //反序列化数据对象
        Serializable se = intent.getSerializableExtra(TestSensitivityActivity.getTestResultKey());
        if (se instanceof TestSensitivityResult) {
            testResult = (TestSensitivityResult) se;
        } else {
            throw new RuntimeException("对象不可反序列化为TestSensitivityResult对象");
        }
        //设置数据
        trueRate.setText(testResult.getTrueRate() * 100 + "%");
        sensitivity.setText(new DecimalFormat("######0.00").format(testResult.getSensitivity()));//保留两位小数
		//设置结果
        result.setText(testResult.getResult());

    }


    /**
     * 标题栏返回按钮
     */
    public void titleReturn(View v) {
        finish();
    }


    /**
     * 提交数据
     */
    public void submit(View view) {
        Toast.makeText(getApplicationContext(),"提交按钮待完善",Toast.LENGTH_SHORT).show();

    }

    /**
     * 重新测试数据
     */
    public void retest(View view) {
        finish();
    }


}
