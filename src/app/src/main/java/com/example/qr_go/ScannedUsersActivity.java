package com.example.qr_go;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScannedUsersActivity extends AppCompatActivity {

    FirebaseFirestore gameQRDBInst;

    GameQRCode selectedQR;

    ListView userList;
    ArrayAdapter<String> userAdapter;
    ArrayList<String> userDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_users);

        gameQRDBInst = FirebaseFirestore.getInstance();

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
