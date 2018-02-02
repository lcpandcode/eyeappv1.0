package com.bysj.eyeapp.vo;

/**
 * Created by lcplcp on 2017/12/29.
 */

public class TestVisionQuestionVO {
    private float size;//图片大小：mm为单位
    private char direction;//方向：上，下，左，右



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
