package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/8.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.eyeapp.view.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class CustomToast {
    private static TextView mTextView;
    private static Toast toastStart;
    private static final int TOAST_DEFAULT_SHOW_TIME = 2000 ;

    public static void showToast(Context context, String message) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.view_global_toast, null);
        //初始化布局控件
        mTextView = (TextView) toastRoot.findViewById(R.id.message);
        //为控件设置属性
        mTextView.setText(message);
        //Toast的初始化
        toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    public static void setShowTime(final int time){
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toastStart.show();
            }
        }, 0, TOAST_DEFAULT_SHOW_TIME);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toastStart.cancel();
                timer.cancel();
            }
        }, time );
    }
}