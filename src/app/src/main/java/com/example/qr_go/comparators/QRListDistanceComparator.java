package com.example.qr_go.comparators;

import com.example.qr_go.containers.QRListDisplayContainer;

import java.util.Comparator;

public class QRListDistanceComparator implements Comparator<QRListDisplayContainer> {
    public int compare(QRListDisplayContainer qr1, QRListDisplayContainer qr2) {
        return qr1.getDistance().compareTo(qr2.getDistance());
    }
}
