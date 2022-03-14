package com.example.qr_go;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class PlayerInfoActivity extends AppCompatActivity {

    private Player selectedPlayer;
    private String selectedPlayerID;
    private HashMap<Integer, String> myQRCodeIDs;
    private ArrayList<GameQRCode> myQRCodes;
    private Integer playerScore;
    final String TAG = "Sample";
    Button addCityButton;

    EditText addCityEditText;
    EditText addProvinceEditText;
    FirebaseFirestore playerDBInst;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        Intent intent = getIntent();
        String selectedPlayerID = intent.getStringExtra("SELECTED_USER");


        TextView playernameText = findViewById(R.id.playerNameText);
        TextView qrNumText = findViewById(R.id.numOfQRCodes);
        TextView totalScoreText = findViewById(R.id.playerTotalScore);
        TextView highestScoreText = findViewById(R.id.playerHighScore);

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
                                //selectedPlayer.setUsername((String) document.getData().get("playername"));
                                //playerScore = selectedPlayer.getTotalScore();
                                //myQRCodeIDs = (HashMap<Integer,String>) document.getData().get("scannedQRCodeIds");
                            }
                            playernameText.setText(selectedPlayer.getUsername());
                            highestScoreText.setText(Integer.toString(selectedPlayer.getHighestUniqueScore()));
                            qrNumText.setText(Integer.toString(selectedPlayer.getScannedQRCodeIds().size()));
                            totalScoreText.setText(Integer.toString(selectedPlayer.getTotalScore()));
                        }
                    }
                });



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




    public Integer getTotalScore(){
        return playerScore;
    }
    public Integer getQRCount() {
        return myQRCodeIDs.size();
    }
    public Integer getHighestQRScore() {

        return 0;
    }
    public void showScannedQRList() {
        return;
    }


}
