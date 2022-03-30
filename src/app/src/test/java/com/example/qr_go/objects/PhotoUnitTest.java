package com.example.qr_go.objects;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.qr_go.objects.ProfilePicture;
import com.example.qr_go.objects.QRPhoto;

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
    public void checkQRPhotoID() {
        QRPhoto photo = new QRPhoto(null, "123","456");
        assertEquals("123", photo.getQRID());
        assertEquals("456", photo.getPhotographerID());
    }
    @Test
    public void checkQRPhotoIDRandom() {
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
    public void checkProfilePictureRandom() {
        String userID;
        for (int i = 0; i <10; i++) {
            userID = UUID.randomUUID().toString() + i;
            ProfilePicture profileIcon = new ProfilePicture(null, userID);
            assertEquals(userID, profileIcon.getUserID());
        }
    }
}
