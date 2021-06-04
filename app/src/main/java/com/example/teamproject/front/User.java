package com.example.teamproject.front;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private Integer userPoint;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getUserPoint() { return userPoint; }
    public void setUserPoint(Integer userPoint) { this.userPoint = userPoint; }


    public User(String userName, Integer userPoint) {
        this.userName = userName;
        this.userPoint = userPoint;
    }

    public User() {}
}

