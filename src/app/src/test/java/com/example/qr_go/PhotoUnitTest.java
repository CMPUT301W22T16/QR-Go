package com.example.qr_go;
import org.junit.Test;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class PhotoUnitTest {
    @Test
    public void checkQRPHotoID() throws NoSuchAlgorithmException{
        QRPhoto photo = new QRPhoto(null, "123","456");
        assertEquals("123", photo.getQRID());
        assertEquals("456", photo.getPhotographerID());
    }
    @Test
    public void checkQRPHotoIDRandom() throws NoSuchAlgorithmException{
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
    public void checkProfilPicture(){
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
