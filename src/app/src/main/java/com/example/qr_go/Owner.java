package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;

// TODO determine any owner-specific functionality

/**
 * Class that represents an owner user with enhanced privileges
 */
public class Owner extends User {

    /**
     * Constructor for owner class
     * @throws NoSuchAlgorithmException if user QR codes are not instantiated properly
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Owner() throws NoSuchAlgorithmException  {
        super();
    }

    /**
     * @return Returns true as owners are owners
     */
    public Boolean isOwner() {
        return true;
    }

}
