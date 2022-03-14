package com.example.qr_go;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String currentUUID;
    public static FirebaseFirestore db;
    protected LocationManager locationManager;
    protected LocationListener locationListener;


    CollectionReference collectionReference;
    ArrayList<GeoLocation> geoLocationList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        geoLocationList = new ArrayList<GeoLocation>();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize FireStore database
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("TEST");
        HashMap<String, String> data = new HashMap<>();
        data.put("TEST KEY", "TEST VALUE");
        collectionReference.document("DOCUMENT").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SUCCESS", "Data has been added successfully!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FAILURE", "Data could not be added: " + e.toString());
            }
        });

        // Log in the user or create a new user
        SharedPreferences loggedUser = this.getSharedPreferences(User.CURRENT_USER, MODE_PRIVATE);
        currentUUID = loggedUser.getString(User.USER_ID, null);
        if (currentUUID == null) {
            User newUser = new Player();
            currentUUID = newUser.getUserid();
            SharedPreferences.Editor ed = loggedUser.edit();
            ed.putString(User.USER_ID, currentUUID);
            ed.apply(); // apply changes
            // Save user to the firestore database
            db.collection("Players").document(newUser.getUserid()).set(newUser);
        }

        // Set onClick for BottomNavigation nav items
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.getMenu().getItem(0).setCheckable(false); // don't select first item by default
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_search:
                        startActivity(new Intent(MapsActivity.this, SearchActivity.class));
                        break;
                    case R.id.nav_my_codes:
                        startActivity(new Intent(MapsActivity.this, MyQRCodesActivity.class));
                        break;
                    case R.id.nav_scan_code:
                        startActivity(new Intent(MapsActivity.this, QRCodeScannerActivity.class));
                        break;
                    case R.id.nav_my_account:
                        startActivity(new Intent(MapsActivity.this, PlayerProfileActivity.class));
                        break;
                }
                return true;
            }
        });
        QRGoDBUtil db = new QRGoDBUtil();
        db.test3();

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Code from https://javapapers.com/android/get-current-location-in-android/
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        db.collection("GameQRCodes")
                .whereNotEqualTo("location", null)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String, Double> tempMap = new HashMap<>();
                        if(task.isSuccessful()) {

                            for(QueryDocumentSnapshot document : task.getResult()) {
                                GeoLocation tempGeoLocation = new GeoLocation((String)document.get("id"));
                                tempMap = (HashMap<String,Double>)document.get("location");
                                if(tempMap.containsKey("latitude")) {
                                    tempGeoLocation.setCoords(tempMap.get("longitude"),tempMap.get("latitude"));
                                    geoLocationList.add(tempGeoLocation);
                                }

                            }
                        }
                        for(int i = 0; i< geoLocationList.size(); i++){
                            LatLng newLoc = new LatLng(geoLocationList.get(i).getLatitude(), geoLocationList.get(i).getLongitude());
                            mMap.addMarker(new MarkerOptions().position(newLoc).title("NewMarker"));
                        }
                    }


                });

    }

    /**
     * Get user id of currently logged in user
     */
    public static String getUserId(){
        return currentUUID;
    }
}