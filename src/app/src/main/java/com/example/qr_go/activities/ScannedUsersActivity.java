package com.example.qr_go.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_go.R;
import com.example.qr_go.adapters.ListScannedUsersAdapter;
import com.example.qr_go.containers.CommentDisplayContainer;
import com.example.qr_go.containers.ScannedUserListDisplayContainer;
import com.example.qr_go.objects.GameQRCode;
import com.example.qr_go.utils.StringUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScannedUsersActivity extends AppCompatActivity {

    private GameQRCode selectedQR;

    private HashMap<String, HashMap<String, String>> playersInfo;

    ListView userList;
    private ArrayAdapter<ScannedUserListDisplayContainer> userAdapter;
    private ArrayList<ScannedUserListDisplayContainer> userDataList;
//    ArrayList<String> userIds;

    final long ONE_MEGABYTE = 4 * 1024 * 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_users);

        userList = findViewById(R.id.userList);
        userDataList = new ArrayList<>();
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
            HashMap<String, String> temp = details.getValue();
            userDataList.add(new ScannedUserListDisplayContainer(details.getKey(), temp.get("Username")));
        }

        userAdapter = new ListScannedUsersAdapter(this, userDataList);
        userList.setAdapter(userAdapter);
        addImages();


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
    /**
     * Adds images for qrs and users
     */
    private void addImages() {
        FirebaseStorage storage = MapsActivity.storage;
        StringUtil stringUtil = new StringUtil();
        StorageReference storageRef = storage.getReference();
        for (ScannedUserListDisplayContainer player : userDataList) {
            String ImageRef = stringUtil.ImagePlayerRef(player.getUserid());
            StorageReference islandRef = storageRef.child(ImageRef);
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    player.setPicture(bitmap);
                    userAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    return;
                }
            });
        }


    }

}
