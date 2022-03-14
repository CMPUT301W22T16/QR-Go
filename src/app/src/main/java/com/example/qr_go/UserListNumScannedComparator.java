package com.example.qr_go;

import java.util.Comparator;

/**
 * Comparator for sorting list of users by the number of QR codes they've scanned
 */
public class UserListNumScannedComparator implements Comparator<UserListDisplayContainer> {
    public int compare(UserListDisplayContainer u1, UserListDisplayContainer u2) {
        return u2.getNumQRs().compareTo(u1.getNumQRs());
    }
}
