package com.bysj.eyeapp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.TestService;
import com.bysj.eyeapp.util.CustomSwipeRefreshLayout;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.TestQuestionVO;
import com.bysj.eyeapp.vo.TestVisionQuestionVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestVisionFragment extends Fragment {
	//字符串常量
	final private static int VISION_NUM = 4;//作答个数默认10
	final private static int QUESTION_NUM = VISION_NUM * 12;
	final private static double SERIOUS = 0.5;//阈值：答题正确率小于SERIOUS判断测试结果为严重
	final private static double MEDIUM = 0.7;//阈值：答题正确率小于MEDIUM判断测试结果为中等
	final private static double LITTLE = 0.8;//阈值：答题正确率小于LITTLE判断测试结果为轻微患病
	final private static String TEST_RESULT_KEY = "视力测试结果";
	final private static String REMIND_CANNOT_CHANGE_EYE = "您已开始答题，不能改变选中的眼睛";
	final private static String REMIND_CHOSE_EYE = "请先选择您要测试的眼睛！";
	final private static String REMIND_CHOSE_OPTION = "请选择您认为正确的选项！";
	final private static String REMIND_INPUT_DISTANCE = "请输入您和手机屏幕距离";
	final private static String REMIND_INPUT_DISTANCE_NOT_NUM = "请输入合法数字";
	final private static char LEFT_EYE = '左';
	final private static char RIGHT_EYE = '右';
	final private static int TOAST_MAX_RANGE_REMIND_SHOW_TIME = 1000 * 8;//距离传感器提示显示时间，8秒
	//数据相关变量
	//类数据相关变量
	private int nowAnswerQuestion = 0;//当前作答数目
	private List<TestVisionQuestionVO> questions;//题目列表
	private int nowAnswerTrue = 0;//当前作答正确个数
	private TestService service;//核心服务类，service层的类
	private boolean isChoseEye = false;//会否选择了眼睛，如果否提示选择眼睛方可进行测试
	private boolean canChangeChoseEye = true;//能否改变选中的眼睛：答题开始后（即已经至少答了一题）不能改变眼睛选择
	private int nowCheckedId = -1;//由于自定义线性布局嵌套radioButton，用RadioGroup的getCheckedId无法获取对应id，所以设置该全局变量存储当前选中的选项的id
	private float testDistance = 0.0f;

	private float visionValResult = 4.0f;//视力测试结果存储变量
	private int visionValFalseCount = 0;//当前视力值下大体错误个数，超过两个图标错误则视为用户最大视力只能到达该值

	private Map<Float,String> mapVision = new HashMap<>();
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
	private AlertDialog testDistanceAlertDialog;
	private EditText testDistanceEditText;
	private TextView  testDistanceTextView;

//	private SensorManager sensorManager;//传感器管理对象
//	private Sensor proximitySensor;//距离传感器

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
		//初始化传感器
//		sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
//		proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//		float maxRange = proximitySensor.getMaximumRange();
//		String maxRangeStr = new DecimalFormat("##0.00").format(maxRange);//保留两位小数
//		CustomToast.showToast(getActivity(),String.format("提示：您的距离传感器支持最大距离为%s,请不要把放置距离超过该距离，否则将导致测试不准确！",maxRangeStr));
//		CustomToast.setShowTime(TOAST_MAX_RANGE_REMIND_SHOW_TIME);
//		Sensor proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//		Boolean res=sensorManager.registerListener(new ProximityListener(),proximitySensor,SensorManager.SENSOR_DELAY_FASTEST);
//		if(!res){//绑定失败，提示
//			CustomToast.showToast(getActivity(),REMIND_BIND_SENSOR_FAIL);
//			return;
//		}
		initMapVision();
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

		//初始化测试距离
		initDistance();
	}

	/**
	 * 初始化mapVision:主要记录视力值和近视度数的大概关系
	 */
	private void initMapVision(){
		float [] VISION_ARR = new float[]{4.0f,4.1f,4.2f,4.3f,4.4f,4.5f,4.6f,4.7f,4.8f,4.9f,5.0f,5.1f,5.2f};
		for(float i : VISION_ARR){
			if(i==4.0){
				mapVision.put(i,"近视大于650度");
			}else if(i==4.1){
				mapVision.put(i,"近视550-600度");
			}else if(i==4.2){
				mapVision.put(i,"近视500-550度");
			}else if(i==4.3){
				mapVision.put(i,"近视450-500度");
			}else if(i==4.4){
				mapVision.put(i,"近视400-450度");
			}else if(i==4.5){
				mapVision.put(i,"近视300-350度");
			}else if(i==4.6){
				mapVision.put(i,"250-300度");
			}else if(i==4.7){
				mapVision.put(i,"近视200-250度");
			}else if(i==4.8){
				mapVision.put(i,"近视150-200度");
			}else if(i==4.9){
				mapVision.put(i,"近视100-150度");
			}else{
				mapVision.put(i,"视力正常，无近视");
			}
		}
	}

	/**
	 * 初始距离输入弹出框
	 */
	private void initDistance(){
		//初始化距离输入弹出框
		LayoutInflater li = LayoutInflater.from(getActivity());
		View promptsView = li.inflate(R.layout.view_test_vision_distanceinput_prompt, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setView(promptsView);
		testDistanceEditText = (EditText) promptsView.findViewById(R.id.test_vision_distance_input);
		testDistanceTextView = (TextView) promptsView.findViewById(R.id.test_vision_distance_textview);
		final EditText userInput = testDistanceEditText;
		final TextView textView = testDistanceTextView;
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								String result = userInput.getText().toString();
								if(RegularUtil.strIsEmpty(result)){
//									textView.setText(REMIND_INPUT_DISTANCE);
//									textView.setTextColor(getActivity().getResources()
//											.getColor(R.color.test_vision_distance_input_remind));
									CustomToast.showToast(getActivity(),REMIND_INPUT_DISTANCE);
									initDistance();
									return;
								}else if(!RegularUtil.strCanToFloat(result)){
//									textView.setText(REMIND_INPUT_DISTANCE_NOT_NUM);
//									textView.setTextColor(getActivity().getResources()
//											.getColor(R.color.test_vision_distance_input_remind));
									CustomToast.showToast(getActivity(),REMIND_INPUT_DISTANCE_NOT_NUM);
									initDistance();
									return;
								}else if(Float.parseFloat(result)>100 || Float.parseFloat(result)<50){
									CustomToast.showToast(getActivity(),"为确保图标显示效果，请输入50-100之间的数字");
									initDistance();
									return;
								}
								testDistance = Float.parseFloat(result);
								//初始化数据
								questions = service.getVisionQuestions(testDistance,VISION_NUM);
								//初始化第一个答题页面
								//nextQuestion();
 								showNewQuestion(questions.get(nowAnswerQuestion));
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});

		testDistanceAlertDialog = alertDialogBuilder.create();
		testDistanceAlertDialog.show();
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
		//判断是否输入了距离
		if(testDistance==0.0){
			testDistanceAlertDialog.show();
			return;
		}
		//判断是否选择了眼睛
		if(!isChoseEye){
			CustomToast.showToast(getActivity(),REMIND_CHOSE_EYE);
			return ;
		}
		//是否选中了选项
		int checkedId = nowCheckedId;
		if(checkedId==-1){
			CustomToast.showToast(getActivity(),REMIND_CHOSE_OPTION);
			return ;
		}
		//一旦开始答题，不能改变选中的眼睛
		setCanChangeChoseEye();

		//判断答题情况，并更新答题状态
		judgeTest();
		if(visionValFalseCount>=2){
			//结束答题
			//作答完成，获取答题结果
			TestVisionResult result = getTestResult();
			Intent intent = new Intent();
			intent.putExtra(TEST_RESULT_KEY,result);
			intent.setClass(thisView.getContext(),TestVisionResultActivity.class);
			startActivity(intent);
		}
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
		//更新对应的视力值记录结果值
		if(question.getVisionVal()!=visionValResult){
			visionValResult = question.getVisionVal();
			visionValFalseCount = 0;//新的视力值，上一个视力值结果计算清零
		}
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
		char trueOption = question.getDirection();
		int trueOptionId = -1;
		switch (trueOption){
			case '上':
				trueOptionId = 1;
				break;
			case '下':
				trueOptionId = 2;
				break;
			case '左':
				trueOptionId = 3;
				break;
			case '右':
				trueOptionId = 4;
				break;
		}
		if(trueOptionId==choseOption){
			//正确题目数加1
			nowAnswerTrue++;
		}else {
			visionValFalseCount++;
		}

	}


	/**
	 * 更新页面的问题区域（待完善）
	 */
	private void showNewQuestion(TestVisionQuestionVO question){
		//设置题目图片
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,0);
		layoutParams.height = (int)mmToPx(question.getSize());// 设置图片的高度
		layoutParams.width = (int)mmToPx(question.getSize()); // 设置图片的宽度
		questionImg.setLayoutParams(layoutParams);
//		questionImg.setMaxHeight((int)mmToPx(question.getSize()));
//		questionImg.setMaxWidth((int)mmToPx(question.getSize()));
		//旋转图片
		questionImg.setPivotX(layoutParams.height/2);
		questionImg.setPivotY(layoutParams.height/2);//支点在图片中心
		float rotateAngle = 0.0f;
		switch (question.getDirection()) {
			case '右':
				rotateAngle = 0f;
				break;
			case '上':
				rotateAngle = 270f;
				break;
			case '下':
				rotateAngle = 90f;
				break;
			case '左':
				rotateAngle = 180f;
				break;
		}
		questionImg.setRotation(rotateAngle);
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
		DecimalFormat df = new DecimalFormat("######0.00");
		double trueRate = nowAnswerTrue/((double) (nowAnswerQuestion + 1)) * 100;
		trueRate = Float.parseFloat(df.format(trueRate));
		result.setTrueRate(trueRate);//计算正确率
//		if(result.getTrueRate()<SERIOUS){
//			result.setResult(getResources().getString(R.string.test_result_serious));
//		}else if(result.getTrueRate()<MEDIUM){
//			result.setResult(getResources().getString(R.string.test_result_medium));
//		}else if(result.getTrueRate()<LITTLE){
//			result.setResult(getResources().getString(R.string.test_result_medium));
//		}else {
//			result.setResult(getResources().getString(R.string.test_result_normal));
//		}
		result.setResult(mapVision.get(visionValResult));
		result.setVision(visionValResult + "");
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
		visionValFalseCount = 0;
		visionValResult = 4.0f;
		testDistance = 0.0f;
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
		if(testDistance==0.0){
			testDistanceAlertDialog.show();
			return;
		}
		questionsNew = service.getVisionQuestions(testDistance,VISION_NUM);

		if(questionsNew.size()==0 || questionsNew==null){
			CustomToast.showToast(getActivity(),GlobalConst.REMIND_NET_ERROR);
			return ;
		}
		//清空数据
		clearData();

		//重新初始化数据
		questions = questionsNew;
		showNewQuestion(questions.get(nowAnswerQuestion));
		CustomToast.showToast(getActivity(),GlobalConst.REMIND_REFRESH_SUCCESS);
	}

	/**
	 * mm 转换为px
	 * @param mm
	 * @return
	 */
	private float mmToPx(float mm){
		WindowManager wm1 = getActivity().getWindowManager();
		int width = wm1.getDefaultDisplay().getWidth();
		int height1 = wm1.getDefaultDisplay().getHeight();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		double screen = Math.sqrt(Math.pow(screenWidth,2) + Math.pow(screenHeight,2));
		double inchPx = screen/getScreenSizeOfDevice();//每英寸含有的像素点个数
		//计算px和mm的关系
		double mmPx = inchPx / (2.54*10);//每毫米含有多少个像素
		return (float)(mmPx * mm);
	}

	/**
	 * 获取屏幕物理尺寸
	 * @return 尺寸大小，单位是
	 */
	private float getScreenSizeOfDevice() {

		double screenInches = 0.0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			Point point = new Point();
			getActivity().getWindowManager().getDefaultDisplay().getRealSize(point);

			DisplayMetrics dm = getResources().getDisplayMetrics();
			double x = Math.pow(point.x/ dm.xdpi, 2);
			double y = Math.pow(point.y / dm.ydpi, 2);
			screenInches = Math.sqrt(x + y);
		}else {
			DisplayMetrics dm = getResources().getDisplayMetrics();
			int width=dm.widthPixels;
			int height=dm.heightPixels;
			double x = Math.pow(width,2);
			double y = Math.pow(height,2);
			double diagonal = Math.sqrt(x+y);
			int dens=dm.densityDpi;
			screenInches = diagonal/(double)dens;
		}

		//CustomToast.showToast(getActivity(),screenInches + "");
		return (float)screenInches;
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
	private String vision;

	public String getVision() {
		return vision;
	}

	public void setVision(String vision) {
		this.vision = vision;
	}

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