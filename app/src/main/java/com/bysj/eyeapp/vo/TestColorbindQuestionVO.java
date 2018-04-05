package com.bysj.eyeapp.vo;

/**
 * Created by lcplcp on 2017/12/29.
 */

public class TestColorbindQuestionVO {
    private int id;
    private String imgUrl;
    private String title;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctOption;
    private String option1Detail;//选择各个选项代表的具体意义：例如选了A有可能是红色盲，选了B可能是蓝色盲等说明
    private String option2Detail;
    private String option3Detail;
    private String option4Detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOption1Detail() {
        return option1Detail;
    }

    public void setOption1Detail(String option1Detail) {
        this.option1Detail = option1Detail;
    }

    public String getOption2Detail() {
        return option2Detail;
    }

    public void setOption2Detail(String option2Detail) {
        this.option2Detail = option2Detail;
    }

    public String getOption3Detail() {
        return option3Detail;
    }

    public void setOption3Detail(String option3Detail) {
        this.option3Detail = option3Detail;
    }

    public String getOption4Detail() {
        return option4Detail;
    }

    public void setOption4Detail(String option4Detail) {
        this.option4Detail = option4Detail;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }
}
