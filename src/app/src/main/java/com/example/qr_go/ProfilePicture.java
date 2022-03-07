package com.example.qr_go;

public class ProfilePicture extends Photo{
    private String userID;
    /** Properties of the profile picture, has the user id attached
     * @param image
     *      the image
     * @param userID
     *      the user ID attached to this photo, the person who took it
     *
     * @author DarFang
     */
    ProfilePicture(byte[] image, String userID){
        super(image);
        this.userID = userID;
    }

    /**
     * getters
     * @return UserID, string
     */
    public String getUserID() {
        return userID;
    }
}
