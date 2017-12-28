package com.bysj.eyeapp.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

public class TestFragment extends Fragment {
	//相关组件定义
	private RadioGroup radioGroup;

//	//字符串常量定义处
	private static final String FRAGMENT_TAB_COLORBIND = "色盲检测";
	private static final String FRAGMENT_TAB_ASTIGMATISM = "散光检测";
	private static final String FRAGMENT_TAB_VISION = "明视检测";
	private static final String FRAGMENT_TAB_SENSITIVITY = "敏感度检测";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//默认创建加载的是色盲测试Fragment（貌似系统直接选择第一个Fragment加载了，所以下面代码可注释掉）
		//showColorbindFragment();

	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_test, null);
		//初始化
		init(view);
		return view;
	}



	/**
	 * 页面初始化方法：设置对应的监听事件等
	 * @param view:当前的view。该方法在onCreateView中调用
	 */
	private void init(View view){
		//默认加载色盲测试
		showColorbindFragment();
		radioGroup = (RadioGroup) view.findViewById(R.id.test_menu);
		int checkedId = radioGroup.getCheckedRadioButtonId();
		//设置监听事件
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
				case R.id.radio_test_colorbind:
					showColorbindFragment();
					break;
				case R.id.radio_test_astigmatism:
					showAstigmatismFragment();
					break;
				case R.id.radio_test_vision:
					showVisionFragment();
					break;
				case R.id.radio_test_sensitivity:
					showSensitivityFragment();
					break;

			}
			}
		});

	}

	/**
	 * 显示色盲测试界面
	 */
	private void showColorbindFragment(){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		//如果Fragment没有被添加，添加Fragment
		Fragment f = fm.findFragmentByTag(FRAGMENT_TAB_COLORBIND);
		if(f == null){
			f = new TestColorbindFragment();
			ft.add(R.id.test_container,f,FRAGMENT_TAB_COLORBIND);
		}
		//隐藏当前界面
		hideCurrentFragment(fm,ft);
		ft.show(f);
		ft.commit();
	}

	/**
	 * 显示散光测试界面
	 */
	private void showAstigmatismFragment(){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		//如果Fragment没有被添加，添加Fragment
		Fragment f = fm.findFragmentByTag(FRAGMENT_TAB_ASTIGMATISM);
		if(f == null){
			f = new TestAstigmatismFragment();
			ft.add(R.id.test_container,f,FRAGMENT_TAB_ASTIGMATISM);
		}
		//隐藏当前界面
		hideCurrentFragment(fm,ft);
		ft.show(f);
		ft.commit();
	}
	/**
	 * 显示明视测试界面
	 */
	private void showVisionFragment(){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		//如果Fragment没有被添加，添加Fragment
		Fragment f = fm.findFragmentByTag(FRAGMENT_TAB_VISION);
		if(f == null){
			f = new TestVisionFragment();
			ft.add(R.id.test_container,f,FRAGMENT_TAB_VISION);
		}
		//隐藏当前界面
		hideCurrentFragment(fm,ft);
		ft.show(f);
		ft.commit();
	}
	/**
	 * 显示敏感度测试界面
	 */
	private void showSensitivityFragment(){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		//如果Fragment没有被添加，添加Fragment
		Fragment f = fm.findFragmentByTag(FRAGMENT_TAB_SENSITIVITY);
		if(f == null){
			f = new TestSensitivityFragment();
			ft.add(R.id.test_container,f,FRAGMENT_TAB_SENSITIVITY);
		}
		//隐藏当前界面
		hideCurrentFragment(fm,ft);
		ft.show(f);
		ft.commit();
	}

	/**
	 * 隐藏当前的Fragment
	 */
	private void hideCurrentFragment(FragmentManager fm,FragmentTransaction ft){
		Fragment f = getCurrentFragment(fm);
		if(f==null){
			return ;
		}
		ft.hide(f);
	}
	/**
	 * 获取当前显示的Fragment
	 */
	private Fragment getCurrentFragment(FragmentManager fm){
		List<Fragment> fs = fm.getFragments();
		for(Fragment f : fs){
			if(f.isVisible()){
				return f;
			}
		}
		return null;
	}
}
