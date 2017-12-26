package com.bysj.eyeapp.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//设置自定义标题
		setupViews();
		//默认首页是视力测试
		//getSupportActionBar().setTitle(R.string.title_test);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
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
		});

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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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


}
