package com.bysj.eyeapp.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

public class TestColorbindResultActivity extends BaseActivity {
	//控件相关变量
	private TextView trueRate ;
	private TextView result ;
	private TextView probability;
	private Button submit;
	private Button retest;


	//数据相关变量
	private TestColorbindResult testResult;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_colorbind_result);

		init();
	}

	private void init(){
		//初始化控件
		trueRate = findViewById(R.id.test_colorbind_result_truerate);
		probability = findViewById(R.id.test_colorbind_result_probability);
		result = findViewById(R.id.test_colorbind_result_result);
		submit = findViewById(R.id.test_colorbind_result_submit);
		retest = findViewById(R.id.test_colorbind_result_retest);
		//获取传输过来的数据
		Intent intent = getIntent();
		//反序列化数据对象
		Serializable se = intent.getSerializableExtra(TestColorbindFragment.getTestResultKey());
		if(se instanceof TestColorbindResult){
			testResult = (TestColorbindResult)se;
		}else {
			throw new RuntimeException("对象不可反序列化为TestColorbindResult对象");
		}
		//设置数据
		trueRate.setText(testResult.getTrueRate() + "%");
		result.setText(testResult.getResult());
		probability.setText(testResult.getProbability() + "%");

	}


	/**
	 * 标题栏返回按钮
	 */
	public void titleReturn(View v){
		finish();
	}


	/**
	 * 提交数据（待完善）
	 */
	public void submit(View view){

	}

	/**
	 * 重新测试数据
	 */
	public void retest(View view){
		finish();
	}



}
