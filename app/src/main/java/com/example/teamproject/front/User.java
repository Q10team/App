package com.example.teamproject.front;

import java.io.Serializable;

public class User implements Serializable {
    private int userNum;
    private String userName;
    private Integer userPoint;

    public int getUserNum() {return userNum; }
    public void setUserNum(int userNum) { this.userNum = userNum; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getUserPoint() { return userPoint; }
    public void setUserPoint(Integer userPoint) { this.userPoint = userPoint; }


    public User(int userNum, String userName, Integer userPoint) {
        this.userNum = userNum;
        this.userName = userName;
        this.userPoint = userPoint;
    }

    public User() {}
}

