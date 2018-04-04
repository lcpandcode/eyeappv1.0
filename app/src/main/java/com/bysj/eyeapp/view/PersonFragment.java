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

import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.vo.UserVO;

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
		Button btn_login = (Button) view.findViewById(R.id.button_person_login);
		Button btn_logout = (Button) view.findViewById(R.id.button_person_logout);
		btn_info.setOnClickListener(new MyListenner());
		btn_eye.setOnClickListener(new MyListenner());
		btn_train.setOnClickListener(new MyListenner());
		btn_communication.setOnClickListener(new MyListenner());
		btn_message.setOnClickListener(new MyListenner());
		btn_login.setOnClickListener(new MyListenner());
		btn_logout.setOnClickListener(new MyListenner());
		return view;
	}


	private class MyListenner implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			//判断是否登录
			if(v.getId()!=R.id.button_person_login && !isLogin()){
				CustomToast.showToast(getActivity(),"您未登录，请先在个人中心中登录！");
				return;
			}
			//避免重复登录
			if(v.getId()==R.id.button_person_login && isLogin()){
				CustomToast.showToast(getActivity(),"您已经登录！");
				return;
			}
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
				case R.id.button_person_login:
					Intent intent6 = new Intent(getActivity(),LoginActivity.class);
					startActivity(intent6);
					break;
				case R.id.button_person_logout:
					logout();
					break;
			}
		}

		private boolean isLogin(){
			GlobalApplication globalApplication = (GlobalApplication)getActivity().getApplicationContext();
			UserVO user = (UserVO)globalApplication.getGlobalVar(GlobalConst.TAG_USER);
			if(user==null){
				return false;
			}
			return true;
		}

		private void logout(){
			GlobalApplication globalApplication = (GlobalApplication)getActivity().getApplication();
			globalApplication.removeGlobalVar(GlobalConst.TAG_USER);
			HttpUtil.seToken("");
			CustomToast.showToast(getActivity(),"已注销用户信息！");
		}
	}
}
