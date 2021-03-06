package com.example.qr_go.activities;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.example.qr_go.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BaseActivity extends FragmentActivity {

    protected void initializeNavbar() {
        // Set onClick for BottomNavigation nav items
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true); // make navbar uncheckable (un-highlightable)
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = new Intent(BaseActivity.this, MapsActivity.class);
                if (id == R.id.nav_search)
                    intent = new Intent(BaseActivity.this, SearchActivity.class);
                else if (id == R.id.nav_my_codes)
                    intent = new Intent(BaseActivity.this, MyQRCodesActivity.class);
                else if (id == R.id.nav_scan_code)
                    intent = new Intent(BaseActivity.this, QRCodeScannerActivity.class);
                else if (id == R.id.nav_my_account)
                    intent = new Intent(BaseActivity.this, PlayerProfileActivity.class);
                else if (id == R.id.nav_home)
                    intent = new Intent(BaseActivity.this, MapsActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // bring up the already opened activity
                startActivity(intent);


                return true;
            }
        });
    }


}
