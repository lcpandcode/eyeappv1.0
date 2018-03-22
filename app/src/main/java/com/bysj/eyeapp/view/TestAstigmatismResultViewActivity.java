package com.bysj.eyeapp.view;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.TestException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.service.PersonService;
import com.bysj.eyeapp.service.TestService;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.TestResultVO;

import java.io.Serializable;

public class TestAstigmatismResultViewActivity extends BaseActivity {
	private static final String REMIND_STRING_EMPTY = "提交字符结果不合法,不能为空";

	//控件相关变量
	private TextView trueRate ;
	private TextView result ;
	private TextView probability;
	private TextView testEye;
	private PersonService service;


	//数据相关变量
	private TestAstigmatismResult testResult;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_astigmatism_result_view);
		init();
	}

	private void init(){
		service = new PersonService(null);
		//初始化控件
		trueRate = findViewById(R.id.test_astigmatism_result_truerate);
		probability = findViewById(R.id.test_astigmatism_result_probability);
		result = findViewById(R.id.test_astigmatism_result_result);
		testEye = findViewById(R.id.test_astigmatism_result_eye);
		//获取传输过来的数据
		Intent intent = getIntent();
		//反序列化数据对象
		int id = intent.getIntExtra("id",0);
		TestResultVO result = service.getTrainDetail(id);
		//设置数据
		trueRate.setText(result.getCorrectRate() + "%" );
		this.result.setText(result.getTestResult() + "");
	}


	/**
	 * 标题栏返回按钮
	 */
	public void titleReturn(View v){
		finish();
	}













}
