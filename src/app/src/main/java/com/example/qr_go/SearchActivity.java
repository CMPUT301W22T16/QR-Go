package com.example.qr_go;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class SearchActivity extends AppCompatActivity {
    // The user that is searching on the app
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }
}
