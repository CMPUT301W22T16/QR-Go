package com.example.qr_go;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MyQRCodesActivity extends AppCompatActivity {

    private Button tempButton;
    FirebaseFirestore playerDBInst;
    private Player selectedPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_codes);

        String selectedPlayerID = MapsActivity.getUserId();


        TextView playernameText = findViewById(R.id.playerNameText);
        TextView qrNumText = findViewById(R.id.numOfQRCodes);
        TextView totalScoreText = findViewById(R.id.playerTotalScore);
        TextView highestScoreText = findViewById(R.id.playerHighScore);
        TextView lowestScoreText = findViewById(R.id.playerLowScore);

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
                            lowestScoreText.setText(Integer.toString(selectedPlayer.getLowestUniqueScore()));
                            qrNumText.setText(Integer.toString(selectedPlayer.getScannedQRCodeIds().size()));
                            totalScoreText.setText(Integer.toString(selectedPlayer.getTotalScore()));
                        }
                    }
                });

//        TODO: replace this and add OnClickListener for a QRCode list
        tempButton = (Button) findViewById(R.id.bt_temp_qr);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyQRCodesActivity.this, QRInfoActivity.class);
//                QRCode selectedGame = gameDataList.get(position);
//                intent.putExtra("selectedGameID", selectedGame.getID());
                startActivity(intent);
            }
        });

    }
}
