package com.example.qr_go.containers;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.UUID;

/**
 * This is a class that represents a comment made by a user to a QRCode
 */
public class CommentDisplayContainer implements Serializable {
    private String message;
    private String username;
    private String userid;
    private Bitmap image;
    public CommentDisplayContainer(String username, String message, String userid) {
        this.username = username;
        this.message = message;
        this.userid = userid;
        this.image = null;
    }

    /**
     * Getter for message
     * @return
     *      Returns this comment's message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Getter for username
     * @return
     *      Returns this comment's username
     */
    public String getUsername() {
        return username;
    }
    public String getUserid() {
        return userid;
    }
    /**
     * Setter for message. Used for editing the message of this comment.
     * @param message
     *          The new message that replaces the old message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    public void setPicture(Bitmap image){this.image = image;};
    public Bitmap getPicture(){return image;}

}