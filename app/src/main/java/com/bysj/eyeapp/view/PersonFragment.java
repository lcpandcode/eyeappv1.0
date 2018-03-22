package com.bysj.eyeapp.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

public class PersonFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_person, null);
		Button btn_info = (Button) view.findViewById(R.id.button_person_info);
		Button btn_eye = (Button) view.findViewById(R.id.button_person_eye);
		Button btn_train = (Button) view.findViewById(R.id.button_person_train);
		Button btn_communication = (Button) view.findViewById(R.id.button_person_communication);
		Button btn_message = (Button) view.findViewById(R.id.button_person_message);
		btn_info.setOnClickListener(new MyListenner());
		btn_eye.setOnClickListener(new MyListenner());
		btn_train.setOnClickListener(new MyListenner());
		btn_communication.setOnClickListener(new MyListenner());
		btn_message.setOnClickListener(new MyListenner());
		return view;
	}


	private class MyListenner implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.button_person_info:
					Intent intent1 = new Intent(getActivity(),PersonInfo.class);
					startActivity(intent1);
					break;
				case R.id.button_person_eye:
//					Intent intent2 = new Intent(getActivity(),PersonEye.class);
//					startActivity(intent2);
					MainActivity activity = (MainActivity)getActivity();
					TabHost tabHost = activity.getTabHost();
					tabHost.setCurrentTabByTag("eyedata");
					activity.setTitle(R.string.title_eyedata);
					break;
				case R.id.button_person_train:
					Intent intent3 = new Intent(getActivity(),PersonTrain.class);
					startActivity(intent3);
					break;
				case R.id.button_person_communication:
					Intent intent4 = new Intent(getActivity(),PersonCommunication.class);
					startActivity(intent4);
					break;
				case R.id.button_person_message:
					Intent intent5 = new Intent(getActivity(),PersonMessage.class);
					startActivity(intent5);
					break;
			}
		}
	}
}
