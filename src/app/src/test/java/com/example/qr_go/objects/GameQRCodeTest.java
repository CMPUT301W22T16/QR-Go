package com.example.qr_go.objects;

import static org.junit.Assert.assertEquals;

import com.example.qr_go.objects.GameQRCode;

import org.junit.Test;


public class GameQRCodeTest {
    @Test
    public void checkScore() {
        GameQRCode code = new GameQRCode("BFG5DGW54\n");
        assertEquals(111, (int) code.getScore());
        // Empty string QR code is 27 cuz when hashing "" is becomes a longer string
        GameQRCode emptyCode = new GameQRCode("");
        assertEquals(27, (int) emptyCode.getScore());
    }

    @Test
    public void checkScoringAlgorithm() {
        GameQRCode code = new GameQRCode("");
        assertEquals(20, (int) code.calculateScore("00"));
        assertEquals(400, (int) code.calculateScore("000"));
        assertEquals(8000, (int) code.calculateScore("0000"));
        assertEquals(1, (int) code.calculateScore("11"));
        assertEquals(1, (int) code.calculateScore("111"));
        assertEquals(1, (int) code.calculateScore("1111"));
        assertEquals(2, (int) code.calculateScore("22 "));
        assertEquals(4, (int) code.calculateScore("222"));
        assertEquals(8, (int) code.calculateScore("2222"));
        assertEquals(9, (int) code.calculateScore("99"));
        assertEquals(81, (int) code.calculateScore("999"));
        assertEquals(10, (int) code.calculateScore("aa"));
        assertEquals(100, (int) code.calculateScore("aaa"));
        assertEquals(15, (int) code.calculateScore("ff"));
        assertEquals(225, (int) code.calculateScore("fff"));
        assertEquals(11, (int) code.calculateScore("bb"));
        assertEquals(81, (int) code.calculateScore("999"));
        assertEquals(14, (int) code.calculateScore("ee"));
        assertEquals(5, (int) code.calculateScore("55"));
    }
}
