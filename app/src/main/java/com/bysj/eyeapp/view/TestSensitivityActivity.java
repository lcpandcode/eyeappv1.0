package com.bysj.eyeapp.view;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

public class TestSensitivityActivity extends BaseActivity {
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


	}






}
