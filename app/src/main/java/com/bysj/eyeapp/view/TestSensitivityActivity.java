package com.bysj.eyeapp.view;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.eyeapp.service.TestSensitivityService;
import com.bysj.eyeapp.util.TestSensitivityUtil;
import com.bysj.eyeapp.vo.TestSensitivityQuestionVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestSensitivityActivity extends BaseActivity {
	//字符串常量
	final private static double MAX_SENSITIVITY = 100.0;
	final private static int QUESTION_NUM = 10;//作答个数默认10
	final private static String TEST_RESULT_KEY = "敏感度测试结果";
	final private static double TEST_RESULT_VERYGOOD = 0.9 * MAX_SENSITIVITY;//优秀的阈值率：大于（0.9 * 最大敏感度值） 代表优秀，下面意思类似
	final private static double TEST_RESULT_GOOD = 0.8 * MAX_SENSITIVITY;
	final private static double TEST_RESULT_NORMAL = 0.7 * MAX_SENSITIVITY;
	final private static double TEST_RESULT_LOW = 0.6 * MAX_SENSITIVITY;
	final private static double TEST_RESULT_BAD = 0.5 * MAX_SENSITIVITY;
	//下面是各个结果对应的结果字符
	final private static String TEST_RESULT_VERYGOOD_STR = "敏感度非常好";
	final private static String TEST_RESULT_GOOD_STR = "敏感度良好";
	final private static String TEST_RESULT_NORMAL_STR = "敏感度正常";
	final private static String TEST_RESULT_LOW_STR = "敏感度偏低";
	final private static String TEST_RESULT_BAD_STR = "敏感度好糟糕啊";

	//类数据相关变量
	private int nowAnswerQuestion = 0;//当前作答数目
	private List<TestSensitivityQuestionVO> questions;//题目列表
	private List<Boolean> testResult;//每道题对应的答题状况：正确还是错误，在计算最终结果时要用到
	private int nowAnswerTrue = 0;//当前作答正确个数
	private TestSensitivityService service;//核心服务类，service层的类
	List<Button> btns ;//所有敏感度测试的按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_sensitivity);
		init();
	}

	private void init(){
		//初始化服务类
		service = new TestSensitivityService();
		//初始化答题结果列表
		testResult = new ArrayList<>();
		//初始化问题数据列表
		questions = service.getDefaultSensitivityQustions();
		//初始化按钮列表
		initBtns();

	}


	/**
	 * 初始化按钮颜色，设置正确背景颜色按钮的点击事件
	 */
	private void initBtns(){
		btns = new ArrayList<>();
		//设置监听按钮事件
		ViewGroup btnTable = (ViewGroup)findViewById(R.id.test_sensitivity_btns);
		//循环初始化按钮列表
		int rowSize = btnTable.getChildCount();
		List<View> btnRow = new ArrayList<>();
		for(int i = 1;i<rowSize;i++){
			btnRow.add(btnTable.getChildAt(i));
		}
		//初始化按钮列表
		for(View row : btnRow){
			ViewGroup vgTem = (ViewGroup)((ViewGroup) row).getChildAt(0);
			int btnCount = vgTem.getChildCount();

			for(int i = 0;i<btnCount;i++){
				Button btn = (Button) vgTem.getChildAt(i);
				btns.add(btn);
			}
		}
		TestSensitivityQuestionVO question = questions.get(0);
		//初始化按钮数据
		showQuestion(question);
	}

	/**
	 * 把问题数据展示在界面上
	 * @param question 问题数据
	 */
	private void showQuestion(TestSensitivityQuestionVO question){
		//获取一个随机数字为正确按钮的对应序号
		int btnTrueNum = (int)(Math.random() * btns.size()) ;
		//循环按钮列表设置对应的颜色以及监听事件
		for(int i = 0;i<btns.size();i++){
			Button btnOption = btns.get(i);
			if(i==btnTrueNum){
				//为正确按钮设置特定颜色，并设置特定的监听事件
				setBtnColor(btnOption,question.getTrueOptionColor());
				btnOption.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						sensitivityBtnOnClickTrue(v);
					}
				});
			}else {
				//错误按钮设置对应颜色
				setBtnColor(btnOption,question.getBackgroundColor());
				btnOption.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						sensitivityBtnOnClickFalse(v);
					}
				});
			}
		}

	}

	/**
	 * 设置某个按钮的背景颜色
	 * @param btn 按钮对象
	 * @param color 颜色对应的字符串，用十六进制字符表示
	 */
	private void setBtnColor(Button btn,String color){
		GradientDrawable grad = (GradientDrawable) btn.getBackground ();
		grad.setColor (Color.parseColor (color));
		btn.setBackgroundDrawable (grad);
	}


	/**
	 * 敏感度按钮点击错误时的点击事件
	 */
	private void sensitivityBtnOnClickFalse(View view){
		//存储当前答题结果
		testResult.add(false);
		nextQuestion();
	}
	/**
	 * 敏感度按钮点击正确时的点击事件
	 */
	private void sensitivityBtnOnClickTrue(View view){
		testResult.add(true);
		nowAnswerTrue++;
		nextQuestion();
	}

	/**
	 * 下一题切换方法
	 */
	private void nextQuestion(){
		nowAnswerQuestion++;
		//判断是否答完题目
		if(nowAnswerQuestion>=QUESTION_NUM){
			//答题完毕，统计结果
			TestSensitivityResult result = getTestResult();
			//跳转至结果界面
			Intent intent = new Intent();
			intent.putExtra(TEST_RESULT_KEY,result);
			intent.setClass(getApplicationContext(),TestSensitivityResultActivity.class);
			startActivity(intent);
			return;
		}
		//重新刷新question页面
		TestSensitivityQuestionVO question = questions.get(nowAnswerQuestion);
		showQuestion(question);
	}

	/**
	 * 统计测试结果
	 */
	private TestSensitivityResult getTestResult(){
		TestSensitivityResult result = new TestSensitivityResult();
		double rate = ((double) nowAnswerTrue)/questions.size() ;
		result.setTrueRate(rate);
		double sensitivity = TestSensitivityUtil.countSensitivity(questions,testResult);
		result.setSensitivity(sensitivity);
		//根据sensitivity的范围得出结果
		if(sensitivity>TEST_RESULT_VERYGOOD){
			result.setResult(TEST_RESULT_VERYGOOD_STR);
		}else if(sensitivity > TEST_RESULT_GOOD){
			result.setResult(TEST_RESULT_GOOD_STR);
		} else if(sensitivity > TEST_RESULT_NORMAL){
			result.setResult(TEST_RESULT_NORMAL_STR);
		} else if(sensitivity > TEST_RESULT_LOW){
			result.setResult(TEST_RESULT_LOW_STR);
		}else {
			result.setResult(TEST_RESULT_BAD_STR);
		}
		return result;
	}



	/**
	 * 标题栏返回按钮
	 */
	public void titleReturn(View v){
		finish();
	}

	public static String getTestResultKey() {
		return TEST_RESULT_KEY;
	}

	public static double getMaxSensitivity() {
		return MAX_SENSITIVITY;
	}
}
class TestSensitivityResult implements Serializable {
	private double trueRate;//答题正确率
	private String result;//测试结果
	private double sensitivity;//眼睛敏感度

	public double getTrueRate() {
		return trueRate;
	}

	public void setTrueRate(double trueRate) {
		this.trueRate = trueRate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


	public double getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}
}
