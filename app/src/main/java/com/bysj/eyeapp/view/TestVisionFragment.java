package com.bysj.eyeapp.view;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TestVisionFragment extends Fragment {
	private RadioButton rbtn1;
	private RadioButton rbtn2;
	private RadioButton rbtn3;
	private RadioButton rbtn4;
	private View thisView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_test_vision, null);
		init(view);
		return view;
	}

	/**
	 * 初始化方法
	 */
	private void init(View view){
		thisView = view;
		rbtn1 = thisView.findViewById(R.id.test_vision_option1);
		rbtn2 = thisView.findViewById(R.id.test_vision_option2);
		rbtn3 = thisView.findViewById(R.id.test_vision_option3);
		rbtn4 = thisView.findViewById(R.id.test_vision_option4);
		//注册按钮
		rbtn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				optionChange(v);
			}
		});
		rbtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				optionChange(v);
			}
		});
		rbtn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				optionChange(v);
			}
		});
		rbtn4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				optionChange(v);
			}
		});
	}

	/**
	 * 选项控制按钮，解决RadioButtonGroup无法线性布局的问题
	 */
	public void optionChange(View view){
		int checkedId = view.getId();
		Resources rs = getContext().getResources();
		RadioButton rbtn1 = thisView.findViewById(R.id.test_vision_option1);
		RadioButton rbtn2 = thisView.findViewById(R.id.test_vision_option2);
		RadioButton rbtn3 = thisView.findViewById(R.id.test_vision_option3);
		RadioButton rbtn4 = thisView.findViewById(R.id.test_vision_option4);
		//所有设置为没有选中
		rbtn1.setChecked(false);
		rbtn2.setChecked(false);
		rbtn3.setChecked(false);
		rbtn4.setChecked(false);
		//设置选中按钮
		switch (checkedId)  {
			case R.id.test_vision_option1 :
				rbtn1.setChecked(true);
				break;
			case R.id.test_vision_option2 :
				rbtn2.setChecked(true);
				break;
			case R.id.test_vision_option3 :
				rbtn3.setChecked(true);
				break;
			case R.id.test_vision_option4 :
				rbtn4.setChecked(true);
				break;
		}
	}
}
