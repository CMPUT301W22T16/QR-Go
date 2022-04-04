package com.example.qr_go.comparators;

import com.example.qr_go.containers.QRListDisplayContainer;

import java.util.Comparator;

public class QRListScoreComparator implements Comparator<QRListDisplayContainer> {
    public int compare(QRListDisplayContainer qr1, QRListDisplayContainer qr2) {
        return qr2.getScore().compareTo(qr1.getScore());
    }
}
