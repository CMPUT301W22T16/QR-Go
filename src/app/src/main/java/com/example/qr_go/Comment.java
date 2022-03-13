package com.example.qr_go;

import java.io.Serializable;
import java.util.UUID;

/**
 * This is a class that represents a comment made by a user to a QRCode
 */
public class Comment implements Serializable {
    private String message;
    private String username;

    public Comment(String username, String message) {
        this.username = username;
        this.message = message;

    }

    /**
     * Getter for message
     * @return
     *      Returns this comment's message
     */
    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Setter for message. Used for editing the message of this comment.
     * @param message
     *          The new message that replaces the old message
     */
    public void setMessage(String message) {
        this.message = message;
    }


}