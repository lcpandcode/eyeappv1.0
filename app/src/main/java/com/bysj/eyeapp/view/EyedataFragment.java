package com.bysj.eyeapp.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bysj.eyeapp.dao.EyedataDAO;
import com.bysj.eyeapp.service.EyedataService;
import com.bysj.eyeapp.util.ArcView;
import com.bysj.eyeapp.util.ArcViewUtil;
import com.bysj.eyeapp.util.CustomSwipeRefreshLayout;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.EyedataVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class EyedataFragment extends Fragment {
	private static final String APPLICATION_EYEDARA_TAG = GlobalConst.APPLICATION_EYEDATA_TAG;
	private static final int AUTO_SUBMIT_DATA_TIME = 60 * 5;//每5分钟提交一次用眼数据到数据库

	private LinearLayout arcViewHolder;
	private ArcView arcView;
	private View round;
	private View thisView;
	private TextView openCloseRatio;
	private TextView openScreenTimeToday;
	private TextView openScreenTimeRecent;
	private TextView openScreenCount;
	private TextView indoorTime;
	private TextView outdoorTime;
	private CustomSwipeRefreshLayout swipeRefreshLayout;


	//数据相关
	private EyedataVO eyedata;
	private EyedataService service;
	private Timer timer;//定时器，用于定时刷新用眼数据以保持最新状态
	private TimerTask timerTask;
	private int timerCounter = 0;//计数器，用于计算提交定时提交用眼数据的时间
	private Handler handler;//主线程的handler，用于接收刷新界面的任务
	private Runnable refreshUI = new Runnable() {//刷新UI用的任务
		@Override
		public void run() {
			initEyedata();
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_eyedata, null);
		thisView = view;
		init();
		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Log.d("debug:","onAttach 执行");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d("debug:","onDetach 执行");
	}







	private void init(){
		Log.e("error","test_error_init");
		service = new EyedataService();
		//初始化圆弧数据
		arcView = getArcView();
		arcViewHolder = thisView.findViewById(R.id.eyedata_bar_arc);
		arcViewHolder.addView(arcView,0);
		//获取圆
		round = thisView.findViewById(R.id.eyedata_bar_arc_round);
		openCloseRatio = thisView.findViewById(R.id.eyedata_bar_ratio);
		openScreenTimeToday = thisView.findViewById(R.id.eyedata_bar_openscreentime_today);
		openScreenTimeRecent = thisView.findViewById(R.id.eyedata_bar_openscreentime_recent);
		openScreenCount = thisView.findViewById(R.id.eyedata_bar_openscreencount);
		indoorTime = thisView.findViewById(R.id.eyedata_bar_indoortime);
		outdoorTime = thisView.findViewById(R.id.eyedata_bar_outdoortime);
		handler = new Handler();

		//注册下拉刷新事件
		//初始化下拉刷新功能
		swipeRefreshLayout = (CustomSwipeRefreshLayout)thisView.findViewById(R.id.eyedata_refresh);
		swipeRefreshLayout.setColorSchemeResources(R.color.global_refresh_loadbar_color1,
				R.color.global_refresh_loadbar_color2,R.color.global_refresh_loadbar_color3);
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

		initEyedata();
		//设置定时器，定时刷新数据
		if(timerTask==null){
			timerTask = new TimerTask() {
				@Override
				public void run() {
					//判断日期是否是今天，防止和昨天重复计算
					if(!RegularUtil.dateIsSame(eyedata.getDate(),new Date())){
						//如果已经过了一天,更新数据库并重新初始化eyedata
						updateEyedata();
					}
					eyedata.setOpenScreenTimeCountRecent(eyedata.getOpenScreenTimeCountRecent() + 1);
					eyedata.setOpenScreenTimeCountToday(eyedata.getOpenScreenTimeCountToday() + 1);
					handler.post(refreshUI);//刷新界面
					if(timerCounter>=AUTO_SUBMIT_DATA_TIME){//每隔AUTO_SUBMIT_DATA_TIME秒往数据库保存用眼数据，默认是五分钟
						timerCounter=0;
						service.updateEyedata(getContext(),eyedata);
					}
					//Log.d("debug:","eyedata页面刷新了数据");
				}
			};
		}
		if(timer==null){
			timer = new Timer();
			timer.schedule(timerTask,1000,1000);//每隔一秒更新数据

		}

	}


	/**
	 * 初始化用眼数据
	 */
	GlobalApplication application;
	private void initEyedata(){
		//Log.e("error","test_error_initEyeData");
		//从application中读取数据，如果没有数据，说明是初次加载
		if(application==null){
			application =  (GlobalApplication)getActivity().getApplication();
		}

		eyedata = (EyedataVO) application.getGlobalVar(APPLICATION_EYEDARA_TAG);
		if(eyedata==null){
			eyedata = service.getEyedataToday(getActivity());
			application.putGlobalVar(APPLICATION_EYEDARA_TAG,eyedata);
		}

		//计算待机与开屏时间比例
		String ratioStr = countRatio(eyedata.getOpenScreenTimeCountToday(),
				3600*24-eyedata.getOpenScreenTimeCountToday());
		openCloseRatio.setText(ratioStr);

		//初始化圆弧区域数据
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		int ratio = eyedata.getOpenScreenTimeCountToday()/(3600*24-eyedata.getOpenScreenTimeCountToday());
		float [] position = caculateRoundPosition(ratio+0.1F);//待完善
		int parentMarginLeft = (int)dpToPx(30);//注意：由于设置了边距，这里要补上！
		int parentMarginTop = (int)dpToPx(0);//注意：由于设置了边距，这里要补上！
		params.leftMargin = (int)position[1] + parentMarginLeft;
		params.topMargin = (int)position[0] + parentMarginTop;
		round.setLayoutParams(params);


		openScreenTimeToday.setText(secondToHour(eyedata.getOpenScreenTimeCountToday()));

		//计算最近一次截止至目前的开屏时间
		openScreenTimeRecent.setText(secondToHour(eyedata.getOpenScreenTimeCountRecent()));

		//计算开屏次数
		openScreenCount.setText(eyedata.getOpenScreenCount() + "");

		indoorTime.setText(secondToHour(eyedata.getIndoorTime()));
		outdoorTime.setText(secondToHour(eyedata.getOutdoorTime()));
	}

	/**
	 * 更新eyedata对象以及数据库中数据，该方法主要是在当天过后，自动更新eyedata对象以及数据库
	 */
	private void updateEyedata(){
		//更新数据库
		service.updateEyedata(getActivity(),eyedata);
		//更新eyedata
		eyedata.setDate(new Date());
		eyedata.setOpenScreenTimeCountRecent(0);
		eyedata.setOpenScreenTimeCountToday(0);
		eyedata.setOpenScreenCount(0);
		eyedata.setOutdoorTime(0);
		eyedata.setOutdoorTime(0);

		//新插入当天的记录
		service.addEyedata(getActivity(),eyedata);

	}

	/**
	 * 画圆弧的方法，用于描绘界面中的圆弧
	 */
	private ArcView getArcView(){
		ArcView.ArcViewParams arcParams = new ArcView.ArcViewParams();
		ArcView arcView = new ArcView(getActivity(),arcParams);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		arcView.setLayoutParams(params);
		return arcView;
	}

	/**
	 *	计算圆型位置的方法，返回两个坐标值：一个是距离父控件左边的距离，一个是距离父控件上边的距离
	 * 其中，父控件的大小由圆弧大小决定
	 * @param rate 比的结果：即开屏与待机时间比
	 * @return 坐标值：距右边和距上边距离
	 */
	private float [] caculateRoundPosition(float rate){
		ArcView.ArcViewParams params = arcView.getParams();
		float width = params.getRight() - params.getLeft() ;
		float height = params.getBottom() - params.getTop();
		float r = width/2;
		float angle = (float) Math.PI * rate;
		float [] result = new float[2];
		double d = r * Math.sin(angle);
		float f = (float)d;
		result[0] = r - (float) (r * Math.sin(angle));
		result[1] = r - (float) (r * Math.cos(angle));
		return result;
	}

	/**
	 * 刷新方法
	 */
	public void refresh(){
		Log.d("debug:","eyedata界面刷新数据！");
		initEyedata();
		CustomToast.showToast(getActivity(),GlobalConst.REMIND_REFRESH_SUCCESS);
	}


	/**
	 * dp 和 px之间的转换
	 */
	Resources rs;
	private float dpToPx(float dpSize){
		if(rs==null){
			rs = getResources();
		}
		DisplayMetrics dm = rs.getDisplayMetrics() ;
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
	}

	/**
	 * 计算两个int的比率，以字符串返回a:b的形式
	 * @param
	 * @param
	 */
	private String countRatio(int num1,int num2){
		//查找最大公约数
		int maxCommonDivisor = findMaxCommonDivisor(num1,num2);
		num1 /= maxCommonDivisor;
		num2 /= maxCommonDivisor;
		return String.format("%d:%d",num1,num2);
	}
	private int findMaxCommonDivisor(int a,int b){
		if(a<b){
			int temp;
			temp=a;
			a=b;
			b=temp;
		}
		if(0==b){
			return a;
		}
		return findMaxCommonDivisor(b,a%b);
	}

	/**
	 * 把秒转换问x小时x分x秒的形式
	 * @param second 总秒数
	 * @return map:hour-val,min-val,sec-val
	 */
	private String secondToHour(int second){
		int hour = second/3600;
		second %= 3600;
		int min = second/60;
		int sec = second%60;
		return String.format("%d小时%d分钟%d秒",hour,min,sec);
	}




}
