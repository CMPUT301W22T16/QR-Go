package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * GameQRCode represents QR codes that players will scan to score points
 */
public class GameQRCode extends QRCode {
    private Integer score;
    ArrayList<String> userIds;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public GameQRCode(String qrCodeContents) throws NoSuchAlgorithmException {
        super(qrCodeContents);
        this.userIds = new ArrayList<>();
        this.score = calculateScore();
    }

    private Integer calculateScore() {
        String a = this.hash;
        return 1;
    }

    public Integer getScore() {
        return score;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }
}
