package com.example.qr_go;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class PlayerInfoActivity extends BaseActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location userLocation;
    private Player selectedPlayer;
    private HashMap<String,Integer> playerQRCodes;
    private QRArrayAdapter qrAdapter;
    private ArrayList<QRListDisplayContainer> qrDisplays;
    private ArrayList<String> qrCodeList;
    private QRListDisplayContainer qrCont;
    private ListView userQRList;
    private ArrayList<String> testIDLIST;
    CollectionReference playerDBInst;
    CollectionReference qrDBInst;
    View view;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        initializeNavbar();
        Intent intent = getIntent();
        String selectedPlayerID = intent.getStringExtra("SELECTED_USER");
        playerQRCodes = new HashMap<String, Integer>();
        testIDLIST = new ArrayList<String>();
        userQRList = findViewById(R.id.user_qr_list);
        qrDisplays = new ArrayList<QRListDisplayContainer>();
        TextView playernameText = findViewById(R.id.playerNameText);
        TextView qrNumText = findViewById(R.id.numOfQRCodes);
        TextView totalScoreText = findViewById(R.id.playerTotalScore);
        TextView highestScoreText = findViewById(R.id.playerHighScore);
        TextView lowestScoreText = findViewById(R.id.playerLowScore);
        TextView playerEmailText = findViewById(R.id.playerEmail);

        playerDBInst = FirebaseFirestore.getInstance().collection("Players");


        playerDBInst
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
                                                null
                                        );
                                qrDisplays.add(qrToDisplay);
                            }
                            playernameText.setText(selectedPlayer.getUsername());
                            highestScoreText.setText(Integer.toString(selectedPlayer.getHighestUniqueScore()));
                            lowestScoreText.setText(Integer.toString(selectedPlayer.getLowestUniqueScore()));
                            qrNumText.setText(Integer.toString(selectedPlayer.getScannedQRCodeIds().size()));
                            totalScoreText.setText(Integer.toString(selectedPlayer.getTotalScore()));
                            playerEmailText.setText(selectedPlayer.getEmail());




                            qrAdapter = new QRArrayAdapter(PlayerInfoActivity.this, qrDisplays, 0);
                            userQRList.setAdapter(qrAdapter);



                        }
                    }
                });

        }




    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setUserLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Acquire a reference to the system Location Manager
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                // Define a listener that responds to location updates{
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }

        } catch (NoSuchMethodError e) {
            e.printStackTrace();
            Toast.makeText(this, "failed to get location", Toast.LENGTH_LONG).show();
            // disable location
        }
    }



    public Integer getQRCount() {
        return playerQRCodes.size();
    }
    public Integer getHighestQRScore() {
        return 0;
    }
    public void showScannedQRList() {
        return;
    }


}
