package com.example.qr_go;

import java.util.Comparator;

public class UserListUniqueQRComparator implements Comparator<UserListDisplayContainer> {
    public int compare(UserListDisplayContainer u1, UserListDisplayContainer u2) {
        return u2.getHighestScore().compareTo(u1.getHighestScore());
    }
}
