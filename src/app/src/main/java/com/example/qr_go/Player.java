package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;

// TODO determine any player-specific functionality

/**
 * Class that represents a regular player user
 */
public class Player extends User {

    /**
     * Constructor for player class
     * @throws NoSuchAlgorithmException if user QR codes are not instantiated properly
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Player() throws NoSuchAlgorithmException  {
        super();
    }

    /**
     * @return Returns false as players are not owners
     */
    public Boolean isOwner() {
        return false;
    }

}
