package com.example.qr_go.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CommentList {
    private ArrayList<HashMap<String,String>> comments;
    public CommentList(){
        this.comments = new ArrayList<>();
    }
    public CommentList(User user, String message){
        this.comments = new ArrayList<>();
        HashMap<String, String> details = new HashMap<>();
        details.put("userId", user.getUserid());
        details.put("Username", user.getUsername());
        details.put("Message", message);
        comments.add(details);
    }
    public void addComment(User user, String message){
        HashMap<String, String> details = new HashMap<>();
        details.put("userId", user.getUserid());
        details.put("Username", user.getUsername());
        details.put("Message", message);
        comments.add(details);
    }
    public void deleteCommenter(String userId) {
        return;
    }
    public ArrayList<HashMap<String, String>> getComments(){return comments;}
}
