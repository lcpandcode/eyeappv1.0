package com.bysj.eyeapp.view;


import android.os.Bundle;
import android.view.View;

public class TestSensitivityResultActivity extends BaseActivity {
	//字符串常量


	// tab用参数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_sensitivity_result);
	}

	private void init(){
		//设置监听按钮事件

	}


	/**
	 * 标题栏返回按钮
	 */
	public void titleReturn(View v){
		finish();
	}






}
