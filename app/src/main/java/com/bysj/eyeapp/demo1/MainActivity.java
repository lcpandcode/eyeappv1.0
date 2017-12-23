package com.bysj.eyeapp.demo1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
	// tab用参数
	private TabHost tabHost;
	private RadioGroup radiogroup;
	private int menuid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setTitle("首页");
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("main").setIndicator("main")
				.setContent(R.id.fragment_main));
		tabHost.addTab(tabHost.newTabSpec("mycenter").setIndicator("mycenter")
				.setContent(R.id.fragment_mycenter));
		tabHost.addTab(tabHost.newTabSpec("search").setIndicator("search")
				.setContent(R.id.fragment_search));
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				menuid = checkedId;
				int currentTab = tabHost.getCurrentTab();
				switch (checkedId) {
					case R.id.radio_main:
						tabHost.setCurrentTabByTag("main");
						//如果需要动画效果就使用
						//setCurrentTabWithAnim(currentTab, 0, "main");
						getSupportActionBar().setTitle("首页");
						break;
					case R.id.radio_mycenter:
						//tabHost.setCurrentTabByTag("mycenter");
						setCurrentTabWithAnim(currentTab, 1, "mycenter");
						getSupportActionBar().setTitle("个人中心");

						break;
					case R.id.radio_search:
						tabHost.setCurrentTabByTag("search");
						getSupportActionBar().setTitle("搜索");
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
			case R.id.radio_main:
				getMenuInflater().inflate(R.menu.menu_bar_demo, menu);
				break;
			case R.id.radio_mycenter:
				menu.clear();
				break;
			case R.id.radio_search:
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
