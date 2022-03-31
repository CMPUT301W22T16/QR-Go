package com.example.qr_go.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.JsonReader;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_go.objects.GeocodedLocation;
import com.example.qr_go.utils.QRGoDBUtil;
import com.example.qr_go.R;
import com.example.qr_go.objects.GameQRCode;
import com.example.qr_go.objects.GeoLocation;
import com.example.qr_go.objects.Player;
import com.example.qr_go.utils.QRGoStorageUtil;
import com.example.qr_go.utils.StringUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import io.grpc.internal.JsonParser;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

/**
 * This activity is shown after user scans a QR code
 * It will ask the user for:
 * 1. Photo of object
 * 2. Location
 * Then user will click on save to create the new QR code
 */
public class NewGameQRActivity extends BaseActivity {
    private GameQRCode gameQRCode;
    private Bitmap imageBitmap;
    private final int LOCATION_REQUEST_CODE = 101;
    private CheckBox locationCheckbox;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    FirebaseFirestore firestoreDb;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_qr_activity);
        initializeNavbar();

        // (0) Get views and instances
        locationCheckbox = findViewById(R.id.location_checkbox);
        firestoreDb = MapsActivity.db;


        // (1) Get the Game QR code string and create a new Game QR code
        Intent intent = getIntent();
        String qrString = intent.getStringExtra("QR");

        // Create new GameQRCode instance
        gameQRCode = new GameQRCode(qrString == null ? "" : qrString);

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

        // Start confetti celebration
        KonfettiView konfettiView = findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(2000L)
                .position(0.0, 0.0, 1.0, 0.0)
                .build();
        konfettiView.start(party);
    }

    /**
     * If unable to get user's location or permission is denied, then disable location
     * This will un-check and disable the checkbox
     */
    private void disableLocation() {
        locationCheckbox.setChecked(false); // un-check
        locationCheckbox.setEnabled(false); // disabled
    }

    /**
     * Once user has accepted/denied permission access
     * If permission granted, set the location of the QR code
     * If permission denied, disable the location
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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

    /**
     * Assuming that location permissions is granted
     * The current location of the user is received
     * The location is then set in the game qr code
     * https://stackoverflow.com/a/36501202
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setQRLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        GeoLocation gpsGeoLocation = new GeoLocation(gameQRCode.getId());
                        gpsGeoLocation.setCoords(location.getLongitude(), location.getLatitude());
                        reverseGeocoding(gpsGeoLocation);
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
            imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.take_qr_photo_imageview);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    /**
     * "Save" button is clicked
     * Save QR code to database and then redirect to another activity
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveNewQRCode(View view) {
        Button saveButton = findViewById(R.id.save_game_qr);
        saveButton.setEnabled(false); // disable save once clicked
        // If user has unchecked the location, then set location as null
        if (!locationCheckbox.isChecked()) {
            gameQRCode.setGeoLocation(null);
        }
        saveToDB();
    }

    /**
     * Save game QR code to database
     */
    private void saveToDB() {
        // Save QR code to database
        QRGoDBUtil db = new QRGoDBUtil(this); // pass in context to do toast
        String currentUserId = MapsActivity.getUserId();
        DocumentReference docRef = firestoreDb.collection("Players").document(currentUserId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    Player player = documentSnapshot.toObject(Player.class);
                    db.updateScannedQRtoDB(gameQRCode, player, null);
                    QRGoStorageUtil StorageUtil = new QRGoStorageUtil();
                    StringUtil stringUtil = new StringUtil();
                    // If user has taken a photo, it could be null!
                    if (imageBitmap != null) StorageUtil.updateImageFromStorage(imageBitmap, stringUtil.ImageQRRef(gameQRCode.getId(), player.getUserid()));

                    // Go to view QR code activity if successful
                    Intent QRInfo = new Intent(getApplicationContext(), QRInfoActivity.class);
                    QRInfo.putExtra("QRid", gameQRCode.getId());
                    startActivity(QRInfo);
                } catch (Exception e) {
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed to save", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Reverse Geocoding
     * (Latitude, Longitude) -> Address
     * Make API call to get the address string name from coordinates
     * Makes calls to https://nominatim.openstreetmap.org/reverse
     * HTTP request source: https://developer.android.com/reference/java/net/HttpURLConnection
     * Author: Android develoeprs
     * Source: https://stackoverflow.com/a/10501619
     *
     * @param location current user location
     * @return address
     */
    private void reverseGeocoding(GeoLocation location) {
        // Must run network call on a new thread, not on the main thread to avoid android.os.NetworkOnMainThreadException
        new Thread() {
            @Override
            public void run() {
                // New geolocation
                GeoLocation gpsGeoLocation = new GeoLocation(gameQRCode.getId());
                gpsGeoLocation.setCoords(location.getLongitude(), location.getLatitude());

                // Fetch address string from nominatim api
                HttpsURLConnection urlConnection = null;
                try {
                    URL url = new URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=" + location.getLatitude() + "&lon=" + location.getLongitude());
                    urlConnection = (HttpsURLConnection) url.openConnection();
                    int status = urlConnection.getResponseCode();

                    switch (status) {
                        case 200:
                        case 201:
                            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            br.close();
                            String result = sb.toString();
                            GeocodedLocation geocodedLocation = new Gson().fromJson(result, GeocodedLocation.class);
                            gpsGeoLocation.setGeocodedLocation(geocodedLocation);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                    // Set geolocation in the QR code even if HTTP request fails
                    gameQRCode.setGeoLocation(gpsGeoLocation);
                }
            }
        }.start();
    }
}