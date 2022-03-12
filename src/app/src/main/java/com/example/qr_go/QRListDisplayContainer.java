package com.example.qr_go;

public class QRListDisplayContainer {
    private Integer score;
    private String hash;

    public QRListDisplayContainer(Integer score, String hash) {
        this.score = score;
        this.hash = hash;
    }

    public Integer getScore() {
        return score;
    }

    public String getHash() {
        return hash;
    }
}
