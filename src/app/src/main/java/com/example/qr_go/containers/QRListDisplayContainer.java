package com.example.qr_go.containers;

/**
 * Class for holding only relevant information from a GameQRCode to display in a list
 */
public class QRListDisplayContainer {
    private Integer score;
    private String id;
    private Double lat;
    private Double lon;
    private Float distance;
    private byte[] picture;

    public QRListDisplayContainer(Integer score, String id, Double lat, Double lon, Float distance) {
        this.score = score;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
        this.picture = new byte[]{};
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

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
