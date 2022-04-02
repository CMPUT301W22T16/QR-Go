package com.example.qr_go.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.qr_go.R;
import com.example.qr_go.adapters.SearchQRArrayAdapter;
import com.example.qr_go.adapters.UserQRArrayAdapter;
import com.example.qr_go.containers.QRListDisplayContainer;
import com.example.qr_go.objects.Player;
import com.example.qr_go.utils.StringUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MyQRCodesActivity extends BaseActivity {

    private Button tempButton;
    FirebaseFirestore playerDBInst;
    private Player selectedPlayer;
    Map.Entry<String, Integer> highestQRCode;
    Map.Entry<String, Integer> lowestQRCode;
    private UserQRArrayAdapter qrAdapter;
    private ArrayList<QRListDisplayContainer> qrDisplays;
    private ArrayList<String> qrCodeList;
    private ListView userQRList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_codes);
        initializeNavbar();
        userQRList = findViewById(R.id.user_qr_list);
        qrDisplays = new ArrayList<QRListDisplayContainer>();
        String selectedPlayerID = MapsActivity.getUserId();


        TextView playernameText = findViewById(R.id.playerNameText);
        TextView qrNumText = findViewById(R.id.numOfQRCodes);
        TextView totalScoreText = findViewById(R.id.playerTotalScore);
        TextView highestScoreText = findViewById(R.id.playerHighScore);
        TextView lowestScoreText = findViewById(R.id.playerLowScore);

        View highestScoreLayout = findViewById(R.id.highest_score);
        View lowestScoreLayout = findViewById(R.id.lowest_score);
        TextView highestScoreQrIDText = highestScoreLayout.findViewById(R.id.qr_id_view);
        TextView highestScoreQrScoreText = highestScoreLayout.findViewById(R.id.qr_score_view);
        TextView lowestScoreQrIDText = lowestScoreLayout.findViewById(R.id.qr_id_view);
        TextView lowestScoreQrScoreText = lowestScoreLayout.findViewById(R.id.qr_score_view);

        playerDBInst = FirebaseFirestore.getInstance();

        playerDBInst.collection("Players")
                .whereEqualTo("userid", selectedPlayerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
                                selectedPlayer = document.toObject(Player.class);
                            }
                            Set<String> idSet = selectedPlayer.getScannedQRCodeIds().keySet();
                            qrCodeList = new ArrayList<String>(idSet);
                            for(int i = 0; i < qrCodeList.size(); i++) {
                                QRListDisplayContainer qrToDisplay =
                                        new QRListDisplayContainer(
                                                selectedPlayer.getScannedQRCodeIds().get(qrCodeList.get(i)),
                                                qrCodeList.get(i),
                                                null,
                                                null,
                                                null,
                                                null
                                        );
                                qrDisplays.add(qrToDisplay);
                            }
                            playernameText.setText(selectedPlayer.getUsername());
                            highestScoreText.setText(Integer.toString(selectedPlayer.getHighestUniqueScore()));
                            lowestScoreText.setText(Integer.toString(selectedPlayer.getLowestUniqueScore()));
                            qrNumText.setText(Integer.toString(selectedPlayer.getScannedQRCodeIds().size()));
                            totalScoreText.setText(Integer.toString(selectedPlayer.getTotalScore()));
                            highestQRCode = selectedPlayer.getHighestQRCode();
                            highestScoreQrIDText.setText(highestQRCode.getKey().substring(0, 8));
                            highestScoreQrScoreText.setText("Score: " + highestQRCode.getValue().toString());
                            lowestQRCode = selectedPlayer.getLowestQRCode();
                            lowestScoreQrIDText.setText(lowestQRCode.getKey().substring(0, 8));
                            lowestScoreQrScoreText.setText("Score: " + lowestQRCode.getValue().toString());

                            // dont have a listener if no QR codes are available
                            if (highestQRCode.getValue() != 0) {
                                highestScoreLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(view.getContext(), QRInfoActivity.class);
                                        intent.putExtra("QRid", highestQRCode.getKey());
                                        view.getContext().startActivity(intent);
                                    }
                                });
                            }
                            // dont have a listener if no QR codes are available
                            if (lowestQRCode.getValue() != 0){
                                lowestScoreLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(view.getContext(), QRInfoActivity.class);
                                        intent.putExtra("QRid", lowestQRCode.getKey());
                                        view.getContext().startActivity(intent);
                                    }
                                });
                            }
                            qrAdapter = new UserQRArrayAdapter(MyQRCodesActivity.this, qrDisplays, 0);
                            userQRList.setAdapter(qrAdapter);
                        }
                        ImageView profileImage = findViewById(R.id.profile_photo);
                        FirebaseStorage storage = MapsActivity.storage;
                        StringUtil stringUtil = new StringUtil();
                        StorageReference storageRef = storage.getReference();
                        String ImageRef = stringUtil.ImagePlayerRef(selectedPlayer.getUserid());
                        StorageReference islandRef = storageRef.child(ImageRef);
                        final long ONE_MEGABYTE = 5 * 1024 * 1024;
                        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profileImage.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                return;
                            }
                        });
                        addImages();
                    }
                });
                userQRList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(view.getContext(), QRInfoActivity.class);
                        intent.putExtra("QRid", qrDisplays.get(position).getId());
                        view.getContext().startActivity(intent);
                    }
                });

    }
    private void addImages() {
        FirebaseStorage storage = MapsActivity.storage;
        StringUtil stringUtil = new StringUtil();
        StorageReference storageRef = storage.getReference();
        for (QRListDisplayContainer qr : qrDisplays) {
            String ImageRef = stringUtil.ImageQRRef(qr.getId(), selectedPlayer.getUserid());
            StorageReference islandRef = storageRef.child(ImageRef);
            final long ONE_MEGABYTE = 5 * 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    qr.setPicture(bitmap);
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
    public void deleteQR(QRListDisplayContainer qr) {
        qrDisplays.remove(qr);
    }
}