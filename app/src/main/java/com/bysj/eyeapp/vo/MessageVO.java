package com.bysj.eyeapp.vo;

/**
 * Created by lcplcp on 2018/2/9.
 */

/***
 * 消息封装类
 */
public class MessageVO {
    private long date;
    private String content;
    private boolean isRead;
    private boolean isCome;//是否是对方发过来的消息

    public boolean isCome() {
        return isCome;
    }

    public void setCome(boolean come) {
        isCome = come;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
