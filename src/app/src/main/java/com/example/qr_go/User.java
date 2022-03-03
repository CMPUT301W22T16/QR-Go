package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

import android.util.Log;

/* TODO:
    - Abstract User class and make Owner and Player class
    - Write a username generator
 */


/**
 * Represents a user. Contains all user contact info and identifying data
 */
public class User {
    // Note: UML says that this is an Integer, but I made it a string since we're using UUID
    private String userid; // Unique ID to identify users.
    private String password; // Password for verifying a user
    private String username;
    private LoginQRCode loginQR;
    private StatusQRCode statusQR;
    private ArrayList<Integer> scannedQRCodeIds;
    private String email;

    /**
     * Constructor for user class
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public User() throws NoSuchAlgorithmException {
        // Generate a new random UUID for a new user's ID
        userid = UUID.randomUUID().toString();
        // Generate a new random UUID for a new user's password
        password = UUID.randomUUID().toString();
        username = "";
        email = "";
        scannedQRCodeIds = new ArrayList<Integer>();
        loginQR = new LoginQRCode(this);
        statusQR = new StatusQRCode(this);
    }

    /**
     * @return ArrayList of IDs of a user's scanned QR codes
     */
    public ArrayList<Integer> getScannedQRCodeIds() {
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
     * @return User's login QR code
     */
    public LoginQRCode getLoginQR() {
        return loginQR;
    }

    /**
     * @return User's status QR code
     */
    public StatusQRCode getStatusQR() {
        return statusQR;
    }

    /**
     * @param email user's new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
