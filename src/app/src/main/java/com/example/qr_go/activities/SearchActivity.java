package com.example.qr_go.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qr_go.containers.QRListDisplayContainer;
import com.example.qr_go.adapters.QRSearchSpinnerAdapter;
import com.example.qr_go.R;
import com.example.qr_go.adapters.SearchFragmentStateAdapter;
import com.example.qr_go.containers.UserListDisplayContainer;
import com.example.qr_go.objects.Player;
import com.example.qr_go.utils.StringUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents the search activity
 */
public class SearchActivity extends BaseActivity {
    private final int LOCATION_REQUEST_CODE = 101;
    private Location userLocation;
    private LocationManager locationManager;
    // String arrays holding the sorting options
    private String[] qrSortOptions = {"Score"};
    private final String[] playerSortOptions = {"Total Score", "# QR Codes", "Unique Score"};
    // Adapter for qr sort options as it may be updated with location
    private ArrayAdapter<String> qrSortOptionAdapter;
    // Fragment state adapter for tabs
    private SearchFragmentStateAdapter searchPagerAdapter;
    // Current fragment being displayed
    private Integer currentFragment = 0;
    // Current position that sort spinner is selected as
    private Integer sortPosition = 0;

    // Data to display in fragments
    private static ArrayList<UserListDisplayContainer> userDisplays;
    private static ArrayList<QRListDisplayContainer> qrDisplays;

    // The search bar
    private static EditText searchBar;

    // The qr search spinner
    private static Spinner qrSearchSpinner;
    // The qr search spinner's adapter
    private QRSearchSpinnerAdapter qrSearchSpinnerAdapter;
    // String array holding qr search options
    private String[] qrSearchOptions = {"Global"};

    // The context
    private Context context;

    // Firebase references
    private FirebaseFirestore db;
    private CollectionReference playersColRef;
    private CollectionReference qrColRef;

    // Variable which determines if the current user is an owner
    private static Boolean isUserOwner;
    // Variable which holds the userid of all owner users
    private static ArrayList<String> ownerIds;
    final long ONE_MEGABYTE = 4 * 1024 * 1024;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initializeNavbar();

        // Initialize database references
        db = FirebaseFirestore.getInstance();
        playersColRef = db.collection("Players");
        qrColRef = db.collection("GameQRCodes");

        // Initialize the ownerIds array
        ownerIds = new ArrayList<>();

        // Determine if the current user is an owner first
        playersColRef.whereEqualTo("owner", true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                isUserOwner = false;
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    String userid = snapshot.get("userid", String.class);
                    ownerIds.add(userid);
                    if (userid.equals(MapsActivity.getUserId())){
                        isUserOwner = true;
                    }
                }
                // Disable the location first so that there is at least a view that the user can see
                // If location is enabled, then the views are updated and the user is able to sort
                // using proximity when the location manager responds.
                disableLocation();
            }
        });

        searchBar = (EditText) findViewById(R.id.search_bar);
        qrSearchSpinner = (Spinner) findViewById(R.id.qr_search_spinner);
    }

    /**
     * If location is disabled, do not allow user to sort by proximity
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void disableLocation() {
        userLocation = null;
        startViews();
    }

    /**
     * Initialize the views
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void startViews() {

        userDisplays = new ArrayList<>();
        qrDisplays = new ArrayList<>();

        // Initialize sort options
        ArrayList<String> qrSortOptionsDataList = new ArrayList<>();
        qrSortOptionsDataList.addAll(Arrays.asList(qrSortOptions));
        ArrayList<String> playerSortOptionsDataList = new ArrayList<>();
        playerSortOptionsDataList.addAll(Arrays.asList(playerSortOptions));
        qrSortOptionAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, qrSortOptionsDataList);
        ArrayAdapter<String> playerSortOptionAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, playerSortOptionsDataList);
        ArrayList<String> qrSearchOptionsDataList = new ArrayList<>();
        qrSearchOptionsDataList.addAll(Arrays.asList(qrSearchOptions));
        qrSearchSpinnerAdapter = new QRSearchSpinnerAdapter(this, qrSearchOptionsDataList);

        Spinner sortOptionSpinner = (Spinner) findViewById(R.id.sort_spinner);
        searchPagerAdapter = new SearchFragmentStateAdapter(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.search_tab_layout);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.search_pager);

        qrSearchSpinner.setAdapter(qrSearchSpinnerAdapter);

        // Set the listener for the spinner so that the fragments update their data based on
        // the selected sort option
        sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                clearSearchBar();
                qrSearchSpinner.setSelection(0);
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
                        searchBar.setVisibility(View.GONE);
                        qrSearchSpinner.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        // When page is Players search
                        sortOptionSpinner.setAdapter(playerSortOptionAdapter);
                        qrSearchSpinner.setSelection(0);
                        qrSearchSpinner.setVisibility(View.GONE);
                        searchBar.setVisibility(View.VISIBLE);
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
        qrColRef.whereNotEqualTo("deleted", true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                qrDisplays.clear();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Map qrLocationMap = (Map) snapshot.get("geoLocation");
                    // Set distances as null for now. If the user's location is found, then this
                    // will be updated later
                    Double qrLat = null;
                    Double qrLon = null;
                    String neighborhood = null;
                    Map usermap = (Map) snapshot.get("userIds");
                    if (qrLocationMap != null) {
                        qrLat = (Double) qrLocationMap.get("latitude");
                        qrLon = (Double) qrLocationMap.get("longitude");
                        Map geocodedLocation = (Map) qrLocationMap.get("geocodedLocation");
                        if (geocodedLocation != null) {
                            Map address = (Map) geocodedLocation.get("address");
                            neighborhood = (String) address.get("neighbourhood");
                        }
                    }
                    String strUserMap;
                    if(usermap.size() <= 0) {
                        strUserMap = null;
                    } else {
                        strUserMap = (String) usermap.keySet().toArray()[0];
                    }
                    QRListDisplayContainer qrToDisplay =
                            new QRListDisplayContainer(
                                    snapshot.get("score", Integer.class),
                                    snapshot.get("id", String.class),
                                    qrLat,
                                    qrLon,
                                    null,
                                    strUserMap,
                                    neighborhood
                            );
                    qrDisplays.add(qrToDisplay);
                }
                // Update the fragments after getting documents is done
                searchPagerAdapter.updateSort(currentFragment, sortPosition);
                // Request location permissions
                // Code taken from NewGameQRActivity.java file
                try {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                    } else {
                        // If has permissions, set location by default
                        setUserLocation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addImages();
            }
        });
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
            }
        }
    }


    /**
     * Assuming that location permissions is granted
     * The current location of the user is received
     * The location is then set and a new sort option
     * is provided for the user
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
                        addLocation();
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
     * Adds images for qrs and users
     */
    private void addImages() {
        FirebaseStorage storage = MapsActivity.storage;
        StringUtil stringUtil = new StringUtil();
        StorageReference storageRef = storage.getReference();
        UserListDisplayContainer lastUser = userDisplays.get(userDisplays.size()-1);
        QRListDisplayContainer lastQR = qrDisplays.get(qrDisplays.size()-1);
        for (UserListDisplayContainer user : userDisplays) {
            String ImageRef = stringUtil.ImagePlayerRef(user.getUserid());
            StorageReference islandRef = storageRef.child(ImageRef);
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    user.setPicture(bitmap);
                    if (user.getUserid().equals(lastUser.getUserid())) {
                        searchPagerAdapter.updateView(currentFragment, sortPosition);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (user.getUserid().equals(lastUser.getUserid())) {
                        searchPagerAdapter.updateView(currentFragment, sortPosition);
                    }
                    return;
                }
            });
        }
        for (QRListDisplayContainer qr : qrDisplays) {
            String ImageRef = stringUtil.ImageQRRef(qr.getId(), qr.getFirstUserID());
            StorageReference islandRef = storageRef.child(ImageRef);
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    qr.setPicture(bitmap);
                    if (qr.getId().equals(lastQR.getId())) {
                        searchPagerAdapter.updateView(currentFragment, sortPosition);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (qr.getId().equals(lastQR.getId())) {
                        searchPagerAdapter.updateView(currentFragment, sortPosition);
                    }
                    return;
                }
            });
        }
    }

    /**
     * Add the option to sort by proximity and update the qrcode array
     */
    private void addLocation() {
        for(QRListDisplayContainer qr : qrDisplays) {
            if (qr.getLat() != null && qr.getLon() != null) {
                Location qrLocation = new Location("qr");
                qrLocation.setLatitude(qr.getLat());
                qrLocation.setLongitude(qr.getLon());
                qr.setDistance(qrLocation.distanceTo(userLocation));
            }
        }
        // Add proximity as a search option;
        qrSortOptionAdapter.add("Proximity");
        qrSearchSpinnerAdapter.add("Regional");
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
     * @return the qr search spinner
     */
    public static Spinner getQrSearchSpinner() {
        return qrSearchSpinner;
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
     * @return If the current user is an owner or not. Used for lists to determine owner view
     */
    public static Boolean getUserOwner() {
        return isUserOwner;
    }

    /**
     * @return All userids that are owners. Used for determining if they are deletable
     */
    public static ArrayList<String> getOwnerIds() {
        return ownerIds;
    }

    /**
     * Deletes the provided user from the activity's list and then forces the fragment to get the
     * updated list
     * @param user the user to delete
     */
    public void deleteUser(UserListDisplayContainer user) {
        userDisplays.remove(user);
        searchPagerAdapter.retrieveData(1);
    }

    /**
     * Deletes the provided qr from the activity's list and then forces the fragment to get the
     * updated list
     * @param qr the qr to delete
     */
    public void deleteQR(QRListDisplayContainer qr) {
        qrDisplays.remove(qr);
        searchPagerAdapter.retrieveData(0);
    }
}