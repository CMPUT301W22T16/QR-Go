package com.example.qr_go;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MainActivity
 */
public class MapsActivity extends BaseActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private static String currentUUID;
    private static String userPassword;
    public static FirebaseFirestore db;
    private SharedPreferences loggedUser;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Location userLocation;

    ArrayList<GeoLocation> geoLocationList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        findUserLocation();
        initialize(); // initialize app on launch
        initializeNavbar();


        geoLocationList = new ArrayList<GeoLocation>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Initialize app on launch
     * Connect to database
     * Check user login
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialize() {
        // Initialize FireStore database if it is already null
        if (db == null) db = FirebaseFirestore.getInstance();

        // Log in the user or create a new user
        if (loggedUser == null) {
            loggedUser = this.getSharedPreferences(User.CURRENT_USER, MODE_PRIVATE);
            currentUUID = loggedUser.getString(User.USER_ID, null);
            userPassword = loggedUser.getString(User.USER_PWD, null);
            if (currentUUID == null) {
                User newUser = new Player();
                currentUUID = newUser.getUserid();
                userPassword = newUser.getPassword();
                SharedPreferences.Editor ed = loggedUser.edit();
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

        mMap.setMyLocationEnabled(true);
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
                            if(geoLocationList.get(i).getScore() != null) {
                                LatLng newLoc = new LatLng(geoLocationList.get(i).getLatitude(), geoLocationList.get(i).getLongitude());
                                selectedMarker = mMap.addMarker(new MarkerOptions().position(newLoc)
                                        .title(tempId.substring(0,8))
                                        .snippet("Score: " + geoLocationList.get(i).getScore()));
                            } else {
                                LatLng newLoc = new LatLng(geoLocationList.get(i).getLatitude(), geoLocationList.get(i).getLongitude());
                                selectedMarker = mMap.addMarker(new MarkerOptions().position(newLoc)
                                        .title(tempId.substring(0,8))
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
        intent.putExtra("QRid", (String)marker.getTag());
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void findUserLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Acquire a reference to the system Location Manager
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        userLocation = new Location(location);
                        LatLng latLng = new LatLng(userLocation.getLatitude(),userLocation.getLongitude()); // whatever
                        float zoom = 16;
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

}