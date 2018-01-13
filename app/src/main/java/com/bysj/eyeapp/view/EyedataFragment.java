package com.bysj.eyeapp.view;

import android.app.Activity;
import android.app.Application;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bysj.eyeapp.dao.EyedataDAO;
import com.bysj.eyeapp.service.EyedataService;
import com.bysj.eyeapp.util.ArcView;
import com.bysj.eyeapp.util.ArcViewUtil;
import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.vo.EyedataVO;

public class EyedataFragment extends Fragment {
	private static final String APPLICATION_EYEDARA_TAG = GlobalConst.APPLICATION_EYEDATA_TAG;

	private ArcView arcView;
	private View thisView;

	//数据相关
	private EyedataVO eyedata;
	private EyedataService service;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_eyedata, null);
		thisView = view;
		init();
		return view;
	}





	private void init(){
		service = new EyedataService();
		initEyedata();
		arcView = getArcView();
		LinearLayout view = thisView.findViewById(R.id.eyedata_bar_arc);
		view.addView(arcView,0);
		//获取圆
		View round = thisView.findViewById(R.id.eyedata_bar_arc_round);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		float [] position = caculateRoundPosition(0.75F);

		int parentMarginLeft = (int)dpToPx(30);//注意：由于设置了边距，这里要补上！
		int parentMarginTop = (int)dpToPx(0);//注意：由于设置了边距，这里要补上！
		params.leftMargin = (int)position[1] + parentMarginLeft;
		params.topMargin = (int)position[0] + parentMarginTop;
//		params.topMargin = 0;
//		params.leftMargin = 0;
		round.setLayoutParams(params);
		//round.setX(500);
		//round.setY(500);

	}

	/**
	 * 初始化用眼数据
	 */
	private void initEyedata(){
		//从application中读取数据，如果没有数据，说明是初次加载
		GlobalApplication application =  (GlobalApplication)getActivity().getApplication();
		eyedata = (EyedataVO) application.getGlobalVar(APPLICATION_EYEDARA_TAG);
		if(eyedata==null){
			eyedata = service.getEyedataToday(getActivity());
			application.putGlobalVar(APPLICATION_EYEDARA_TAG,eyedata);
		}
	}

	/**
	 * 画圆弧的方法，用于描绘界面中的圆弧
	 */
	private ArcView getArcView(){
		ArcView.ArcViewParams arcParams = new ArcView.ArcViewParams();
		ArcView arcView = new ArcView(getActivity(),arcParams);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		arcView.setLayoutParams(params);
		return arcView;
	}

	/**
	 *	计算圆型位置的方法，返回两个坐标值：一个是距离父控件左边的距离，一个是距离父控件上边的距离
	 * 其中，父控件的大小由圆弧大小决定
	 * @param rate 比的结果：即开屏与待机时间比
	 * @return 坐标值：距右边和距上边距离
	 */
	private float [] caculateRoundPosition(float rate){
		ArcView.ArcViewParams params = arcView.getParams();
		float width = params.getRight() - params.getLeft() ;
		float height = params.getBottom() - params.getTop();
		float r = width/2;
		float angle = (float) Math.PI * rate;
		float [] result = new float[2];
		double d = r * Math.sin(angle);
		float f = (float)d;
		result[0] = r - (float) (r * Math.sin(angle));
		result[1] = r - (float) (r * Math.cos(angle));
		return result;
	}



	/**
	 * dp 和 px之间的转换
	 */
	private float dpToPx(float dpSize){
		DisplayMetrics dm = getResources().getDisplayMetrics() ;
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
	}


}
