package com.example.qr_go.objects;

public class Photo {
    /**
     * General class for photo, contains the image as a variable
     * @param image
     *      itself
     * @author DarFang
     */

    /*
    we need to compress the images to the database
    we need to be able to load the image into the activity
     */
    protected byte[] image;
    Photo(byte[] image){
        this.image = image;

    }
}
