package com.example.qr_go;

/**
 * Class for holding only relevant information from a User to display in a list
 */
public class UserListDisplayContainer {
    private String userid;
    private String username;
    private Integer totalScore;
    private Integer numQRs;

    public UserListDisplayContainer(String userid, String username, Integer totalScore, Integer numQRs) {
        this.userid = userid;
        this.username = username;
        this.totalScore = totalScore;
        this.numQRs = numQRs;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public Integer getNumQRs() {
        return numQRs;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }
}
