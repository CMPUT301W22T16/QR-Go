package com.example.qr_go;

/**
 * Class for holding only relevant information from a GameQRCode to display in a list
 */
public class QRListDisplayContainer {
    private Integer score;
    private String id;
    private Double lat;
    private Double lon;
    private Float distance;

    public QRListDisplayContainer(Integer score, String id, Double lat, Double lon, Float distance) {
        this.score = score;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
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
}
