package com.example.qr_go;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a user. Contains all user contact info and identifying data
 */
public abstract class User {
    private String userid; // Unique ID to identify users.
    private String password; // Password for verifying a user
    private String username;
    private LoginQRCode loginQR;
    private StatusQRCode statusQR;
    private HashMap<String, Integer> scannedQRCodeIds;
    private String email;

    public static final String CURRENT_USER = "LOGIN";
    public static final String USER_ID = "uuid";

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
        //loginQR = new LoginQRCode(this);
        //statusQR = new StatusQRCode(this);
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
        //loginQR = new LoginQRCode(this);
        //statusQR = new StatusQRCode(this);
    }

    /**
     * Function that tells if a user is an owner and should have enhanced privileges
     * @return Returns true if user is an owner, false otherwise
     */
    public abstract Boolean isOwner();

    /**
     * Adds a QR code to the user's list of scanned QR codes if the user has not already scanned
     * it
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
     * @param qr GameQRCode object which is to be removed from the player's list of scanned QRs
     * @return true if the qr code was successfully deleted, false otherwise
     */
    public Boolean deleteQRCode(GameQRCode qr) {
        String qrID = qr.getId();
        if (scannedQRCodeIds.containsKey(qrID)) {
            scannedQRCodeIds.remove(qr.getId());
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

//     /**
//      * @return User's login QR code
//      */
//     public LoginQRCode getLoginQR() {
//         return loginQR;
//     }

//     /**
//      * @return User's status QR code
//      */
//     public StatusQRCode getStatusQR() {
//         return statusQR;
//     }

    /**
     * @param email user's new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
