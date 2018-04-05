package com.bysj.eyeapp.vo;

/**
 * Created by lcplcp on 2017/12/29.
 */

public class TestVisionQuestionVO {
    private float size;//图片大小：mm为单位
    private char direction;//方向：上，下，左，右
    private float visionVal;//视力值，取值在4.0-5.2之间

    public float getVisionVal() {
        return visionVal;
    }

    public void setVisionVal(float visionVal) {
        this.visionVal = visionVal;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }
}
