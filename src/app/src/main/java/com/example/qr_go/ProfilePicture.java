package com.example.qr_go;

public class ProfilePicture extends Photo{
    String userID;

    ProfilePicture(byte[] image, String userID){
        super(image);
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
