package com.example.qr_go;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // Set the listener for the spinner so that the fragments update their data based on the
        // selected sort option
        sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                searchPagerAdapter.updateSort(currentFragment, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

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




    }
}
