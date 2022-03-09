package com.example.qr_go;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

public class NewGameQRActivity extends AppCompatActivity {
    private GameQRCode gameQRCode;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_qractivity);

        // (1) Get the Game QR code string and create a new Game QR code
        Intent intent = getIntent();
        String qrString = intent.getStringExtra("QR");

        try {
            gameQRCode = new GameQRCode(qrString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // (2) Set score text
        TextView scoreView = findViewById(R.id.score);
        scoreView.setText(String.valueOf(gameQRCode.getScore()));
    }
}