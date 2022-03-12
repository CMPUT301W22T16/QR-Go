package com.example.qr_go;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the search activity
 */
public class SearchActivity extends FragmentActivity {
    // String arrays holding the sorting options
    private static final String[] qrSortOptions = {"Score", "Proximity"};
    private static final String[] playerSortOptions = {"Total Score", "# QR Codes", "Unique Score"};
    // Current fragment being displayed
    private Integer currentFragment = 0;
    // Current position that sort spinner is selected as
    private Integer sortPosition = 0;

    // Data to display in fragments
    private static ArrayList<UserListDisplayContainer> userDisplays;
    private static ArrayList<QRListDisplayContainer> qrDisplays;

    // The search bar
    private static EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference playersColRef = db.collection("Players");
        CollectionReference ownersColRef = db.collection("Owners");
        CollectionReference qrColRef = db.collection("GameQRCodes");

        userDisplays = new ArrayList<>();
        qrDisplays = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.search_tab_layout);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.search_pager);
        Spinner sortOptionSpinner = (Spinner) findViewById(R.id.sort_spinner);
        Button backButton = (Button) findViewById(R.id.back_button);
        SearchFragmentStateAdapter searchPagerAdapter =
                new SearchFragmentStateAdapter(this);
        searchBar = (EditText) findViewById(R.id.search_bar);

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

        // Back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        // Initialize sort options
        ArrayList<String> qrSortOptionsDataList = new ArrayList<>();
        qrSortOptionsDataList.addAll(Arrays.asList(qrSortOptions));
        ArrayList<String> playerSortOptionsDataList = new ArrayList<>();
        playerSortOptionsDataList.addAll(Arrays.asList(playerSortOptions));
        ArrayAdapter<String> qrSortOptionAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, qrSortOptionsDataList);
        ArrayAdapter<String> playerSortOptionAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, playerSortOptionsDataList);

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
        playersColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userDisplays.clear();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    // TODO: Update how to get total score, highest unique score, and number of qr codes scanned with updated User class
                    ArrayList<String> qrCodes = (ArrayList<String>) snapshot.get("scannedQRCodeIds");
                    UserListDisplayContainer userToDisplay =
                            new UserListDisplayContainer(
                                    snapshot.get("userid", String.class),
                                    snapshot.get("username", String.class),
                                    snapshot.get("totalScore", Integer.class),
                                    qrCodes.size()
                            );
                    userDisplays.add(userToDisplay);
                }
            }
        });
        ownersColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    ArrayList<String> qrCodes = (ArrayList<String>) snapshot.get("scannedQRCodeIds");
                    UserListDisplayContainer userToDisplay =
                            new UserListDisplayContainer(
                                    snapshot.get("userid", String.class),
                                    snapshot.get("username", String.class),
                                    snapshot.get("totalScore", Integer.class),
                                    qrCodes.size()
                            );
                    userDisplays.add(userToDisplay);
                }
            }
        });
        qrColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                qrDisplays.clear();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    QRListDisplayContainer qrToDisplay =
                            new QRListDisplayContainer(
                                    snapshot.get("score", Integer.class),
                                    snapshot.get("id", String.class)
                            );
                    qrDisplays.add(qrToDisplay);
                }
                // Update the fragments after getting documents is done
                searchPagerAdapter.updateSort(currentFragment, sortPosition);

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
     */
    private void clearSearchBar() {
        searchBar.getText().clear();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }
}
