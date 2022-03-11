package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;


// TODO determine any player-specific functionality

/**
 * Class that represents a regular player user
 */
public class Player extends User {

    /**
     * Constructor for player class
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Player()   {
        super();
    }

    /**
     * Constructor for player class for mock data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Player(String uuid, String password, String username, Integer totalScore, String email) {
        super(uuid, password, username, totalScore, email);
    }

    /**
     * @return Returns false as players are not owners
     */
    public Boolean isOwner() {
        return false;
    }

}
