package com.example.qr_go.objects;

import android.os.Build;
import android.util.Patterns;

import androidx.annotation.RequiresApi;
import androidx.core.util.PatternsCompat;

import com.example.qr_go.utils.UsernameGenerator;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a user. Contains all user contact info and identifying data
 */
public abstract class User {
    private String userid; // Unique ID to identify users.
    private String password; // Password for verifying a user
    private String username;
    private HashMap<String, Integer> scannedQRCodeIds;
    private String email;

    public static final String CURRENT_USER = "LOGIN";
    public static final String USER_ID = "uuid";
    public static final String USER_PWD = "password";


    /**
     * Constructor for user class
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public User() {
        // Generate a new random UUID for a new user's ID
        userid = UUID.randomUUID().toString();
        // Generate a new random UUID for a new user's password
        password = UUID.randomUUID().toString();
        username = UsernameGenerator.generateUsername();
        email = "";
        scannedQRCodeIds = new HashMap<>();
    }

    /**
     * Constructor for user class to generate mock data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public User(String uuid, String password, String username, String email) {
        // Generate a new random UUID for a new user's ID
        userid = uuid;
        // Generate a new random UUID for a new user's password
        this.password = password;
        this.username = username;
        this.email = email;
        scannedQRCodeIds = new HashMap<>();
    }

    /**
     * Function that tells if a user is an owner and should have enhanced privileges
     *
     * @return Returns true if user is an owner, false otherwise
     */
    public abstract Boolean isOwner();

    /**
     * Adds a QR code to the user's list of scanned QR codes if the user has not already scanned
     * it
     *
     * @param qr GameQRCode object which is to be added to the player's list of scanned QRs
     * @return true if the qr code was successfully added, false otherwise
     */
    public Boolean addQRCode(GameQRCode qr) {
        String qrID = qr.getId();
        Integer qrScore = qr.getScore();
        if (!scannedQRCodeIds.containsKey(qrID)) {
            scannedQRCodeIds.put(qrID, qrScore);
            return true;
        }
        return false;
    }

    /**
     * Deletes a QR code to the user's list of scanned QR codes and subtracts its score from the
     * user's total
     *
     * @param qrID the ID of the gameQRcode
     * @return true if the qr code was successfully deleted, false otherwise
     */
    public Boolean deleteQRCode(String qrID) {
        if (scannedQRCodeIds.containsKey(qrID)) {
            scannedQRCodeIds.remove(qrID);
            return true;
        }
        return false;
    }

    /**
     * @return ArrayList of IDs of a user's scanned QR codes
     */
    public HashMap<String, Integer> getScannedQRCodeIds() {
        return scannedQRCodeIds;
    }

    /**
     * @return user's ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the sum of all the user's scores
     *
     * @return user's total score
     */
    public Integer getTotalScore() {
        Integer totalScore = 0;
        for (Integer score : scannedQRCodeIds.values()) {
            totalScore += score;
        }
        return totalScore;
    }

    /**
     * Returns the highest unique score that the user has
     *
     * @return user's highest unique score
     */
    public Integer getHighestUniqueScore() {
        Integer highestScore = 0;
        for (Integer score : scannedQRCodeIds.values()) {
            if (score > highestScore) {
                highestScore = score;
            }
        }
        return highestScore;
    }

    /**
     * Returns the lowest unique score that the user has
     *
     * @return user's lowest unique score
     */
    public Integer getLowestUniqueScore() {
        Integer lowestScore = Integer.MAX_VALUE;
        for (Integer score : scannedQRCodeIds.values()) {
            if (score < lowestScore) {
                lowestScore = score;
            }
        }
        // if no codes were found, return 0
        return lowestScore == Integer.MAX_VALUE ? 0 : lowestScore;
    }

    /**
     * Returns the QR Code with the highest unique score that the user has
     *
     * @return QR code with user's highest unique score, returns null if not found
     */
    public Map.Entry<String, Integer> getHighestQRCode() {
        Integer highestScore = getHighestUniqueScore();
        for (Map.Entry<String, Integer> entry : scannedQRCodeIds.entrySet()) {
            if (highestScore.equals(entry.getValue())) {
                return entry;
            }
        }
        return new AbstractMap.SimpleEntry<>("        ", 0);
    }

    /**
     * Returns the QR Code with the lowest unique score that the user has
     *
     * @return QR code with user's lowest unique score, returns null if not found
     */
    public Map.Entry<String, Integer> getLowestQRCode() {
        Integer lowestScore = getLowestUniqueScore();
        for (Map.Entry<String, Integer> entry : scannedQRCodeIds.entrySet()) {
            if (lowestScore.equals(entry.getValue())) {
                return entry;
            }
        }
        return new AbstractMap.SimpleEntry<>("        ", 0);
    }

    /**
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username user's new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return user's email
     */
    public String getEmail() {
        return email;
    }


    /**
     * @param email user's new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Whether the user email is valid
     *
     * @return True/False if email is a valid email
     * Source: Stack overflow
     * URL: https://stackoverflow.com/a/15808057
     * Author: user1737884 [https://stackoverflow.com/users/1737884/user1737884]
     */
    public boolean isEmailValid() {
        if (email == null) return false;
        return !email.isEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches();
    }

}
