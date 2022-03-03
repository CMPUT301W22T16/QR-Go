package com.example.qr_go;

public class QRPhoto extends Photo{
    String photographerID;
    String QRID;
    QRPhoto(byte[] image, String QRID, String photographerID){
        super(image);
        this.photographerID = photographerID;
        this.QRID = QRID;
    }

    public String getPhotographerID() {
        return photographerID;
    }

    public String getQRID() {
        return QRID;
    }
}
