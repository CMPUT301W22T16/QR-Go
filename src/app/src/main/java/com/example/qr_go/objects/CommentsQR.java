package com.example.qr_go.objects;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.qr_go.containers.CommentDisplayContainer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Custom conatiner for comments, used for database interface, and can be translated to an arraylist for the android adapter
 * this is the revised version of comments container, originally Matthew's work
 * @author Darius Fang
 */
public class CommentsQR {
    private HashMap<String, HashMap<String, String>> comments;
    // TODO: add qrid to constructor
    public CommentsQR(HashMap<String, HashMap<String, String>> comments) {
        this.comments = comments;

    }
    public CommentsQR(User user, String message, String photolink) {
        this.comments = new HashMap<>();
        HashMap<String, String> details = new HashMap<>();
        details.put("userId", user.getUserid());
        details.put("Username", user.getUsername());
        details.put("Message", message);
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
    public void addComment(@NonNull User user, String message, String photolink){

        HashMap<String, String> details = new HashMap<>();
        details.put("userId", user.getUserid());
        details.put("Username", user.getUsername());
        details.put("Message", message);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd '@'hh:mm:ss a");
        String strDate = dateFormat.format(date);
        comments.put(strDate, details);
    }
    public HashMap<String, HashMap<String, String>> getcomments() {
        return comments;
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

    public void deleteComment(String userid){
        comments.remove(userid);
    }
    /**
     * Custom converstion for formating of android adapter
     * @return ArrayList<Comment>
     */
    public ArrayList<CommentDisplayContainer> getCommentObjects(){
        ArrayList<CommentDisplayContainer> out= new ArrayList<>();
        ArrayList<String> keyList = new ArrayList<>(comments.keySet());
        Collections.sort(keyList);
        for (String key : keyList){
            HashMap<String, String> temp = comments.get(key);
            CommentDisplayContainer comment = new CommentDisplayContainer(temp.get("Username"), temp.get("Message"), temp.get("userId"));
            out.add(comment);
        }
        return out;
    }
}
