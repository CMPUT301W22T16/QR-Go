package com.example.qr_go;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class QRInfoActivity extends AppCompatActivity {

    private GameQRCode selectedQR;
    private QRPhoto[] QRPhotos;
    private Comment[] comments;
//    private GeoLocation location;     // TODO: uncomment after GeoLocation is implemented

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);
    }

    public void showUsersList() {

    }

    /**
     * Display all comments added to this page
     */
    public void showComments() {

    }

    public void showLocation() {

    }

    public void showIcon() {

    }

    /**
     * Adds a new comment to this page
     */
    public void addComment() {

    }

}
