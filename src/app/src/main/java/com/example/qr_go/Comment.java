package com.example.qr_go;

import java.util.HashMap;
import java.util.UUID;

/**
 * This is a class that represents a comment made by a user to a QRCode
 */

/**
 * this is the revised version of comments, originally Matthew's code
 * @Author Darius Fang
 */
public class Comment {
    private HashMap<String, String> details = new HashMap<>();
    private HashMap<String, HashMap<String, String>> comments;
    public Comment(User user, String message, String photolink) {
        this.comments = new HashMap<>();
        createDetails(user, message, photolink);
        comments.put(user.getUserid(), details);
    }
    public Comment() {
        this.comments = new HashMap<>();
    }
    private void createDetails(User user, String message, String photolink){
        details.clear();
        details.put("Username", user.getUsername());
        details.put("Message", message);
        details.put("PhotoRef", photolink);
    }
    public void addComment(User user, String message, String photolink){
        createDetails(user, message, photolink);
        comments.put(user.getUserid(), details);
    }
    public HashMap<String, HashMap<String, String>> getComments()
    {
        return this.comments;

    }

    /**
     * Getter for message
     * @return
     *      Returns this comment's message
     */
    public String getMessage(String userId)
    {
        return comments.get(userId).get("Message");

    }

    /**
     * Setter for message. Used for editing the message of this comment.
     * @param message
     *          The new message that replaces the old message
     */
    public void setMessage(String userId, String message) {
        HashMap<String, String> details = comments.get(userId);
        details.put("Message", message);
        comments.put(userId, details);

    }


}