package com.example.qr_go.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.qr_go.objects.QRCode;

import org.junit.Test;


public class QRCodeTest {

    @Test
    public void convertsStringToHash()  {
        QRCode code = new QRCode("BFG5DGW54\n");
        assertEquals("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", code.getHash());
    }
    @Test
    public void hashIsId(){
        QRCode code = new QRCode("BFG5DGW54\n");
        assertEquals(code.getId(), code.getHash());
    }
}
