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
		//默认首页是视力测试
		getSupportActionBar().setTitle(R.string.title_test);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("test").setIndicator("test")
				.setContent(R.layout.fragment_test));
		tabHost.addTab(tabHost.newTabSpec("mycenter").setIndicator("mycenter")
				.setContent(R.layout.fragment_person));
		tabHost.addTab(tabHost.newTabSpec("search").setIndicator("search")
				.setContent(R.layout.fragment_knowledge));
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				menuid = checkedId;
				int currentTab = tabHost.getCurrentTab();
				switch (checkedId) {
					case R.id.radio_test:
						tabHost.setCurrentTabByTag("main");
						//如果需要动画效果就使用
						//setCurrentTabWithAnim(currentTab, 0, "main");
						getSupportActionBar().setTitle(R.string.title_test);
						break;
					case R.id.radio_person:
						//tabHost.setCurrentTabByTag("mycenter");
						setCurrentTabWithAnim(currentTab, 1, "mycenter");
						getSupportActionBar().setTitle(R.string.title_person);

						break;
					case R.id.radio_knowledge:
						tabHost.setCurrentTabByTag("search");
						getSupportActionBar().setTitle(R.string.title_knowledge);
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
