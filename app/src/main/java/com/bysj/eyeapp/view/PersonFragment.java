package com.bysj.eyeapp.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PersonFragment extends Fragment {
	private View thisView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_person, null);
		thisView = view;
		init();
		return view;
	}

	private void init(){
		Intent intent = new Intent(getActivity(),PersonInfo.class);
	   startActivity(intent);
	}
}
