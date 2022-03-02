package com.example.qr_go;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class LoginQRCodeTest {

    @Ignore // need to mock firestore
    @Test
    public void testIsLoginValid() throws NoSuchAlgorithmException {

        String id = "testid";
        String password = "testpassword";
        String input = id + "\n" + password;
        LoginQRCode loginQRCode = new LoginQRCode(input);

        // since nothing exists in firestore
        assertFalse(loginQRCode.isLoginValid(input));

    }
}
