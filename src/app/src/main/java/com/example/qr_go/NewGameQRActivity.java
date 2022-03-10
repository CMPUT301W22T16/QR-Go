package com.example.qr_go;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class NewGameQRActivity extends AppCompatActivity {
    private GameQRCode gameQRCode;
    private final int LOCATION_REQUEST_CODE = 101;
    private CheckBox locationCheckbox;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

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

        gameQRCode = new GameQRCode(qrString);


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

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setQRLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // https://stackoverflow.com/a/36501202
                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        gameQRCode.setLocation(location); // set the location
                    }
                };
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }

        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            Toast.makeText(this, "failed to get location", Toast.LENGTH_LONG).show();
            // disable location
            disableLocation();
        }
    }

    /**
     * Launch Take Photo activity to take a photo of the QR object
     * https://developer.android.com/training/camera/photobasics#java
     *
     * @param view
     */
    public void launchTakePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "failed to launch camera activity", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Used by launchTakePhoto to get the photo taken by the user's camera
     * When photo is taken, display it on the image view, and save it
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.take_qr_photo_imageview);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    /**
     * "Save" button is clicked
     * Save QR code to database and then redirect to another activity
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveNewQRCode(View view) {
        // If user has unchecked the location, then set location as null
        if (!locationCheckbox.isChecked()){
            gameQRCode.setLocation(null);
        }

        // Save QR code to database
        QRGoDBUtil db = new QRGoDBUtil();
        // TODO: replace DUMMY-USER-ID with my user id
        db.GrabQRFromDatabase(gameQRCode.getId(), "DUMMY-USER-ID");

        // TODO: redirect to the new QR code activity?
        // Go to view QR code activity
        startActivity(new Intent(this, MapsActivity.class));
    }

}