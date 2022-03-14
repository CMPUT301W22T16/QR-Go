package com.example.qr_go;

import java.util.Comparator;

public class QRListScoreComparator implements Comparator<QRListDisplayContainer> {
    public int compare(QRListDisplayContainer qr1, QRListDisplayContainer qr2) {
        return qr2.getScore().compareTo(qr1.getScore());
    }
}
