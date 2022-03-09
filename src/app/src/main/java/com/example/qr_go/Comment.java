package com.example.qr_go;

import java.util.UUID;

/**
 * This is a class that represents a comment made by a user to a QRCode
 */
public class Comment {

    private UUID commentId;
    private String message;
    private UUID attachedQRId;

    public Comment(UUID attachedQRId, String message) {
        this.commentId = UUID.randomUUID();
        this.message = message;
        this.attachedQRId = attachedQRId;
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
     * Setter for message. Used for editing the message of this comment.
     * @param message
     *          The new message that replaces the old message
     */
    public void setMessage(String message) {
        this.message = message;
    }


}
