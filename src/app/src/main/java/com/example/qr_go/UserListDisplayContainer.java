package com.example.qr_go;

public class UserListDisplayContainer {
    private String userid;
    private String username;

    public UserListDisplayContainer(String userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }
}
