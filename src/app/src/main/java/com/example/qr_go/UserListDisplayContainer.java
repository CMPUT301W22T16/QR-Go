package com.example.qr_go;

/**
 * Class for holding only relevant information from a User to display in a list
 */
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
