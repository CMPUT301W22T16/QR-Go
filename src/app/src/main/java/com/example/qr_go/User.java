package com.example.qr_go;

import java.util.ArrayList;
import java.util.UUID;

/* TODO:
    - Determine and implement functionality of login() and showStatus() functions
    - Are we supposed to be giving a user their username, or should that be provided by the user
      and be passed in as an argument to the constructor, or should we leave it empty and let the
      user set it after creating their account? Should we be checking for username uniqueness?
    - Use proper login and status qr code constructors
    - Do we want to make an addScannedQR() method, or would a user.getScannedQRCodeIds().add()
      suffice?
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
    public void User() {
        // Generate a new random UUID for a new user's ID
        userid = UUID.randomUUID().toString();
        // Generate a new random UUID for a new user's password
        password = UUID.randomUUID().toString();
        username = "";
        email = "";
        scannedQRCodeIds = new ArrayList<Integer>();
        loginQR = new LoginQRCode(); // Generate a new login QR code (unsure of args)
        statusQR = new StatusQRCode(); // Generate a new status QR code (unsure of args)
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
     * @param email user's new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    // Not too sure if what these methods are supposed to do.
    public void login(LoginQRCode loginQR) {
        // ...
    }

    public void showStatus(statusQRCode statusQR) {
        // ...
    }
}
