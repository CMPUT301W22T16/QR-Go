package com.example.qr_go;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BaseActivity extends FragmentActivity {

    protected void initializeNavbar() {
        // Set onClick for BottomNavigation nav items
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        System.out.println("IN BASE ACTIVITYY");
        System.out.println("bottomNavigationView" + bottomNavigationView);

        bottomNavigationView.getMenu().getItem(0).setCheckable(false); // don't select first item by default
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_search:
                        startActivity(new Intent(BaseActivity.this, SearchActivity.class));
                        break;
                    case R.id.nav_my_codes:
                        startActivity(new Intent(BaseActivity.this, MyQRCodesActivity.class));
                        break;
                    case R.id.nav_scan_code:
                        startActivity(new Intent(BaseActivity.this, QRCodeScannerActivity.class));
                        break;
                    case R.id.nav_my_account:
                        startActivity(new Intent(BaseActivity.this, PlayerProfileActivity.class));
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(BaseActivity.this, MapsActivity.class));
                        break;
                }
                return true;
            }
        });
    }


}
