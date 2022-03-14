package com.example.qr_go;

import java.util.ArrayList;

/** list of comments to be stored into database
 *
 */
public class ListComments {
    private ArrayList<Comment> comments;
    ListComments (){
        this.comments = new ArrayList<>();
    }

    /**
     * add comment to the list
     * @param comment comment to be added
     */
    public void addComment(Comment comment){
        comments.add(comment);
    }

    /**
     * get size of the array
     * @return size of the array
     */
    public int size(){
        return comments.size();
    }
}
