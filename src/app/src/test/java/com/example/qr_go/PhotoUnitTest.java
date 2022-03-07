package com.example.qr_go;
import org.junit.Test;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Test the photo and it's sub classes
 * @author DarFang
 */

/*
need to add byte image testing
 */
public class PhotoUnitTest {
    @Test
    public void checkQRPhotoID() throws NoSuchAlgorithmException{
        QRPhoto photo = new QRPhoto(null, "123","456");
        assertEquals("123", photo.getQRID());
        assertEquals("456", photo.getPhotographerID());
    }
    @Test
    public void checkQRPhotoIDRandom() throws NoSuchAlgorithmException{
        String userID, QRID;
        for (int i = 0; i <10; i++) {
            userID = UUID.randomUUID().toString() + i;
            QRID = UUID.randomUUID().toString() + (i + 1);
            QRPhoto photo = new QRPhoto(null, QRID, userID);
            assertEquals(QRID, photo.getQRID());
            assertEquals(userID, photo.getPhotographerID());
        }
    }
    @Test
    public void checkProfilePicture(){
        ProfilePicture profileIcon = new ProfilePicture(null, "123");
        assertEquals("123", profileIcon.getUserID());
    }
    @Test
    public void checkProfilePictureRandom() throws NoSuchAlgorithmException{
        String userID;
        for (int i = 0; i <10; i++) {
            userID = UUID.randomUUID().toString() + i;
            ProfilePicture profileIcon = new ProfilePicture(null, userID);
            assertEquals(userID, profileIcon.getUserID());
        }
    }
}
