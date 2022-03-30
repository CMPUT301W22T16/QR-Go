package com.example.qr_go.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.qr_go.objects.GameQRCode;
import com.example.qr_go.objects.Player;
import com.example.qr_go.objects.User;

import org.junit.Before;
import org.junit.Test;



public class UserTest {
    private User testUser;

    @Before
    public void setup()  {
        testUser = new Player();
    }

    @Test
    public void testAddQRCode(){
        Integer prevSize = testUser.getScannedQRCodeIds().size();
        GameQRCode qr = new GameQRCode("test");
        testUser.addQRCode(qr);
        // Test that the number of scanned qr codes increases
        assertEquals(prevSize+1, testUser.getScannedQRCodeIds().size());
        // Test that the user does not add the qr code twice
        assertFalse(testUser.addQRCode(qr));
    }

    @Test
    public void testRemoveQRCode(){
        GameQRCode qr = new GameQRCode("test");
        testUser.addQRCode(qr);
        Integer prevSize = testUser.getScannedQRCodeIds().size();
        testUser.deleteQRCode(qr.getId());
        // Test that the number of scanned qr codes decreases
        assertEquals(prevSize-1, testUser.getScannedQRCodeIds().size());
        // Test that the user does not delete qr codes it does not have
        assertFalse(testUser.deleteQRCode(qr.getId()));
    }

    @Test
    public void testGetTotalScore() {
        // Test that total score is 0 when no qr codes have been added yet
        assertTrue(testUser.getTotalScore().equals(0));
        GameQRCode qr1 = new GameQRCode("test");
        GameQRCode qr2 = new GameQRCode("test12345");
        GameQRCode qr3 = new GameQRCode("12345test");
        Integer sumScores = qr1.getScore() + qr2.getScore() + qr3.getScore();
        testUser.addQRCode(qr1);
        testUser.addQRCode(qr2);
        testUser.addQRCode(qr3);
        // Test that it gets total score of added values
        assertEquals(sumScores, testUser.getTotalScore());
        testUser.deleteQRCode(qr1.getId());
        sumScores -= qr1.getScore();
        // Test that it gets total score after deleted values
        assertEquals(sumScores, testUser.getTotalScore());
    }

    @Test
    public void testGetHighestScore() {
        Integer currentMax = 0;
        Integer maxIndex = 0;
        // Test that highest score is 0 when no qr codes have been added yet
        assertTrue(testUser.getHighestUniqueScore().equals(0));
        GameQRCode qr1 = new GameQRCode("test");
        GameQRCode qr2 = new GameQRCode("test12345");
        GameQRCode qr3 = new GameQRCode("12345test");
        GameQRCode[] qrlist = {qr1, qr2, qr3};
        int i = 0;
        for (GameQRCode qr : qrlist) {
            if (qr.getScore() > currentMax) {
                currentMax = qr.getScore();
                maxIndex = i;
            }
            i++;
        }
        testUser.addQRCode(qr1);
        testUser.addQRCode(qr2);
        testUser.addQRCode(qr3);
        // Test that it gets total score of added values
        assertEquals(currentMax, testUser.getHighestUniqueScore());
        testUser.deleteQRCode(qrlist[maxIndex].getId());
        // Test that it gets total score after deleted values
        assertNotEquals(currentMax, testUser.getHighestUniqueScore());
        assertTrue(testUser.getHighestUniqueScore() <= currentMax);
    }

}
