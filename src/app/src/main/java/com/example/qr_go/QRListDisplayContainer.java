package com.example.qr_go;

/**
 * Class for holding only relevant information from a GameQRCode to display in a list
 */
public class QRListDisplayContainer {
    private Integer score;
    private String id;
    private Float distance;

    public QRListDisplayContainer(Integer score, String id, Float distance) {
        this.score = score;
        this.id = id;
        this.distance = distance;
    }

    public Integer getScore() {
        return score;
    }

    public String getId() {
        return id;
    }

    public Float getDistance() {
        return distance;
    }
}
