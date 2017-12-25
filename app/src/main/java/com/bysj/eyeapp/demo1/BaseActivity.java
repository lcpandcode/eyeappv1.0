package com.bysj.eyeapp.demo1;

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
     * 默认初始化方法
     */
    protected void setupViews() {
        //super.setContentView(R.layout.titlebar);
        mTitleTextView = (TextView) findViewById(R.id.text_title);
        mBackwardbButton = (Button) findViewById(R.id.button_backward);
        mForwardButton = (Button) findViewById(R.id.button_forward);
        //显示标题的返回按钮，如果需要隐藏，调用showBackwardView，其中show传false布尔值即可，确认按钮类似
        showBackwardView(R.string.title_btn_return,true);
        //显示标题的确认按钮
        showForwardView(R.string.title_btn_confirm,true);
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


    /*
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     * 按钮点击调用的方法
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
