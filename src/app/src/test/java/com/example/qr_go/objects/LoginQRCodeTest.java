package com.example.qr_go.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.qr_go.objects.LoginQRCode;

import org.junit.Ignore;
import org.junit.Test;


public class LoginQRCodeTest {
    private String mockUserId = "user-id";
    private String mockPassword = "password";

    @Ignore // need to mock firestore
    @Test
    public void testIsLoginValid() {

        String id = "testid";
        String password = "testpassword";
        String input = id + "\n" + password;
        LoginQRCode loginQRCode = new LoginQRCode(input);

        // since nothing exists in firestore
        // TODO: I have changed isLoginValid to take callback functions
//        assertFalse(loginQRCode.isLoginValid());
    }

    @Test
    public void testParsingContent() {
        LoginQRCode loginQRCode1 = new LoginQRCode(mockUserId + "\n" + mockPassword);
        assertEquals(mockUserId, loginQRCode1.getUserId());
        assertEquals(mockPassword, loginQRCode1.getPassword());
        // With QR-IDENTIFIER in the front
        LoginQRCode loginQRCode2 = new LoginQRCode(LoginQRCode.QR_IDENTIFIER+ mockUserId + "\n" + mockPassword);
        assertEquals(mockUserId, loginQRCode2.getUserId());
        assertEquals(mockPassword, loginQRCode2.getPassword());
    }
}
