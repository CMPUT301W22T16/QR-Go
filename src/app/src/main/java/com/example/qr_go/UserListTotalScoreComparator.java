package com.example.qr_go;

import java.util.Comparator;

/**
 * Comparator that sorts a list of users by their total score
 */
public class UserListTotalScoreComparator implements Comparator<UserListDisplayContainer> {
    public int compare(UserListDisplayContainer u1, UserListDisplayContainer u2) {
        return u2.getTotalScore().compareTo(u1.getTotalScore());
    }
}
