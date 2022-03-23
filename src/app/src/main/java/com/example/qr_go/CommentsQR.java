package com.example.qr_go;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom conatiner for comments, used for database interface, and can be translated to an arraylist for the android adapter
 * this is the revised version of comments container, originally Matthew's work
 * @author Darius Fang
 */
public class CommentsQR {
    private HashMap<String, HashMap<String, String>> comments;
    // TODO: add qrid to constructor
    public CommentsQR(User user, String message, String photolink) {
        this.comments = new HashMap<>();
        HashMap<String, String> details = new HashMap<>();
        details.put("Username", user.getUsername());
        details.put("Message", message);
        details.put("PhotoRef", photolink);
        comments.put(user.getUserid(), details);
    }
    public CommentsQR() {
        this.comments = new HashMap<>();
    }

    /**
     * adds the comment onto the hashmap stack
     * @param user user to be added
     * @param message message of the user
     * @param photolink profile of user
     */
    public void addComment(User user, String message, String photolink){

        HashMap<String, String> details = new HashMap<>();
        details.put("Username", user.getUsername());
        details.put("Message", message);
        details.put("PhotoRef", photolink);
        comments.put(user.getUserid(), details);
    }

    /**
     * Comment getter
     * @return HashMap<String, HashMap<String, String>
     */
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

    /**
     * Custom converstion for formating of android adapter
     * @return ArrayList<Comment>
     */
    public ArrayList<Comment> getCommentObjects(){
        ArrayList<Comment> out= new ArrayList<>();
        for (Map.Entry<String, HashMap<String, String>> details:comments.entrySet() ){
            HashMap<String, String> temp = details.getValue();
            Comment comment = new Comment(temp.get("Username"), temp.get("Message"));
            out.add(comment);
        }
        return out;
    }
}
