package com.example.qr_go.objects;

public class QRPhoto extends Photo{
    private String photographerID;
    private String QRID;
    /** QR Photo with properties, has ids of the user that took it and the QR id attached to it
    * @param image
    *      the image
    * @param QRID
    *     the QR ID attached to this photo
    * @param photographerID
    * the user ID attached to this photo, the person who took it
    *
    * @author DarFang
    *
    * @version 1.0
    * @since Mar 2 2022
    */
    public QRPhoto(byte[] image, String QRID, String photographerID){
        super(image);
        this.photographerID = photographerID;
        this.QRID = QRID;
    }

    /**
     * Get value photographerID
     *
     * @return photographerID
     *
     * @author DarFang
     */
    public String getPhotographerID() {
        return photographerID;
    }
    /**
     * Get value QRID
     *
     * @return getQRID
     *
     * @author DarFang
     */
    public String getQRID() {
        return QRID;
    }
}
