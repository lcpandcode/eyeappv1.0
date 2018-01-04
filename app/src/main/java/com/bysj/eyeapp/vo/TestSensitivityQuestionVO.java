package com.bysj.eyeapp.vo;

/**
 * Created by lcplcp on 2017/12/29.
 */

/**
 * 敏感度测试中生成敏感度题目的封装vo
 */
public class TestSensitivityQuestionVO {
    //背景颜色（即错误选项）：用16进制的颜色表示
    private String backgroundColor;
    private String trueOptionColor;//正确选项的颜色
    private int difficulty;//题目难度系数：难度系数影响分值，越难分值越大

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTrueOptionColor() {
        return trueOptionColor;
    }

    public void setTrueOptionColor(String trueOptionColor) {
        this.trueOptionColor = trueOptionColor;
    }
}
