package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GameQRCode represents QR codes that players will scan to score points
 */
public class GameQRCode extends QRCode {
    private Integer score = 0;
    ArrayList<String> userIds;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public GameQRCode(String qrCodeContents) throws NoSuchAlgorithmException {
        super(qrCodeContents); // gets hash
        this.userIds = new ArrayList<>();
        this.score = calculateScore(this.hash);
    }

    protected Integer calculateScore(String hash) {
        // Scoring algorithm
        // (1) Find repeats using regex pattern
        Pattern pattern = Pattern.compile("([0-9a-f])(\\1+)");
        Matcher matcher = pattern.matcher(hash);

        // (2) Count score
        String group;
        Character firstChar;
        Integer length;
        Double scoreCount = 0.0;
        while (matcher.find()) {
            group = matcher.group();
            firstChar = group.charAt(0);
            length = group.length();
            if (firstChar == '0') {
                // a "0" is 1 point. Many 0 in a row are multiplied by 20^(n-1) whereby n is the number of repeats
                scoreCount += Math.pow(20, length - 1);
            } else if (firstChar > '0' && firstChar <= '9') {
                scoreCount += Math.pow(firstChar - 48, length - 1); // minus 48 from char
            } else if (firstChar >= 'a' && firstChar <= 'z') {
                scoreCount += Math.pow(firstChar - 87, length - 1); // minus 87 from char
            } else if (firstChar >= 'A' && firstChar <= 'Z') {
                scoreCount += Math.pow(firstChar - 55, length - 1); // minus 55 from char
            } else {
                return -1;
            }
        }
        return (int) Math.round(scoreCount);
    }

    public Integer getScore() {
        return score;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }
}
