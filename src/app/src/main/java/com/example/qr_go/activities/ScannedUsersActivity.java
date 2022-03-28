package com.example.qr_go.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_go.R;
import com.example.qr_go.objects.GameQRCode;

import java.util.ArrayList;

public class ScannedUsersActivity extends AppCompatActivity {

    GameQRCode selectedQR;

    ListView userList;
    ArrayAdapter<String> userAdapter;
    ArrayList<String> userDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_users);

        userList = findViewById(R.id.userList);
        userDataList = new ArrayList<String>();

        // Get information from extras
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            selectedQR = null;
        } else {
            selectedQR = (GameQRCode) getIntent().getSerializableExtra("selectedQR");
            userDataList = selectedQR.getUserObjects();
        }

        userAdapter = new ArrayAdapter<>(this, R.layout.list_users_content, userDataList);
        userList.setAdapter(userAdapter);
    }

}
