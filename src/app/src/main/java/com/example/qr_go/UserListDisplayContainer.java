package com.example.qr_go;

/**
 * Class for holding only relevant information from a User to display in a list
 */
public class UserListDisplayContainer {
    private String userid;
    private String username;
    private Integer totalScore;
    private Integer numQRs;
    private Integer highestScore;
    private Boolean isCurrentUser;

    public UserListDisplayContainer(String userid, String username, Integer totalScore, Integer numQRs, Integer highestScore, Boolean isCurrentUser) {
        this.userid = userid;
        this.username = username;
        this.totalScore = totalScore;
        this.numQRs = numQRs;
        this.highestScore = highestScore;
        this.isCurrentUser = isCurrentUser;
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

    public Integer getHighestScore() {
        return highestScore;
    }

    public Boolean getIsCurrentUser() {
        return isCurrentUser;
    }
}
