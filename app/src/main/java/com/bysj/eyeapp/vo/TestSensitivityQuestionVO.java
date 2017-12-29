package com.bysj.eyeapp.vo;

/**
 * Created by lcplcp on 2017/12/29.
 */

/**
 * 敏感度测试中生成敏感度题目的封装vo
 */
public class TestSensitivityQuestionVO {
    //背景颜色（即错误选项）：用16进制的颜色表示
    private int backgroundColor;
    private int trueOptionColor;//正确选项的颜色

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTrueOptionColor() {
        return trueOptionColor;
    }

    public void setTrueOptionColor(int trueOptionColor) {
        this.trueOptionColor = trueOptionColor;
    }
}
