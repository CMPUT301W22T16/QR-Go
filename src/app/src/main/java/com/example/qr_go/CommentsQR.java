package com.example.qr_go;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This is a class that represents a comment made by a user to a QRCode
 */

/**
 * this is the revised version of comments, originally Matthew's code
 * @Author Darius Fang
 */
public class CommentsQR {
    private HashMap<String, HashMap<String, String>> comments;
    private String qrid;
    // TODO: add qrid to constructor
    public CommentsQR(User user, String message, String photolink) {
        this.comments = new HashMap<>();
        HashMap<String, String> details = createDetails(user, message, photolink);
        comments.put(user.getUserid(), details);
    }
    public CommentsQR() {
        this.comments = new HashMap<>();
    }
    private HashMap<String, String> createDetails(User user, String message, String photolink){
        HashMap<String, String> details = new HashMap<>();
        details.put("Username", user.getUsername());
        details.put("Message", message);
        details.put("PhotoRef", photolink);
        return details;
    }
    public void addComment(User user, String message, String photolink){
        createDetails(user, message, photolink);
        HashMap<String, String> details = createDetails(user, message, photolink);
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
    public ArrayList<Comment> getCommentObjects(){
        ArrayList<Comment> out= new ArrayList<Comment>();
        for (Map.Entry<String, HashMap<String, String>> details:comments.entrySet() ){
            HashMap<String, String> temp = details.getValue();
            Comment comment = new Comment(temp.get("Username"), temp.get("Message"));
            out.add(comment);
        }
        return out;
    }
}
