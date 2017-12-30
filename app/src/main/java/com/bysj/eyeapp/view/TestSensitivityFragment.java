package com.bysj.eyeapp.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TestSensitivityFragment extends Fragment {
	private View thisView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_test_sensitivity, null);
		thisView = view;
		thisView.findViewById(R.id.test_sensitivity_btn_begin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				beginAnswer();
			}
		});
		return view;
	}

	/**
	 * 进入答题方法
	 */
	private void beginAnswer(){
		Intent intent = new Intent();
		intent.setClass(thisView.getContext(),TestSensitivityActivity.class);
		startActivity(intent);
	}

}
