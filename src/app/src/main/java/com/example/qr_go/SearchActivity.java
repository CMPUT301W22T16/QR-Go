package com.example.qr_go;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private Integer sortPosition = 0;
    private static ArrayList<UserListDisplayContainer> userDisplays;
    private static ArrayList<QRListDisplayContainer> qrDisplays;

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
        SearchFragmentStateAdapter searchPagerAdapter =
                new SearchFragmentStateAdapter(this);

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
                switch(position) {
                    case 0:
                        // When page is QR code search
                        sortOptionSpinner.setAdapter(qrSortOptionAdapter);
                        break;
                    case 1:
                        // When page is Players search
                        sortOptionSpinner.setAdapter(playerSortOptionAdapter);
                        break;
                    default:
                        break;
                }
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
                    // Update the fragments after getting documents is done
                    searchPagerAdapter.updateSort(currentFragment, sortPosition);

                    // Set the listener for the spinner so that the fragments update their data based on the
                    // selected sort option
                    // Done in the listener so that the fragment isn't updated when no data has
                    // been received yet
                    sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            searchPagerAdapter.updateSort(currentFragment, position);
                            sortPosition = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            return;
                        }

                    });
                }
            }
        });

    }

    public static ArrayList<UserListDisplayContainer> getUserDisplays() {
        return userDisplays;
    }

    public static ArrayList<QRListDisplayContainer> getQrDisplays() {
        return qrDisplays;
    }
}
