package com.bysj.eyeapp.dto;

/**
 * Created by lcplcp on 2018/1/12.
 */

/**
 * 用眼数据dao
 */
public class EyedataDTO {
    private int id;
    private int openScreenTime ;//今日开屏总时长，int类型，单位是秒
    private int openScreenCount;//开屏次数统计
    private int indoorTime;//室内时间
    private int outdoorTime;//室外时间
    private String date;//日期，代表该条记录时哪天的记录

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpenScreenTime() {
        return openScreenTime;
    }

    public void setOpenScreenTime(int openScreenTime) {
        this.openScreenTime = openScreenTime;
    }

    public int getOpenScreenCount() {
        return openScreenCount;
    }

    public void setOpenScreenCount(int openScreenCount) {
        this.openScreenCount = openScreenCount;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
