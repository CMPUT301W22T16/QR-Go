package com.example.qr_go;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class SearchActivity extends FragmentActivity {
    // The user that is searching on the app
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchFragmentStateAdapter searchPagerAdapter = new SearchFragmentStateAdapter(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.searchTabLayout);
        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.searchPager);

        viewPager.setAdapter(searchPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "QR Codes" : "Players")).attach();

    }
}
