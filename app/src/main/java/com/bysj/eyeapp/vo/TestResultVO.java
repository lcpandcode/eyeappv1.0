package com.bysj.eyeapp.vo;

/**
 * Created by lcplcp on 2018/1/12.
 */

public class TestResultVO {
    private Integer id;

    private Integer uId;

    private Integer correctRate;

    private String date;

    private String eye ;

    private Double testResult ;

    private String type ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(Integer correctRate) {
        this.correctRate = correctRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public Double getTestResult() {
        return testResult;
    }

    public void setTestResult(Double testResult) {
        this.testResult = testResult;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
