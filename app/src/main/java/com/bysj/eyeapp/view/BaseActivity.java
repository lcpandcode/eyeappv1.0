package com.bysj.eyeapp.view;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主界面的父类，用于需要自定义标题栏的类，直接继承即可，具体的用法可参考MainActivity
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    //自定义标题栏相关变量
    protected TextView mTitleTextView;
    protected Button mBackwardbButton;
    protected Button mForwardButton;
    protected FrameLayout mContentLayout;

    /**
     * 默认初始化方法,子类需要根据自己需要，重写该方法，该方法主要用于初始化标题栏相关的参数
     * 在该方法中必需标题栏相关参数，如果标题栏没有按钮，则可以把按钮设置为null
     * @param mTitleTextView:标题栏的标题文字；
     * @param mBackwardbButton:标题栏的返回按钮
     * @param mForwardButton:标题栏的提交（确认）按钮
     */
    protected void setupViews(TextView mTitleTextView,Button mBackwardbButton ,Button mForwardButton) {
        this.mTitleTextView = mTitleTextView;
        this.mBackwardbButton = mBackwardbButton;
        this.mForwardButton = mForwardButton;
    }
    /**
     * 是否显示返回按钮
     * @param backwardResid  文字
     * @param show  true则显示
     */
    protected void showBackwardView(int backwardResid, boolean show) {
        if (mBackwardbButton != null) {
            if (show) {
                mBackwardbButton.setText(backwardResid);
                mBackwardbButton.setVisibility(View.VISIBLE);
            } else {
                mBackwardbButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    /**
     * 提供是否显示提交按钮
     * @param forwardResId  文字
     * @param show  true则显示
     */
    protected void showForwardView(int forwardResId, boolean show) {
        if (mForwardButton != null) {
            if (show) {
                mForwardButton.setVisibility(View.VISIBLE);
                mForwardButton.setText(forwardResId);
            } else {
                mForwardButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    /**
     * 返回按钮点击后触发，根据不同的页面进行重写即可
     * @param backwardView
     */
    protected void onBackward(View backwardView) {

        Toast toast = Toast.makeText(this, "点击返回，可在此处调用finish()", Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.color.title_background);
        toast.setView(view);
        toast.show();
    }

    /**
     * 提交按钮点击后触发
     * @param forwardView
     */
    protected void onForward(View forwardView) {
        Toast toast = Toast.makeText(this, "点击确认，可在此处调用finish()", Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.color.title_background);
        toast.setView(view);
        toast.show();
    }


    //设置标题内容
    @Override
    public void setTitle(int titleId) {
        mTitleTextView.setText(titleId);
    }

    //设置标题内容
    @Override
    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }

    //设置标题文字颜色
    @Override
    public void setTitleColor(int textColor) {
        mTitleTextView.setTextColor(textColor);
    }


    /**
     *
     * 按钮点击调用的方法，当需要用到按钮时，不同页面重写该方法即可
     * @view 当前的按钮元素
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_backward:
                onBackward(v);
                break;

            case R.id.button_forward:
                onForward(v);
                break;

            default:
                break;
        }
    }
}
