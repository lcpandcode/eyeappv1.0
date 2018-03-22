package com.bysj.eyeapp.view;


import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.bysj.eyeapp.service.EyedataService;
import com.bysj.eyeapp.service.TestService;
import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.ScreenObserver;
import com.bysj.eyeapp.vo.EyedataVO;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {
	//字符串常量
	final static String tabSpaceTabTest = "test";
	final static String tabSpaceTabPerson = "person";
	final static String tabSpaceTabKnowledge = "knowledge";
	final static String tabSpaceTabBlurayfilt = "blurayfilt";
	final static String tabSpaceTabEyedata = "eyedata";

	// tab用参数
	private TabHost tabHost;
	private RadioGroup radiogroup;
	private int menuid;

	//数据相关变量
	private EyedataService eyedataService;
	private ScreenObserver screenObserver;
	private EyedataVO eyedataVO;
	GlobalApplication application;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init(){
		//初始化数据
		application = (GlobalApplication)getApplication();
		eyedataVO = (EyedataVO)application.getGlobalVar(GlobalConst.APPLICATION_EYEDATA_TAG);
		if(eyedataVO==null){
			//读取数据库用眼数据
			//如果今天的用眼记录已经存在，直接查询，否则新加入一条用眼记录
			if(!eyedataService.judgeTodayEyedataExist(application)){
				eyedataVO = new EyedataVO();
				eyedataService.addEyedata(application,eyedataVO);
			}else {
				eyedataVO = eyedataService.getEyedataToday(application);
			}
			application.putGlobalVar(GlobalConst.APPLICATION_EYEDATA_TAG,eyedataVO);
		}

		//初始化自定义标题
		setupViews((TextView) findViewById(R.id.text_title),null,null);
		radiogroup = (RadioGroup) findViewById(R.id.footer_rbtns);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		eyedataService = new EyedataService();

		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec(tabSpaceTabTest).setIndicator(tabSpaceTabTest)
				.setContent(R.id.fragment_test));
		tabHost.addTab(tabHost.newTabSpec(tabSpaceTabPerson).setIndicator(tabSpaceTabPerson)
				.setContent(R.id.fragment_person));
		tabHost.addTab(tabHost.newTabSpec(tabSpaceTabKnowledge).setIndicator(tabSpaceTabKnowledge)
				.setContent(R.id.fragment_knowledge));
		tabHost.addTab(tabHost.newTabSpec(tabSpaceTabBlurayfilt).setIndicator(tabSpaceTabBlurayfilt)
				.setContent(R.id.fragment_blurayfilt));
		tabHost.addTab(tabHost.newTabSpec(tabSpaceTabEyedata).setIndicator(tabSpaceTabEyedata)
				.setContent(R.id.fragment_eyedata));
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				footerMenuChange(checkedId);
			}

		});
		//注册开屏监听事件
		screenObserver = new ScreenObserver(getApplicationContext());
		screenObserver.setScreenStateListener(new ScreenObserver.ScreenStateListener() {
			@Override
			public void onScreenOn() {
				Log.d("开屏事件：","------>开屏");
				//更新用眼数据
				openScreenRefreshEyedata();
			}

			@Override
			public void onScreenOff() {
				Log.d("闭屏事件：","------>关闭屏");
				//更新用眼数据
				closeScreenRefreshEyedata();
			}

			@Override
			public void onUserPresent() {//解锁触发的方法

				Log.d("test","testwww");
				System.out.println("解锁了屏幕");
			}
		});
	}

	/**
	 * 底部功能按钮切换方法
	 */

	private void footerMenuChange(int checkedId){
		menuid = checkedId;
		int currentTab = tabHost.getCurrentTab();
		switch (checkedId) {
			case R.id.radio_test:
				tabHost.setCurrentTabByTag(tabSpaceTabTest);
				//如果需要动画效果就使用
				//setCurrentTabWithAnim(currentTab, 0, tabSpaceTabTest);
				setTitle(R.string.title_test);
				showBackwardView(R.string.title_btn_return,true);
				showForwardView(R.string.title_btn_confirm,true);
				break;
			case R.id.radio_person:
				tabHost.setCurrentTabByTag(tabSpaceTabPerson);
				setTitle(R.string.title_person);
				showBackwardView(-1,false);
				showForwardView(-1,false);
				break;
			case R.id.radio_knowledge:
				tabHost.setCurrentTabByTag(tabSpaceTabKnowledge);
				setTitle(R.string.title_knowledge);
				showBackwardView(R.string.title_btn_return,true);
				showForwardView(R.string.title_btn_confirm,true);
				break;
			case R.id.radio_blurayfilt:
				tabHost.setCurrentTabByTag(tabSpaceTabBlurayfilt);
				setTitle(R.string.title_blurayfilt);
				showBackwardView(R.string.title_btn_return,true);
				showForwardView(-1,false);
				break;
			case R.id.radio_eyedata:
				tabHost.setCurrentTabByTag(tabSpaceTabEyedata);
				setTitle(R.string.title_eyedata);
				showBackwardView(-1,false);
				showForwardView(R.string.title_btn_confirm,true);
		}
		// 刷新actionbar的menu
		getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		switch (menuid) {
			case R.id.radio_test:
				getMenuInflater().inflate(R.menu.menu_bar_demo, menu);
				break;
			case R.id.radio_person:
				menu.clear();
				break;
			case R.id.radio_knowledge:
				menu.clear();
				break;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// 这个方法是关键，用来判断动画滑动的方向
	private void setCurrentTabWithAnim(int now, int next, String tag) {
		if (now > next) {
			tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			tabHost.setCurrentTabByTag(tag);
			tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
		} else {
			tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			tabHost.setCurrentTabByTag(tag);
			tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
		}
	}

	/**
	 * 开屏触发提交用眼数据：包括开屏次数+1，开屏时间累计（设置开屏时间），数据均放在全局application中的EyedataVO对象中
	 */
	private void openScreenRefreshEyedata(){

		//今天开屏次数+1
		eyedataVO.setOpenScreenCount(eyedataVO.getOpenScreenCount() + 1);
		eyedataVO.setOpenScreenTimeCountRecent(0);//本次时长变为0
		//更新当次开屏时间
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				eyedataService.updateEyedata(getApplicationContext(),eyedataVO);
//				Log.d("开屏更新数据:","开屏，更新用眼数据成功");
//				Log.d("当前用眼数据详情：",eyedataVO.toString());
//			}
//		}).start();
//		eyedataVO.setRecentOpenScreenTime(new SimpleDateFormat(GlobalConst.DATETIME_PATTERN).format(new Date()));
	}

	/**
	 * 关闭屏幕触发提交用眼数据：主要是更新今日开屏时间累计
	 */
	private void closeScreenRefreshEyedata(){

//		Date dateRecentOpenScreen = null;
//		try {
//			dateRecentOpenScreen = new SimpleDateFormat(GlobalConst.DATETIME_PATTERN).parse(eyedataVO.getRecentOpenScreenTime());
//
//		} catch (ParseException e) {
//			Log.d("错误：","日期格式错误");
//			return ;
//		}
//		Date dateNow = new Date();
//
//		int recentOpenSreenCount = (int)((dateNow.getTime() - dateRecentOpenScreen.getTime())/1000);
//		Log.d("recentOpenSreenCount",recentOpenSreenCount + "");
//		eyedataVO.setOpenScreenTimeCountToday(eyedataVO.getOpenScreenTimeCountToday() + recentOpenSreenCount);
//		Log.d("begin close:","开始关闭");
//		//由于这里很卡，异步更新
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				eyedataService.updateEyedata(application,eyedataVO);
//				Log.d("数据库：","数据库操作成功");
//				Log.d("更新数据:","更新用眼数据成功");
//				Log.d("当前用眼数据详情：",eyedataVO.toString());
//			}
//		}).start();
//		Log.d("end close:","结束关闭");

	}


	/**
	 * 判断是否在室内,该方法被定时任务所执行
	 * @return 布尔判断结果
	 */
	private boolean isIndoorNow(){
		return false;
	}

	public TabHost getTabHost(){
		return  this.tabHost;
	}






}
