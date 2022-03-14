package com.example.qr_go;

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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Represents the search activity
 */
public class SearchActivity extends FragmentActivity {
    private final int LOCATION_REQUEST_CODE = 101;
    private Location userLocation;
    LocationManager locationManager;
    // String arrays holding the sorting options
    private String[] qrSortOptions = {"Score", "Proximity"};
    private final String[] playerSortOptions = {"Total Score", "# QR Codes", "Unique Score"};
    // Current fragment being displayed
    private Integer currentFragment = 0;
    // Current position that sort spinner is selected as
    private Integer sortPosition = 0;

    // Data to display in fragments
    private static ArrayList<UserListDisplayContainer> userDisplays;
    private static ArrayList<QRListDisplayContainer> qrDisplays;

    // The search bar
    private static EditText searchBar;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Request location permissions
        // Code taken from NewGameQRActivity.java file
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                // If has permissions, set location by default
                setUserLocation();
//                disableLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button backButton = (Button) findViewById(R.id.back_button);
        searchBar = (EditText) findViewById(R.id.search_bar);

        // Back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @return User data to display on fragments
     */
    public static ArrayList<UserListDisplayContainer> getUserDisplays() {
        return userDisplays;
    }

    /**
     * @return QR data to display on fragments
     */
    public static ArrayList<QRListDisplayContainer> getQrDisplays() {
        return qrDisplays;
    }

    /**
     * Returns the search bar for use in the fragments
     * @return The search bar EditText
     */
    public static EditText getSearchBar() {
        return searchBar;
    }

    /**
     * Utility function for clearing search bar and minimizing keyboard
     * https://stackoverflow.com/a/2342856
     */
    private void clearSearchBar() {
        searchBar.getText().clear();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }

    /**
     * Once user has accepted/denied permission access
     * If permission granted, set the location of the QR code
     * If permission denied, disable the location
     *
     * Code from NewGameQRActivity.java
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
                setUserLocation();
            } else {
                disableLocation();
            }
        }
    }

    /**
     * If location is disabled, do not allow user to sort by proximity
     */
    private void disableLocation() {
        qrSortOptions = new String[]{"Score"};
        userLocation = null;
        startViews();
    }

    /**
     * Assuming that location permissions is granted
     * The current location of the user is received
     * The location is then set in the game qr code
     *
     * Code from NewGameQRActivity.java
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setUserLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Acquire a reference to the system Location Manager
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        userLocation = new Location(location);
                        startViews();
                        locationManager.removeUpdates(this);
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

    @SuppressLint("MissingPermission")
    private void startViews() {
        // Initialize database references
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference playersColRef = db.collection("Players");
        CollectionReference ownersColRef = db.collection("Owners");
        CollectionReference qrColRef = db.collection("GameQRCodes");

        userDisplays = new ArrayList<>();
        qrDisplays = new ArrayList<>();

        // Initialize sort options
        ArrayList<String> qrSortOptionsDataList = new ArrayList<>();
        qrSortOptionsDataList.addAll(Arrays.asList(qrSortOptions));
        ArrayList<String> playerSortOptionsDataList = new ArrayList<>();
        playerSortOptionsDataList.addAll(Arrays.asList(playerSortOptions));
        ArrayAdapter<String> qrSortOptionAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, qrSortOptionsDataList);
        ArrayAdapter<String> playerSortOptionAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, playerSortOptionsDataList);

        Spinner sortOptionSpinner = (Spinner) findViewById(R.id.sort_spinner);
        SearchFragmentStateAdapter searchPagerAdapter =
                new SearchFragmentStateAdapter(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.search_tab_layout);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.search_pager);

        // Set the listener for the spinner so that the fragments update their data based on
        // the selected sort option
        sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                clearSearchBar();
                searchPagerAdapter.retrieveData(currentFragment);
                searchPagerAdapter.updateSort(currentFragment, position);
                sortPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        // Initialize adapter for the ViewPager
        viewPager.setAdapter(searchPagerAdapter);
        // Set the layout mediator
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "QR Codes" : "Players")).attach();

        // Set an on page callback such that the sort spinner updated depending on which fragment
        // is being shown
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                clearSearchBar();
                switch(position) {
                    case 0:
                        // When page is QR code search
                        sortOptionSpinner.setAdapter(qrSortOptionAdapter);
                        searchBar.setHint("Search QR Location");
                        break;
                    case 1:
                        // When page is Players search
                        sortOptionSpinner.setAdapter(playerSortOptionAdapter);
                        searchBar.setHint("Search Username");
                        break;
                    default:
                        break;
                }
                searchPagerAdapter.setSearch(position);
                currentFragment = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // Get users and QRs from the database
        playersColRef.whereNotEqualTo("deleted", true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userDisplays.clear();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Player user = snapshot.toObject(Player.class);
                    UserListDisplayContainer userToDisplay =
                            new UserListDisplayContainer(
                                    user.getUserid(),
                                    user.getUsername(),
                                    user.getTotalScore(),
                                    user.getScannedQRCodeIds().size(),
                                    user.getHighestUniqueScore(),
                                    MapsActivity.getUserId().equals(user.getUserid())
                            );
                    userDisplays.add(userToDisplay);
                }
            }
        });
        ownersColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Owner user = snapshot.toObject(Owner.class);
                    UserListDisplayContainer userToDisplay =
                            new UserListDisplayContainer(
                                    user.getUserid(),
                                    user.getUsername(),
                                    user.getTotalScore(),
                                    user.getScannedQRCodeIds().size(),
                                    user.getHighestUniqueScore(),
                                    MapsActivity.getUserId().equals(user.getUserid())
                            );
                    userDisplays.add(userToDisplay);
                }
            }
        });
        qrColRef.whereNotEqualTo("deleted", true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                qrDisplays.clear();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Float distance;
                    Map qrLocationMap = (Map) snapshot.get("location");
                    // If no recorded location, set distance as null, else calculate the distance
                    if (qrLocationMap == null || userLocation == null) {
                        distance = null;
                    } else {
                        Double qrLat = (Double) qrLocationMap.get("latitude");
                        Double qrLon = (Double) qrLocationMap.get("longitude");
                        Location qrLocation = new Location("qr");
                        qrLocation.setLatitude(qrLat);
                        qrLocation.setLongitude(qrLon);
                        distance = qrLocation.distanceTo(userLocation);
                    }
                    QRListDisplayContainer qrToDisplay =
                            new QRListDisplayContainer(
                                    snapshot.get("score", Integer.class),
                                    snapshot.get("id", String.class),
                                    distance
                            );
                    qrDisplays.add(qrToDisplay);
                }
                // Update the fragments after getting documents is done
                searchPagerAdapter.updateSort(currentFragment, sortPosition);

            }
        });

    }

}
