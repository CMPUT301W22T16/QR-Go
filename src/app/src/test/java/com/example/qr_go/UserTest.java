package com.example.qr_go;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import java.security.NoSuchAlgorithmException;

public class UserTest {
    private User testUser;

    @Before
    public void setup() {
        testUser = new User();
    }

    @Ignore
    @Test
    public void userLoginQRTest() throws NoSuchAlgorithmException {
        String loginString = testUser.getUserid()+"\n"+testUser.getPassword();

        // Compare hashed login data and user's login QR data
        QRCode newLoginQR = new QRCode(loginString);
        LoginQRCode genLoginQR = testUser.getLoginQR();
        assertEquals(newLoginQR.getHash(), genLoginQR.getHash());
    }

    @Ignore
    @Test
    public void userStatusQRTest() throws NoSuchAlgorithmException {
        String statusString = testUser.getUserid();

        // Compare hashed status data and user's status QR data
        QRCode newStatusQR = new QRCode(statusString);
        StatusQRCode genStatusQR = testUser.getStatusQR();
        assertEquals(newStatusQR.getHash(), genStatusQR.getHash());
    }

}
