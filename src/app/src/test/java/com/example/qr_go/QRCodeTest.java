package com.example.qr_go;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class QRCodeTest {

    @Test
    public void convertsStringToHash() throws NoSuchAlgorithmException {
        QRCode code = new QRCode("BFG5DGW54");
        assertEquals("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6  -", code.getHash());
    }
}
