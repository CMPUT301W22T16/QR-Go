package com.example.qr_go.objects;

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
    private boolean deleted;
    private boolean owner;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Player()   {
        super();
        boolean deleted = false;
        boolean owner = false;
    }

    /**
     * Constructor for player class for mock data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Player(String uuid, String password, String username, String email) {
        super(uuid, password, username, email);
        boolean deleted = false;
        boolean owner = false;
    }

    /**
     * @return Returns false as players are not owners
     */
    public Boolean isOwner() {
        return owner;
    }

    /**
     * @return if the player is deleted
     */
    public Boolean isDeleted(){ return deleted;}
    /**
     * flags the player as deleted
     */
    public void deletePlayer(){deleted = true;}


}
