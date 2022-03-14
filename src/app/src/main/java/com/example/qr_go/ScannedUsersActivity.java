package com.example.qr_go;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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

        // TODO: this is temp
        String []usernames = {"User1", "User2", "User3", "User4", "User5", "User6"};
        selectedQR = new GameQRCode();
        for(int i=0; i<usernames.length; i++) {
            Player player = new Player();
            player.setUsername(usernames[i]);
            selectedQR.addUser(player);
        }
        userDataList = selectedQR.getUserObjects();

        userAdapter = new ArrayAdapter<>(this, R.layout.list_users_content, userDataList);
        userList.setAdapter(userAdapter);
    }

}
