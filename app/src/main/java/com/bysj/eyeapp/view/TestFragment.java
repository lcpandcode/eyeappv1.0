package com.bysj.eyeapp.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.List;

public class TestFragment extends Fragment {
	//相关组件定义
	private RadioGroup radioGroup;
	private FragmentManager fm;
	BaseActivity activity ;

	//字符串常量定义处
	private static final String FRAGMENT_TAB_COLORBIND = "色盲检测";
	private static final String FRAGMENT_TAB_ASTIGMATISM = "散光检测";
	private static final String FRAGMENT_TAB_VISION = "明视检测";
	private static final String FRAGMENT_TAB_SENSITIVITY = "敏感度检测";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_test, null);
		activity = (BaseActivity) getActivity();
		//初始化
		init(view);
		return view;
	}



	/**
	 * 页面初始化方法：设置对应的监听事件等
	 * @param view:当前的view。该方法在onCreateView中调用
	 */
	private void init(View view){
		fm = getFragmentManager();
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
					activity.setTitle(R.string.test_colorbind);
					break;
				case R.id.radio_test_astigmatism:
					showAstigmatismFragment();
					activity.setTitle(R.string.test_astigmatism);
					break;
				case R.id.radio_test_vision:
					showVisionFragment();
					activity.setTitle(R.string.test_vision);
					break;
				case R.id.radio_test_sensitivity:
					showSensitivityFragment();
					activity.setTitle(R.string.test_sensitivity);
					break;

			}
			}
		});

	}

	/**
	 * 显示色盲测试界面
	 */
	private void showColorbindFragment(){
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
	 * 隐藏当前的所有Fragment（除了测试的主页面，否则将会导致页面空白）
	 */
	private void hideCurrentFragment(FragmentManager fm,FragmentTransaction ft){
		List<Fragment> fs = fm.getFragments();
		for(Fragment f : fs){
			if(f.isVisible() && f!=null && f.getId()!=R.id.fragment_test){
				ft.hide(f);
			}
		}
	}

}
