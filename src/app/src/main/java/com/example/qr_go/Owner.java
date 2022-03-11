package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;


// TODO determine any owner-specific functionality

/**
 * Class that represents an owner user with enhanced privileges
 */
public class Owner extends User {

    /**
     * Constructor for owner class
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Owner()   {
        super();
    }

    /**
     * @return Returns true as owners are owners
     */
    public Boolean isOwner() {
        return true;
    }

}
