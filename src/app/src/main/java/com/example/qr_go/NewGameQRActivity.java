package com.example.qr_go;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

public class NewGameQRActivity extends AppCompatActivity {
    private GameQRCode gameQRCode;
    private final int LOCATION_REQUEST_CODE = 101;
    private CheckBox locationCheckbox;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_qractivity);

        // (0) Get views and instances
        locationCheckbox = findViewById(R.id.location_checkbox);

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

        // (3) Request location permissions
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                // If has permissions, set location by default
                setQRLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void disableLocation() {
        locationCheckbox.setChecked(false); // un-check
        locationCheckbox.setEnabled(false); // disabled
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission granted, set the location
                setQRLocation();
            } else {
                // No permission, so uncheck the location and disable
                disableLocation();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setQRLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                @SuppressLint("MissingPermission") Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                @SuppressLint("MissingPermission") Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                gameQRCode.setLocation(gpsLocation!=null?   gpsLocation : networkLocation); // set the location
            }

        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            Toast.makeText(this, "failed to get location", Toast.LENGTH_LONG).show();
            // disable location
            disableLocation();
        }

    }

}