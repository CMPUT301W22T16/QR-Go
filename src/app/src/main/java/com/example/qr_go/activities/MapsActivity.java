package com.example.qr_go.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.qr_go.R;
import com.example.qr_go.objects.GeoLocation;
import com.example.qr_go.objects.Player;
import com.example.qr_go.objects.User;
import com.example.qr_go.utils.QRGoDBUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

/**
 * MainActivity
 */
public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private static String currentUUID; // userId singleton
    private static String userPassword; // password singleton
    public static FirebaseFirestore db; // database singleton
    public static FirebaseStorage storage;
    private static SharedPreferences sharedPrefs;
    protected LocationManager locationManager;
    protected Location userLocation;
    private final int LOCATION_REQUEST_CODE = 101;
    private ArrayList<GeoLocation> geoLocationList;
    private final float zoom = 16;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        findUserLocation();
        initializeData(); // initialize app on launch
        initializeNavbar();
        geoLocationList = new ArrayList<GeoLocation>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Initialize app singletons on launch
     * Connect to database
     * Check user login
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeData() {
        // Initialize FireStore database if it is already null
        if (db == null) db = FirebaseFirestore.getInstance();
        if (storage == null) storage = FirebaseStorage.getInstance();
        // Log in the user or create a new user
        if (sharedPrefs == null) {
            sharedPrefs = this.getSharedPreferences(User.CURRENT_USER, MODE_PRIVATE);
            currentUUID = sharedPrefs.getString(User.USER_ID, null);
            userPassword = sharedPrefs.getString(User.USER_PWD, null);
            if (currentUUID == null) {
                User newUser = new Player();
                currentUUID = newUser.getUserid();
                userPassword = newUser.getPassword();
                SharedPreferences.Editor ed = sharedPrefs.edit();
                ed.putString(User.USER_ID, currentUUID);
                ed.putString(User.USER_PWD, userPassword);
                ed.apply(); // apply changes
                // Save user to the firestore database
                db.collection("Players").document(newUser.getUserid()).set(newUser);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String qrId;
        mMap = googleMap;

        // set my location on the map only if permission is allowed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (userLocation != null) {
                // Move camera to current position
                LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude()); // whatever
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            }
        }

        mMap.setOnInfoWindowClickListener(this);
        //Code from https://javapapers.com/android/get-current-location-in-android/
        db.collection("GameQRCodes")
                .whereNotEqualTo("geoLocation", null)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Map tempMap = new HashMap<>();
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                GeoLocation tempGeoLocation = new GeoLocation((String) document.get("id"));
                                tempMap = (Map) document.get("geoLocation");
                                if (tempMap.containsKey("latitude")) {
                                    tempGeoLocation.setCoords((Double) tempMap.get("longitude"), (Double) tempMap.get("latitude"));
                                    tempGeoLocation.setScore((Long) document.get("score"));
                                    geoLocationList.add(tempGeoLocation);
                                }
                            }
                        }
                        Marker selectedMarker;
                        String tempId;
                        for (int i = 0; i < geoLocationList.size(); i++) {
                            tempId = geoLocationList
                                    .get(i)
                                    .getQRId();
                            if (geoLocationList.get(i).getScore() != null) {
                                LatLng newLoc = new LatLng(geoLocationList.get(i).getLatitude(), geoLocationList.get(i).getLongitude());
                                selectedMarker = mMap.addMarker(new MarkerOptions().position(newLoc)
                                        .title(tempId.substring(0, 8))
                                        .snippet("Score: " + geoLocationList.get(i).getScore()));
                            } else {
                                LatLng newLoc = new LatLng(geoLocationList.get(i).getLatitude(), geoLocationList.get(i).getLongitude());
                                selectedMarker = mMap.addMarker(new MarkerOptions().position(newLoc)
                                        .title(tempId.substring(0, 8))
                                        .snippet("Score: null"));
                            }
                            selectedMarker.setTag(tempId);
                        }
                    }
                });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, QRInfoActivity.class);
        String markerQrId = (String) marker.getTag();
        intent.putExtra("QRid", markerQrId);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void findUserLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

            else {
                // Acquire a reference to the system Location Manager
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        userLocation = new Location(location);
                        LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude()); // whatever
                        if (mMap != null)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                        locationManager.removeUpdates(this);
                    }
                };
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }

        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            Toast.makeText(this, "failed to get location", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * When user permission dialog returns from user
     * If permission allowed, try finding the user's location again
     * If not location, then need to set current location on maps to false
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission granted, set the location
                findUserLocation();
                mMap.setMyLocationEnabled(true);
            } else {
                // No permission, set maps my location to false
                mMap.setMyLocationEnabled(false);
            }
        }
    }

    /**
     * Get user id of currently logged in user
     */
    public static String getUserId() {
        return currentUUID;
    }

    /**
     * Get password of currently logged in user
     */
    public static String getPassword() {
        return userPassword;
    }

    /**
     * Set user id of currently logged in user
     */
    public static void setUserId(String userId) {
        currentUUID = userId;
    }

    /**
     * Set password of currently logged in user
     */
    public static void setPassword(String password) {
        userPassword = password;
    }
}