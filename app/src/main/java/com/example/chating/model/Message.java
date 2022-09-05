package com.example.chating.model;

import java.util.Date;

public class Message {

    public String userName;
    public String UidSender;
    public String UidReciever;
    public String text;
    public long time;

    public Message(){

    }

    public Message(String userName, String uidSender, String uidReciever, String text, long time) {
        this.userName = userName;
        UidSender = uidSender;
        UidReciever = uidReciever;
        this.text = text;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
