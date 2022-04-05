package com.example.qr_go.containers;

import android.graphics.Bitmap;

public class ScannedUserListDisplayContainer {
    private String username;
    private Bitmap image;
    private String userid;
    public ScannedUserListDisplayContainer(String userid, String username) {
        this.userid = userid;
        this.username = username;
        this.image = null;
    }

    /**
     * Getter for username
     * @return
     *      Returns this comment's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * gets the userid
     * @return userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * sets the picture
     * @param image the picture
     */
    public void setPicture(Bitmap image){this.image = image;};

    /**
     * gets the picture
     * @return image
     */
    public Bitmap getPicture(){return image;}
}
