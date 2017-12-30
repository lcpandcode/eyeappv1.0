package com.bysj.eyeapp.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.eyeapp.service.TestAstigmatismService;
import com.bysj.eyeapp.vo.TestAstigmatismQuestionVO;

import java.io.Serializable;
import java.util.List;

public class TestAstigmatismFragment extends Fragment {
	//控件变量
	//字符串常量
	final private static int QUESTION_NUM = 2;//作答个数默认10
	final private static double SERIOUS = 0.5;//阈值：答题正确率小于SERIOUS判断测试结果为严重
	final private static double MEDIUM = 0.7;//阈值：答题正确率小于MEDIUM判断测试结果为中等
	final private static double LITTLE = 0.8;//阈值：答题正确率小于LITTLE判断测试结果为轻微患病
	final private static String TEST_RESULT_KEY = "散光测试结果";
	final private static String REMIND_CANNOT_CHANGE_EYE = "您已开始答题，不能改变选中的眼睛";
	final private static String REMIDN_CHOSE_EYE = "请先选择您要测试的眼睛！";
	final private static char LEFT_EYE = '左';
	final private static char RIGHT_EYE = '右';

	//控件相关变量
	private View thisView;
	private Button btnNext ;
	private ImageView questionImg;
	private TextView questionTitle;
	private RadioGroup rbtnsEye;//选择研究对应的按钮
	private RadioGroup options;
	private RadioButton option1;
	private RadioButton option2;
	private RadioButton option3;
	private RadioButton option4;
	private RadioButton rbtnLeftEye;
	private RadioButton rbtnRightEye;

	//类数据相关变量
	private int nowAnswerQuestion = 0;//当前作答数目
	private List<TestAstigmatismQuestionVO> questions;//题目列表
	private int nowAnswerTrue = 0;//当前作答正确个数
	private TestAstigmatismService service;//核心服务类，service层的类
	private boolean isChoseEye = false;//会否选择了眼睛，如果否提示选择眼睛方可进行测试
	private boolean canChangeChoseEye = true;//能否改变选中的眼睛：答题开始后（即已经至少答了一题）不能改变眼睛选择

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_test_astigmatism, null);
		thisView = view;
		init();
		return view;
	}

	private void init(){
		//初始化控件变量
		btnNext = thisView.findViewById(R.id.test_astigmatism_nextbtn);
		questionImg = thisView.findViewById(R.id.test_question_astigmatism_img);
		questionTitle = thisView.findViewById(R.id.test_astigmatism_question_title);
		options = thisView.findViewById(R.id.test_astigmatism_options);
		option1 = thisView.findViewById(R.id.test_astigmatism_option1);
		option2 = thisView.findViewById(R.id.test_astigmatism_option2);
		option3 = thisView.findViewById(R.id.test_astigmatism_option3);
		option4 = thisView.findViewById(R.id.test_astigmatism_option4);
		rbtnsEye = thisView.findViewById(R.id.test_astigmatism_rbtns);
		rbtnLeftEye = thisView.findViewById(R.id.test_astigmatism_rbtn_eye_left);
		rbtnRightEye = thisView.findViewById(R.id.test_astigmatism_rbtn_eye_right);
		service = new TestAstigmatismService();
		//初始化数据
		questions = service.getAstigmatismQustion(QUESTION_NUM);
		//初始化第一个答题页面
		showNewQuestion(questions.get(nowAnswerQuestion));
		//设置监听事件
		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nextQuestion();
			}
		});
		rbtnLeftEye.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isChoseEye = true;
			}
		});
		rbtnRightEye.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isChoseEye = true;
			}
		});
	}

	/**
	 * 下一题按钮对应的事件
	 */
	private void nextQuestion(){
		//判断是否选择了眼睛
		if(!isChoseEye){
			Toast.makeText(getActivity(),REMIDN_CHOSE_EYE,Toast.LENGTH_SHORT).show();
			return ;
		}
		//一旦开始答题，不能改变选中的眼睛
		setCanChangeChoseEye();
		//判断答题情况，并更新答题状态
		judgeTest();
		nowAnswerQuestion++;
		if(nowAnswerQuestion<QUESTION_NUM){
			//获取下一题
			TestAstigmatismQuestionVO question = questions.get(nowAnswerQuestion);
			//清除上题的选项
			options.clearCheck();
			//更新页面问题
			showNewQuestion(question);
		}else {
			//作答完成，获取答题结果
			TestAstigmatismResult result = getTestResult();
			Intent intent = new Intent();
			intent.putExtra(TEST_RESULT_KEY,result);
			intent.setClass(thisView.getContext(),TestAstigmatismResultActivity.class);
			startActivity(intent);
		}

	}

	/**
	 * 判断当前答题是否正确
	 */
	private void judgeTest(){
		TestAstigmatismQuestionVO question = questions.get(nowAnswerQuestion);
		//获取选项值
		int checkId = options.getCheckedRadioButtonId();
		int choseOption = 0;
		switch (checkId) {
			case R.id.test_astigmatism_option1:
				choseOption = 1;
				break;
			case R.id.test_astigmatism_option2:
				choseOption = 2;
				break;
			case R.id.test_astigmatism_option3:
				choseOption = 3;
				break;
			case R.id.test_astigmatism_option4:
				choseOption = 4;
				break;
		}
		if(question.getTrueOption()==choseOption){
			//正确题目数加1
			nowAnswerTrue++;
		}

	}


	/**
	 * 更新页面的问题区域（待完善）
	 */
	private void showNewQuestion(TestAstigmatismQuestionVO question){
		//设置图片展示的src属性待完善（需要发起请求获得Bitmap）

		questionTitle.setText(question.getTitle());
		option1.setText(question.getOption1());
		option2.setText(question.getOption2());
		option3.setText(question.getOption3());
		option4.setText(question.getOption4());

	}

	/**
	 * 计算作答结果方法（该方法未完成，如何计算患病概率带定）
	 */
	private TestAstigmatismResult getTestResult(){
		TestAstigmatismResult result = new TestAstigmatismResult();
		result.setTrueRate(nowAnswerTrue/((double) QUESTION_NUM) * 100);//计算正确率
		if(result.getTrueRate()<SERIOUS){
			result.setResult(getResources().getString(R.string.test_result_serious));
		}else if(result.getTrueRate()<MEDIUM){
			result.setResult(getResources().getString(R.string.test_result_medium));
		}else if(result.getTrueRate()<LITTLE){
			result.setResult(getResources().getString(R.string.test_result_medium));
		}else {
			result.setResult(getResources().getString(R.string.test_result_normal));
		}
		//获取测试的眼睛
		char eye = rbtnsEye.getCheckedRadioButtonId()==R.id.test_astigmatism_rbtn_eye_left?LEFT_EYE:RIGHT_EYE;
		result.setEye(eye);
		//计算患散光可能性(该部分待定）

		return result;
	}


	/**
	 * 设置眼睛能否选择的状态
	 */
	private void setCanChangeChoseEye(){
		//首先，吧rbtn设置为不可点击
		rbtnLeftEye.setClickable(false);
		rbtnLeftEye.setClickable(false);
		canChangeChoseEye = false;
		//设置提示
		rbtnLeftEye.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Toast.makeText(getActivity(),REMIND_CANNOT_CHANGE_EYE,Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		rbtnRightEye.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Toast.makeText(getActivity(),REMIND_CANNOT_CHANGE_EYE,Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}



	public static String getTestResultKey() {
		return TEST_RESULT_KEY;
	}
}

/**
 * 测试结果保存的类，用于在Activity之间传输数据，所以需要实现Serializable序列化接口
 */
class TestAstigmatismResult implements Serializable {
	private double trueRate;//答题正确率
	private int probability;//患该病概率
	private String result;//测试结果
	private char eye;//测试的眼睛:取值左或者右

	public char getEye() {
		return eye;
	}

	public void setEye(char eye) {
		this.eye = eye;
	}

	public double getTrueRate() {
		return trueRate;
	}

	public void setTrueRate(double trueRate) {
		this.trueRate = trueRate;
	}

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}