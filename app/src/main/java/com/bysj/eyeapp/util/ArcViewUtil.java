package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/7.
 */

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.bysj.eyeapp.view.R;

/**
 * 圆弧操作相关工具类：包括获取圆弧、圆等曲线方法
 */
public class ArcViewUtil {

    /**
     * 获取一个直径为dia dp的圆型
     */
    public static View getRoundView (float dia, Context context){
        ArcView.ArcViewParams arcParams = new ArcView.ArcViewParams();
        arcParams.setStartAngle(0);
        arcParams.setArcAngle(360);
        arcParams.setRound(true);
        dia = dpToPx(dia,context);
        arcParams.setRight(dia);
        arcParams.setBottom(dia);
        View arcView = new ArcView(context,arcParams);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        arcView.setLayoutParams(params);
        return arcView;
    }


    /**
     * dp 和 px之间的转换
     */
    private static float dpToPx(float dpSize,Context context){
        if(!(context instanceof Activity)){
            throw new RuntimeException("context 必须是Activity!");
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics() ;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
    }
}
