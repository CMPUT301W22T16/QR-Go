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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerInfoActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location userLocation;
    private Player selectedPlayer;
    private HashMap<String,Integer> playerQRCodes;
    private QRArrayAdapter qrAdapter;
    private ArrayList<QRListDisplayContainer> qrDisplays;
    private ArrayAdapter<String> qrCodeList;
    private QRListDisplayContainer qrCont;
    FirebaseFirestore playerDBInst;
    View view;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);

        Intent intent = getIntent();
        String selectedPlayerID = intent.getStringExtra("SELECTED_USER");
        playerQRCodes = new HashMap<String, Integer>();

        TextView playernameText = findViewById(R.id.playerNameText);
        TextView qrNumText = findViewById(R.id.numOfQRCodes);
        TextView totalScoreText = findViewById(R.id.playerTotalScore);
        TextView highestScoreText = findViewById(R.id.playerHighScore);
        TextView lowestScoreText = findViewById(R.id.playerLowScore);
        TextView playerEmailText = findViewById(R.id.playerEmail);

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
                                //playerQRCodes = document.
                            }
                            playernameText.setText(selectedPlayer.getUsername());
                            highestScoreText.setText(Integer.toString(selectedPlayer.getHighestUniqueScore()));
                            lowestScoreText.setText(Integer.toString(selectedPlayer.getLowestUniqueScore()));
                            qrNumText.setText(Integer.toString(selectedPlayer.getScannedQRCodeIds().size()));
                            totalScoreText.setText(Integer.toString(selectedPlayer.getTotalScore()));
                            playerEmailText.setText(selectedPlayer.getEmail());


                        }
                    }
                });

        // borrowed from marco but it doesn't work and I'm currently crying because he did a really good job at putting it together and i have no idea how to
        // integrate it ://///
        playerQRCodes = selectedPlayer.getScannedQRCodeIds();
        for(int i = 0; i < playerQRCodes.size(); i++) {
            playerDBInst.collection("QRCodes").
                    whereNotEqualTo("deleted", true).
                    whereEqualTo("id", playerQRCodes.get(i)).
                    get().
                    addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            qrDisplays.clear();
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Float distance;
                                Map qrLocationMap = (Map) snapshot.get("geoLocation");
                                // If no recorded location, set distance as null, else calculate the distance
                                if (qrLocationMap == null || userLocation == null) {
                                    distance = null;
                                } else {
                                    Double qrLat = (Double) qrLocationMap.get("latitude");
                                    Double qrLon = (Double) qrLocationMap.get("longitude");
                                    Location qrLocation = new Location("qr");
                                    qrLocation.setLatitude(qrLat);
                                    qrLocation.setLongitude(qrLon);
                                    distance = qrLocation.distanceTo(userLocation);
                                }
                                QRListDisplayContainer qrToDisplay =
                                        new QRListDisplayContainer(
                                                snapshot.get("score", Integer.class),
                                                snapshot.get("id", String.class),
                                                distance
                                        );
                                qrDisplays.add(qrToDisplay);
                            }
                            // Update the fragments after getting documents is done
                            searchPagerAdapter.updateSort(currentFragment, sortPosition);

                        }
                    });

        }

        qrCont = new QRListDisplayContainer();
        qrAdapter = new QRArrayAdapter(view.getContext(), qrDisplays, sortPos);
        qrListView.setAdapter(qrAdapter);



//        for(int i = 0; i < myQRCodeIDs.size(); i++) {
//            //Create loop to grab and instantiate all QR codes scanned by a player
//            playerDBInst.collection("GameQRCodes")
//                    .whereEqualTo(myQRCodeIDs.get(i), selectedPlayerID)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @RequiresApi(api = Build.VERSION_CODES.O)
//                        @Override
//                        // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    GameQRCode tempQR = new GameQRCode((String)document.getData().get("hash"));
//                                    myQRCodes.add(tempQR);
//                                    //get highest scoring QR
//                                }
//                            }
//                        }
//                    });
//        }







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
