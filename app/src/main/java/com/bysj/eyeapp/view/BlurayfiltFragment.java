package com.bysj.eyeapp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class BlurayfiltFragment extends Fragment {


	private WindowManager windowManager;
	public static LinearLayout linearLayout;
	SeekBar seekBar;
	View thisView;
	RadioGroup radioGroupModel;
	RadioGroup radioGroupColor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_blurayfilter, null);
		thisView = view;
		init();
		return view;
	}

	WindowManager.LayoutParams params;
	private void init(){
		radioGroupModel = thisView.findViewById(R.id.model);
		radioGroupColor = thisView.findViewById(R.id.color);
		windowManager = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		// 悬浮窗
		params=new WindowManager.LayoutParams();
		LayoutInflater inflater=LayoutInflater.from(getActivity());
		linearLayout= (LinearLayout) inflater.inflate(R.layout.eye_protect_bg,null);
		params.width=WindowManager.LayoutParams.MATCH_PARENT;
		params.height=WindowManager.LayoutParams.MATCH_PARENT;
		params.gravity= Gravity.CENTER_VERTICAL|Gravity.RIGHT;
		params.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;;
		params.alpha=0f;
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		windowManager.addView(linearLayout,params);


		seekBar = (SeekBar) thisView.findViewById(R.id.progress);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值

				params.alpha = (float) ((0.8/200.0) * progress);
				windowManager.updateViewLayout(linearLayout, params);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.e("------------", "开始滑动！");
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.e("------------", "停止滑动！");
			}
		});


		//初始化功能界面
		Button btnNormal = thisView.findViewById(R.id.blurelayfilter_normal);
		Button btnEvening = thisView.findViewById(R.id.blurelayfilter_evening);
		Button btnProtecteye = thisView.findViewById(R.id.blurelayfilter_protecteye);

		btnNormal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				params.alpha=0f;
				windowManager.updateViewLayout(linearLayout, params);
			}
		});
		btnEvening.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				params.alpha=0.5f;
				linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.blurelayfilter_black));
				windowManager.updateViewLayout(linearLayout, params);
			}
		});
		btnProtecteye.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				params.alpha=0.4f;
				linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.blurelayfilter_green));
				windowManager.updateViewLayout(linearLayout, params);
			}
		});

		Button btnGreen = thisView.findViewById(R.id.blurelayfilter_green);
		Button btnGray = thisView.findViewById(R.id.blurelayfilter_gray);
		Button btnYellow = thisView.findViewById(R.id.blurelayfilter_yellow);
		Button btnBlack = thisView.findViewById(R.id.blurelayfilter_black);

		btnGreen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("test","btn1_test");
				linearLayout.setBackgroundColor(getActivity().getResources().
						getColor(R.color.blurelayfilter_green));
				windowManager.updateViewLayout(linearLayout, params);
			}
		});

		btnGray.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				linearLayout.setBackgroundColor(getActivity().getResources().
						getColor(R.color.blurelayfilter_gray));
				windowManager.updateViewLayout(linearLayout, params);
			}
		});

		btnYellow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				linearLayout.setBackgroundColor(getActivity().getResources().
						getColor(R.color.blurelayfilter_yellow));
				windowManager.updateViewLayout(linearLayout, params);
			}
		});

		btnBlack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				linearLayout.setBackgroundColor(getActivity().getResources().
						getColor(R.color.blurelayfilter_black));
				windowManager.updateViewLayout(linearLayout, params);
			}
		});



	}


	/**
	 * 打开护眼模式方法
//	 */
//	public void openEyeProtect(){
//		// 悬浮窗
//		button = new Button(this.getActivity());
//		button.setText("悬浮窗 Zhang Phil @CSDN");
//		button.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 作为测试，点击后删除该悬浮窗（即Button按钮）
//				windowManager.removeView(button);
//			}
//		});
//
//		windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
//		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//
//		// 靠手机屏幕的左边居中显示
//		params.gravity = Gravity.CENTER | Gravity.LEFT;
//
//		params.type = WindowManager.LayoutParams.TYPE_PHONE;
//		params.format = PixelFormat.RGBA_8888;
//
//		// 如果设置以下属性，那么该悬浮窗口将不可触摸，不接受输入事件，不影响其他窗口事件的传递和分发
//		// params.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
//		// |LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
//
//		// 可以设定坐标
//		// params.x=xxxx
//		// params.y=yyyy
//
//		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//
//		// 透明度
//		// params.alpha=0.8f;
//
//		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//		windowManager.addView(button, params);
//
//		// 更新
//		// windowManager.updateViewLayout(button, params);
//	}
//

}
