package com.example.qr_go;

/**
 * Class for holding only relevant information from a GameQRCode to display in a list
 */
public class QRListDisplayContainer {
    private Integer score;
    private String id;

    public QRListDisplayContainer(Integer score, String id) {
        this.score = score;
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public String getId() {
        return id;
    }
}
