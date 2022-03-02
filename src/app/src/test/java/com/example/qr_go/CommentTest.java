package com.example.qr_go;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.UUID;

public class CommentTest {

    public static final String MOCK_MSG = "Test message 1";

    private Comment mockComment() {
        String message = MOCK_MSG;

        // GameQRCode qr = mockQR();    // TODO: for after GameQRCode
        UUID QRId = UUID.randomUUID();

        // Comment comment = new Comment(qr.getQRId, message);      // TODO: for after GameQRCode
        Comment comment = new Comment(QRId, message);
        return comment;
    }

    // TODO: make and use this once GameQRCode is developed
//    private GameQRCode mockQR() {
//        return new GameQRCode( ... );
//    }

    @Test
    void testGetMessage() {
        Comment comment = mockComment();
        assertEquals(MOCK_MSG, comment.getMessage());
    }

    @Test
    void testSetMessage() {
        Comment comment = mockComment();

        assertEquals(MOCK_MSG, comment.getMessage());

        String msg = "New Message";
        comment.setMessage(msg);
        assertEquals(msg, comment.getMessage());
    }

}
