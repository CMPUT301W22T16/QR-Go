package com.example.qr_go;

/**
 * Class for holding only relevant information from a User to display in a list
 */
public class UserListDisplayContainer {
    private String userid;
    private String username;
    private Integer totalScore;
    private Integer numQRs;
    private Integer highestScore; // User's highest unique score
    private Boolean isCurrentUser; // If this user is the current user on the app
    private Integer rankPosition; // The user's rank given the sort

    public UserListDisplayContainer(String userid, String username, Integer totalScore, Integer numQRs, Integer highestScore, Boolean isCurrentUser) {
        this.userid = userid;
        this.username = username;
        this.totalScore = totalScore;
        this.numQRs = numQRs;
        this.highestScore = highestScore;
        this.isCurrentUser = isCurrentUser;
        this.rankPosition = 0;
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

    public Integer getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(Integer rankPosition) {
        this.rankPosition = rankPosition;
    }
}
