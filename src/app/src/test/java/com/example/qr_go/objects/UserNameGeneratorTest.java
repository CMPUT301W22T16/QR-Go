package com.example.qr_go.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.example.qr_go.objects.QRCode;
import com.example.qr_go.utils.UsernameGenerator;

import org.junit.Test;


public class UserNameGeneratorTest {

    @Test
    public void testGenerateUsername()  {
        String username1 = UsernameGenerator.generateUsername();
        String username2 = UsernameGenerator.generateUsername();
        assertNotNull(username1);
        assertNotNull(username2);
        assertNotEquals(username1, username2);
    }
}
