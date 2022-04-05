package com.example.qr_go.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_go.R;
import com.example.qr_go.containers.ListScannedUsersContainer;
import com.example.qr_go.objects.GameQRCode;
import com.example.qr_go.objects.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScannedUsersActivity extends AppCompatActivity {

    private GameQRCode selectedQR;

    HashMap<String, HashMap<String, String>> playersInfo;

    ListView userList;
    ListScannedUsersContainer userAdapter;
    ArrayList<Pair> userDataList;
    TextView emptyText;
//    ArrayList<String> userIds;

    final long ONE_MEGABYTE = 4 * 1024 * 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_users);
        emptyText = (TextView)findViewById(R.id.empty_list);
        userList = findViewById(R.id.userList);
        //userList.setEmptyView(emptyText);
        userDataList = new ArrayList<Pair>();
//        userIds = new ArrayList<String>();

        // Get information from extras
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            selectedQR = null;
        } else {
            selectedQR = (GameQRCode) getIntent().getSerializableExtra("selectedQR");
            playersInfo = selectedQR.getUserIds();
        }

        for (Map.Entry<String, HashMap<String, String>> details:playersInfo.entrySet() ){
//            userIds.add(details.getKey());
            HashMap<String, String> temp = details.getValue();
            userDataList.add(new Pair(temp.get("PhotoRef"), temp.get("Username")));
        }

        userAdapter = new ListScannedUsersContainer(this, userDataList);
        userList.setAdapter(userAdapter);

        // When item on the list is pressed
//        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(view.getContext(), PlayerInfoActivity.class);
//                intent.putExtra("SELECTED_USER", userIds.get(position));
//                view.getContext().startActivity(intent);
//            }
//        });
    }

}
