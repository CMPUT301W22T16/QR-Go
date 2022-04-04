package com.example.qr_go.objects;

import static org.junit.Assert.assertEquals;

import com.example.qr_go.containers.CommentDisplayContainer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    public void testCommentListSize() {
        Player player = new Player();
        player.setUsername(MOCK_USER);
        CommentsQR comments = new CommentsQR(player, MOCK_MSG, null);
        assertEquals(MOCK_MSG, comments.getMessage(player.getUserid()));
        assertEquals(1, comments.getCommentObjects().size());
        // add new comment, this time delay is so that the index can be changed
        //(has to be a different second)
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        comments.addComment(player, "this is new message", null);
        assertEquals(2, comments.getCommentObjects().size());
        // new message from a different user
        String msg = "New Message";
        Player player1 = new Player();
        player1.setUsername("iamUser2");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        comments.addComment(player1, msg, null);
        assertEquals(3, comments.getCommentObjects().size());
        ArrayList<CommentDisplayContainer> commentArrayList = comments.getCommentObjects();
    }

}
