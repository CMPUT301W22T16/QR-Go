package com.example.qr_go.containers;

import android.graphics.Bitmap;

/**
 * Class for holding only relevant information from a GameQRCode to display in a list
 */
public class QRListDisplayContainer {
    private Integer score;
    private String id;
    private Double lat;
    private Double lon;
    private Float distance;
    private Bitmap picture;
    private String firstUserID;

    public QRListDisplayContainer(Integer score, String id, Double lat, Double lon, Float distance, String userid) {
        this.score = score;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
        this.picture = null;
        this.firstUserID = userid;
    }

    public Integer getScore() {
        return score;
    }

    public String getId() {
        return id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Float getDistance() {
        return distance;
    }

    public String getFirstUserID() {
        return firstUserID;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
