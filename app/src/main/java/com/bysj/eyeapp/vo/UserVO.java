package com.bysj.eyeapp.vo;

/**
 * Created by lcplcp on 2018/1/10.
 */

public class UserVO {
    private int id;
    private String nickName;
    private String phone;
    private String sex;
    private String password;
    private String type;

    public int getId() {
        return id;
    }

    public UserVO setId(int id) {
        this.id = id;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public UserVO setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserVO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public UserVO setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserVO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getType() {
        return type;
    }

    public UserVO setType(String type) {
        this.type = type;
        return this;
    }
}
