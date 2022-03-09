package com.example.qr_go;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;


public class SearchActivity extends FragmentActivity {
    // The user that is searching on the app
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchFragmentStateAdapter searchPagerAdapter = new SearchFragmentStateAdapter(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.search_tab_layout);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.search_pager);
        Spinner sortOptionSpinner = (Spinner) findViewById(R.id.sort_spinner);

        viewPager.setAdapter(searchPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "QR Codes" : "Players")).attach();

        // test sort dropdown
        String []sortOptionsList = {"Top Score", "Option 2", "Option 3"};
        ArrayList<String> sortOptionsDataList = new ArrayList<>();
        sortOptionsDataList.addAll(Arrays.asList(sortOptionsList));
        ArrayAdapter<String> sortOptionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, sortOptionsDataList);
        sortOptionSpinner.setAdapter(sortOptionAdapter);


    }
}
