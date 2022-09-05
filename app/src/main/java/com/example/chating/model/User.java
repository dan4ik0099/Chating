package com.example.chating.model;



public class User {
    public String id, userName, uid;

    public User() {
    }

    public User(String id, String userName, String uid) {
        this.id = id;
        this.userName = userName;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}