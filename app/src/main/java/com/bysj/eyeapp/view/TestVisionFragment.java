package com.bysj.eyeapp.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.TestService;
import com.bysj.eyeapp.util.CustomSwipeRefreshLayout;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.vo.TestQuestionVO;
import com.bysj.eyeapp.vo.TestVisionQuestionVO;

import java.io.Serializable;
import java.util.List;

public class TestVisionFragment extends Fragment {
	//字符串常量
	final private static int QUESTION_NUM = 2;//作答个数默认10
	final private static double SERIOUS = 0.5;//阈值：答题正确率小于SERIOUS判断测试结果为严重
	final private static double MEDIUM = 0.7;//阈值：答题正确率小于MEDIUM判断测试结果为中等
	final private static double LITTLE = 0.8;//阈值：答题正确率小于LITTLE判断测试结果为轻微患病
	final private static String TEST_RESULT_KEY = "视力测试结果";
	final private static String REMIND_CANNOT_CHANGE_EYE = "您已开始答题，不能改变选中的眼睛";
	final private static String REMIDN_CHOSE_EYE = "请先选择您要测试的眼睛！";
	final private static String REMIDN_CHOSE_OPTION = "请选择您认为正确的选项！";
	final private static char LEFT_EYE = '左';
	final private static char RIGHT_EYE = '右';
	//数据相关变量
	//类数据相关变量
	private int nowAnswerQuestion = 0;//当前作答数目
	private List<TestVisionQuestionVO> questions;//题目列表
	private int nowAnswerTrue = 0;//当前作答正确个数
	private TestService service;//核心服务类，service层的类
	private boolean isChoseEye = false;//会否选择了眼睛，如果否提示选择眼睛方可进行测试
	private boolean canChangeChoseEye = true;//能否改变选中的眼睛：答题开始后（即已经至少答了一题）不能改变眼睛选择
	private int nowCheckedId = -1;//由于自定义线性布局嵌套radioButton，用RadioGroup的getCheckedId无法获取对应id，所以设置该全局变量存储当前选中的选项的id

	//控件相关变量
	private View thisView;
	private Button btnNext ;
	private ImageView questionImg;
	private RadioGroup rbtnsEye;//选择研究对应的按钮
	private RadioGroup options;
	private RadioButton option1;
	private RadioButton option2;
	private RadioButton option3;
	private RadioButton option4;
	private RadioButton rbtnLeftEye;
	private RadioButton rbtnRightEye;
	private CustomSwipeRefreshLayout swipeRefreshLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_test_vision, null);
		thisView = view;
		init(view);
		return view;
	}

	/**
	 * 初始化方法
	 */
	private void init(View view){
		//初始化控件变量
		btnNext = thisView.findViewById(R.id.test_vision_nextbtn);
		questionImg = thisView.findViewById(R.id.test_question_vision_img);
		options = thisView.findViewById(R.id.test_vision_options);
		option1 = thisView.findViewById(R.id.test_vision_option1);
		option2 = thisView.findViewById(R.id.test_vision_option2);
		option3 = thisView.findViewById(R.id.test_vision_option3);
		option4 = thisView.findViewById(R.id.test_vision_option4);
		rbtnsEye = thisView.findViewById(R.id.test_vision_rbtns);
		rbtnLeftEye = thisView.findViewById(R.id.test_vision_rbtn_eye_left);
		rbtnRightEye = thisView.findViewById(R.id.test_vision_rbtn_eye_right);
		service = new TestService();
		//初始化数据
		questions = service.getVisionQuestions(QUESTION_NUM);
		//初始化第一个答题页面
		showNewQuestion(questions.get(nowAnswerQuestion));
		//设置监听事件
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionChange(v);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionChange(v);
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionChange(v);
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionChange(v);
            }
        });
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

		//设置下拉刷新
		//初始化下拉刷新功能
		swipeRefreshLayout = (CustomSwipeRefreshLayout)thisView.findViewById(R.id.test_vision_refresh);
		//swipeRefreshLayout.setmListView((ListView) thisView.findViewById(R.id.));

		//设置刷新时动画的颜色，可以设置4个
		swipeRefreshLayout.setColorSchemeResources(R.color.global_refresh_loadbar_color1,
				R.color.global_refresh_loadbar_color2,R.color.global_refresh_loadbar_color3);
		//设置下拉刷新事件
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				//tv.setText("正在刷新");
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//tv.setText("刷新完成");
						refresh();
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 0);
			}
		});
	}

	/**
	 * 选项控制按钮，解决RadioButtonGroup无法线性布局的问题
	 */
	public void optionChange(View view){
		int checkedId = view.getId();
		Resources rs = getContext().getResources();
		RadioButton option1 = thisView.findViewById(R.id.test_vision_option1);
		RadioButton option2 = thisView.findViewById(R.id.test_vision_option2);
		RadioButton option3 = thisView.findViewById(R.id.test_vision_option3);
		RadioButton option4 = thisView.findViewById(R.id.test_vision_option4);
		//所有设置为没有选中
		option1.setChecked(false);
		option2.setChecked(false);
		option3.setChecked(false);
		option4.setChecked(false);
		//设置选中按钮
		switch (checkedId)  {
			case R.id.test_vision_option1 :
				option1.setChecked(true);
				break;
			case R.id.test_vision_option2 :
				option2.setChecked(true);
				break;
			case R.id.test_vision_option3 :
				option3.setChecked(true);
				break;
			case R.id.test_vision_option4 :
				option4.setChecked(true);
				break;
		}
		nowCheckedId = checkedId;
	}

	/**
	 * 下一题按钮对应的事件
	 */
	private void nextQuestion(){
		//判断是否选择了眼睛
		if(!isChoseEye){
			CustomToast.showToast(getActivity(),REMIDN_CHOSE_EYE);
			return ;
		}
		//是否选中了选项
		int checkedId = nowCheckedId;
		if(checkedId==-1){
			CustomToast.showToast(getActivity(),REMIDN_CHOSE_OPTION);
			return ;
		}
		//一旦开始答题，不能改变选中的眼睛
		setCanChangeChoseEye();
		//判断答题情况，并更新答题状态
		judgeTest();
		nowAnswerQuestion++;
		if(nowAnswerQuestion<QUESTION_NUM){
			//获取下一题
			TestVisionQuestionVO question = questions.get(nowAnswerQuestion);
			//清除上题的选项
			options.clearCheck();
			//更新页面问题
			showNewQuestion(question);
		}else {
			//作答完成，获取答题结果
			TestVisionResult result = getTestResult();
			Intent intent = new Intent();
			intent.putExtra(TEST_RESULT_KEY,result);
			intent.setClass(thisView.getContext(),TestVisionResultActivity.class);
			startActivity(intent);
		}

	}

	/**
	 * 判断当前答题是否正确
	 */
	private void judgeTest(){
		//防止返回点击页面报异常，判断答题数目是否超过了题目个数
		if(nowAnswerQuestion>=questions.size()){
			return ;
		}
		TestVisionQuestionVO question = questions.get(nowAnswerQuestion);
		//获取选项值
		int checkId = nowCheckedId;
		int choseOption = 0;
		switch (checkId) {
			case R.id.test_vision_option1:
				choseOption = 1;
				break;
			case R.id.test_vision_option2:
				choseOption = 2;
				break;
			case R.id.test_vision_option3:
				choseOption = 3;
				break;
			case R.id.test_vision_option4:
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
	private void showNewQuestion(TestVisionQuestionVO question){
		//设置图片展示的src属性待完善（需要发起请求获得Bitmap）

		//清除选中的值
		nowCheckedId = -1;
		option1.setChecked(false);
		option2.setChecked(false);
		option3.setChecked(false);
		option4.setChecked(false);

	}

	/**
	 * 计算作答结果方法（该方法未完成，如何计算患病概率带定）
	 */
	private TestVisionResult getTestResult(){
		TestVisionResult result = new TestVisionResult();
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
		char eye = rbtnsEye.getCheckedRadioButtonId()==R.id.test_vision_rbtn_eye_left?LEFT_EYE:RIGHT_EYE;
		result.setEye(eye);

		return result;
	}


	/**
	 * 设置眼睛能否选择的状态
	 */
	private void setCanChangeChoseEye() {
		//首先，吧rbtn设置为不可点击
		rbtnLeftEye.setClickable(false);
		rbtnRightEye.setClickable(false);
		canChangeChoseEye = false;
		//设置提示
		rbtnLeftEye.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(canChangeChoseEye){
					return false;
				}
				CustomToast.showToast(getActivity(),REMIND_CANNOT_CHANGE_EYE);
				return false;
			}
		});
		rbtnRightEye.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(canChangeChoseEye){
					return false;
				}
				CustomToast.showToast(getActivity(),REMIND_CANNOT_CHANGE_EYE);
				return false;
			}
		});
	}


	/**
	 * 清楚页面数据
	 */
	private void clearData(){
		nowAnswerQuestion = 0;
		questions.clear();
		nowAnswerTrue = 0;
		//清空选中状态
		options.clearCheck();
		rbtnsEye.clearCheck();
		rbtnLeftEye.setClickable(true);
		rbtnRightEye.setClickable(true);
		isChoseEye = false;
		canChangeChoseEye = true;
	}

	/**
	 * 下拉刷新方法
	 */
	private void refresh(){
		List<TestVisionQuestionVO> questionsNew;
		questionsNew = service.getVisionQuestions(QUESTION_NUM);
//		try {
//			questionsNew = service.getVisionQuestions(QUESTION_NUM);
//		} catch (HttpException e) {
//			Log.e("网络错误：" ,e.getMessage());
//			CustomToast.showToast(getActivity(),GlobalConst.REMIND_NET_ERROR);
//			return ;
//		}
		if(questionsNew.size()==0 || questionsNew==null){
			CustomToast.showToast(getActivity(),GlobalConst.REMIND_NET_ERROR);
			return ;
		}
		//成功请求数据，清空数据
		clearData();

		//重新初始化数据
		questions = questionsNew;
		showNewQuestion(questions.get(nowAnswerQuestion));
		CustomToast.showToast(getActivity(),GlobalConst.REMIND_REFRESH_SUCCESS);
	}


	public static String getTestResultKey(){
		return TEST_RESULT_KEY;
	}
}

/**
 * 测试结果保存的类，用于在Activity之间传输数据，所以需要实现Serializable序列化接口
 */
class TestVisionResult implements Serializable {
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