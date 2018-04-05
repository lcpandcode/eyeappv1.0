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
import com.bysj.eyeapp.service.TestService;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.TestResultVO;

import java.io.Serializable;

public class TestColorbindResultActivity extends BaseActivity {
	private static final String REMIND_STRING_EMPTY = "数据有误，测试相关数据不能为空";

	//控件相关变量
	private TextView trueRate ;
	private TextView result ;
	private TextView probability;
	private Button submit;
	private Button retest;


	//数据相关变量
	private TestColorbindResult testResult;
	private TestService service;


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
		service = new TestService();
		//获取传输过来的数据
		Intent intent = getIntent();
		//反序列化数据对象
		Serializable se = intent.getSerializableExtra(TestColorbindFragment.getTestResultKey());
		if(se instanceof TestColorbindResult){
			testResult = (TestColorbindResult)se;
		}else {
			throw new RuntimeException("对象不可反序列化为TestColorbindResult对象");
		}
//		String correctRate = intent.getExtras().getString("correctRate");
//		String result = intent.getExtras().getString("result");
//		String probability = (100-Integer.parseInt(correctRate)) + "";
		//设置数据
		trueRate.setText(testResult.getTrueRate() + "%");
		this.result.setText(testResult.getResult());
		this.probability.setText(testResult.getProbability() + "%");

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
	public void submit(View view) {
		//获取并组装数据
		String trueRate = this.trueRate.getText().toString();
		trueRate = trueRate.split("%")[0];
		String probability = this.probability.getText().toString();
		probability = probability.split("%")[0];
		String result = this.result.getText().toString();
		//判断字符是否合法
		if(RegularUtil.strIsEmpty(trueRate) || RegularUtil.strIsEmpty(probability)
				|| RegularUtil.strIsEmpty(result)){
			CustomToast.showToast(getApplicationContext(),REMIND_STRING_EMPTY);
			return ;
		}
		//数据转换
		int trueRateInt =  (int)Double.parseDouble(trueRate);
		TestResultVO resultVO = new TestResultVO();
		resultVO.setCorrectRate(trueRateInt);
		resultVO.setTestResult(this.result.getText().toString());
		resultVO.setType(GlobalConst.TEST_TYPE_COLORBIND);
		resultVO.setEye("");
		try {
			service.submitTestResult(resultVO);
		} catch (HttpException e) {
			Log.e("网络错误：",e.getMessage());
			CustomToast.showToast(getApplicationContext(),GlobalConst.REMIND_NET_ERROR);
			return ;
		} catch (TestException e){
			Log.e("提交失败：",e.getMessage());
			CustomToast.showToast(getApplicationContext(),GlobalConst.REMIND_NET_ERROR);
			return ;
		}catch (UserException e){
			Log.e("用户权限：",e.getMessage());
			CustomToast.showToast(getApplicationContext(),e.getMessage());
			//没有登录，跳转至登录界面
			goToLogin();
			return ;
		}catch (Exception e){
			Log.e("系统错误：",e.getMessage());
			CustomToast.showToast(getApplicationContext(),GlobalConst.SYSTEM_ERROR + e.getMessage());
			return ;
		}
		//无异常，说明提交成功
		CustomToast.showToast(getApplicationContext(),GlobalConst.REMIND_SUBMIT_SUCCESS);
		finish();
	}


	/**
	 * 跳转到登录界面
	 */
	private void goToLogin(){
		Intent intent = new Intent();
		//设置标志数据，登录完成跳回原来的提问界面
		intent.putExtra(GlobalConst.LOGIN_TO_OTHER_UI_TAG,"TestColorbindResult");
		intent.setClass(getApplicationContext(),LoginActivity.class);
		startActivity(intent);
	}

	/**
	 * 重新测试数据
	 */
	public void retest(View view){
		finish();
	}



}
