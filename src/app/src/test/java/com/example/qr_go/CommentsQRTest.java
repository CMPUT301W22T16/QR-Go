package com.example.qr_go;

import static org.junit.Assert.assertEquals;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

/**
 * this is the revised version of commentstest
 */
public class CommentsQRTest {
    public static final String MOCK_MSG = "Test message 1";
    public static final String MOCK_USER = "user1";

    @Test
    public void testGetMessage() {
        Player player = new Player();
        player.setUsername(MOCK_USER);
        CommentsQR comments = new CommentsQR(player, MOCK_MSG, null);
        assertEquals(MOCK_MSG, comments.getMessage(player.getUserid()));
    }
    @Test
    public void testSetMessage() {
        Player player = new Player();
        player.setUsername(MOCK_USER);
        CommentsQR comments = new CommentsQR(player, MOCK_MSG, null);
        assertEquals(MOCK_MSG, comments.getMessage(player.getUserid()));
        String msg = "New Message";
        comments.setMessage(player.getUserid(), msg);
        assertEquals(msg, comments.getMessage(player.getUserid()));
    }
    @Test
    public void testCommentListSize() {
        Player player = new Player();
        player.setUsername(MOCK_USER);
        CommentsQR comments = new CommentsQR(player, MOCK_MSG, null);
        assertEquals(MOCK_MSG, comments.getMessage(player.getUserid()));
        assertEquals(1, comments.getComments().size());
        // as of right now only 1 user can comment
        comments.addComment(player, "this is new message", null);
        assertEquals("this is new message", comments.getMessage(player.getUserid()));
        assertEquals(1, comments.getComments().size());
        // new message from a different user
        String msg = "New Message";
        Player player1 = new Player();
        player1.setUsername("iamUser2");
        comments.addComment(player1, msg, null);
        assertEquals(msg, comments.getMessage(player1.getUserid()));
        assertEquals(2, comments.getComments().size());
        ArrayList<Comment> commentArrayList = comments.getCommentObjects();
        if (commentArrayList.get(0).getUsername() == MOCK_USER){
            assertEquals("this is new message", commentArrayList.get(0).getMessage());
            assertEquals("New Message", commentArrayList.get(1).getMessage());
        }
        else{
            assertEquals("New Message", commentArrayList.get(0).getMessage());
            assertEquals("this is new message", commentArrayList.get(1).getMessage());
        }
    }

}
