package com.example.qr_go;

import static org.junit.Assert.assertEquals;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * this is the revised version of commentstest
 */
public class CommentTest {
    public static final String MOCK_MSG = "Test message 1";
    public static final String MOCK_USER = "user1";





    @Test
    public void testGetMessage() {
        Player player = new Player();
        player.setUsername(MOCK_USER);
        Comment comments = new Comment(player, MOCK_MSG, null);
        assertEquals(MOCK_MSG, comments.getMessage(player.getUserid()));
    }
    @Test
    public void testSetMessage() {
        Player player = new Player();
        player.setUsername(MOCK_USER);
        Comment comments = new Comment(player, MOCK_MSG, null);
        assertEquals(MOCK_MSG, comments.getMessage(player.getUserid()));
        String msg = "New Message";
        comments.setMessage(player.getUserid(), msg);
        assertEquals(msg, comments.getMessage(player.getUserid()));
    }
    @Test
    public void testCommentListSize() {
        Player player = new Player();
        player.setUsername(MOCK_USER);
        Comment comments = new Comment(player, MOCK_MSG, null);
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
        assertEquals(msg, comments.getMessage(player.getUserid()));
        assertEquals(2, comments.getComments().size());
    }

}
