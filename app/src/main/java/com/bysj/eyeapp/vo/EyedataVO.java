package com.bysj.eyeapp.vo;

import java.util.Date;

/**
 * Created by lcplcp on 2018/1/12.
 */

public class EyedataVO {
    private float ratio;//开屏与待机时长之比
    private int openScreenTimeCountToday = 0;//今日开屏总时长，int类型，单位是秒
    private int openScreenCount = 1;//开屏次数统计
    private int indoorTime = 0;//室内时间
    private int outdoorTime = 0;//室外时间

    public int getIndoorTime() {
        return indoorTime;
    }

    public void setIndoorTime(int indoorTime) {
        this.indoorTime = indoorTime;
    }

    public int getOutdoorTime() {
        return outdoorTime;
    }

    public void setOutdoorTime(int outdoorTime) {
        this.outdoorTime = outdoorTime;
    }

    private String recentOpenScreenTime;//最近一次开屏时间，用于计算当前开屏时间

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getOpenScreenTimeCountToday() {
        return openScreenTimeCountToday;
    }

    public void setOpenScreenTimeCountToday(int openScreenTimeCountToday) {
        this.openScreenTimeCountToday = openScreenTimeCountToday;
    }

    public int getOpenScreenCount() {
        return openScreenCount;
    }

    public void setOpenScreenCount(int openScreenCount) {
        this.openScreenCount = openScreenCount;
    }



    public String getRecentOpenScreenTime() {
        return recentOpenScreenTime;
    }

    public void setRecentOpenScreenTime(String recentOpenScreenTime) {
        this.recentOpenScreenTime = recentOpenScreenTime;
    }
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("ratio:" + ratio).append(",openScreenTimeCountToday:" + openScreenTimeCountToday)
                .append(",openScreenCount:" + openScreenCount).append(",indoorTime:" + indoorTime)
                .append(",outdoorTime" + outdoorTime);
        return sb.toString();
    }
}
