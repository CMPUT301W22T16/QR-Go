package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a user. Contains all user contact info and identifying data
 */
public abstract class User {
    // Note: UML says that this is an Integer, but I made it a string since we're using UUID
    private String userid; // Unique ID to identify users.
    private String password; // Password for verifying a user
    private String username;
    private Integer totalScore;
    //private LoginQRCode loginQR;
    //private StatusQRCode statusQR;
    private ArrayList<String> scannedQRCodeIds;
    private String email;

    /**
     * Constructor for user class
     * @throws NoSuchAlgorithmException if user QR codes are not instantiated properly
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public User() throws NoSuchAlgorithmException {
        // Generate a new random UUID for a new user's ID
        userid = UUID.randomUUID().toString();
        // Generate a new random UUID for a new user's password
        password = UUID.randomUUID().toString();
        //username = UsernameGenerator.generateUsername();
        username = "Darius";
        totalScore = 0;
        email = "";
        scannedQRCodeIds = new ArrayList<>();
       //loginQR = new LoginQRCode(this);
        //statusQR = new StatusQRCode(this);
    }

    /**
     * Function that tells if a user is an owner and should have enhanced privileges
     * @return Returns true if user is an owner, false otherwise
     */
    public abstract Boolean isOwner();

    /**
     * Adds a QR code to the user's list of scanned QR codes and its score to the user's total
     * @param qr GameQRCode object which is to be added to the player's list of scanned QRs
     */
    public void addQRCode(GameQRCode qr) {
        scannedQRCodeIds.add(qr.getId());
        totalScore += qr.getScore();
    }

    /**
     * Deletes a QR code to the user's list of scanned QR codes and subtracts its score from the
     * user's total
     * @param qr GameQRCode object which is to be removed from the player's list of scanned QRs
     */
    public void deleteQRCode(GameQRCode qr) {
        scannedQRCodeIds.remove(qr.getId());
        totalScore -= qr.getScore();
    }

    /**
     * @return ArrayList of IDs of a user's scanned QR codes
     */
    public ArrayList<String> getScannedQRCodeIds() {
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
     * @return user's total score
     */
    public Integer getTotalScore() {
        return totalScore;
    }

    /**
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username user's new username
     * @return true if username was successfully changed, false otherwise
     */
    public boolean setUsername(String username) {
        if (!UsernameGenerator.isValidUsername(username)) {
            return false;
        }
        this.username = username;
        return true;
    }

    /**
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return User's login QR code
     */
//    public LoginQRCode getLoginQR() {
//        return loginQR;
//    }

    /**
     * @return User's status QR code
     */
//    public StatusQRCode getStatusQR() {
//        return statusQR;
//    }

    /**
     * @param email user's new email
     */
    public void setEmail(String email) {
        this.email = email;
    }


}
