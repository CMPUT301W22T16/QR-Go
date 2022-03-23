package com.example.qr_go;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StatusQRCodeTest {
    private String mockData = "mock-user-id";
    @Test
    public void testGetData(){
        StatusQRCode statusQRCode1 = new StatusQRCode(mockData);
        assertEquals(mockData, statusQRCode1.getData());
        // Should remove qr identifier
        StatusQRCode statusQRCode2 = new StatusQRCode(StatusQRCode.QR_IDENTIFIER + mockData);
        assertEquals(mockData, statusQRCode2.getData());
    }
}
