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

public class PlayerInfoActivity extends AppCompatActivity {
    private User selectedUser;
    private String selectedUserID;
    private ArrayList<String> myQRCodeIDs;
    private ArrayList<GameQRCode> myQRCodes;
    private Integer userScore;
    final String TAG = "Sample";
    Button addCityButton;

    EditText addCityEditText;
    EditText addProvinceEditText;
    FirebaseFirestore userDBInst;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);

        TextView usernameText = findViewById(R.id.userNameText);
        TextView qrNumText = findViewById(R.id.numOfQRCodes);
        TextView totalScoreText = findViewById(R.id.userTotalScore);
        TextView highestScoreText = findViewById(R.id.userHighScore);

        userDBInst = FirebaseFirestore.getInstance();

        userDBInst.collection("Players")
                .whereEqualTo("userid", selectedUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                selectedUser.setUsername((String) document.getData().get("username"));
                                userScore = (Integer) document.getData().get("totalScore");
                                myQRCodeIDs = (ArrayList<String>) document.getData().get("scannedQRCodeIds");
                            }
                        }
                    }
                });
        usernameText.setText(selectedUser.getUsername());
        qrNumText.setText(Integer.toString(myQRCodeIDs.size()));
        totalScoreText.setText(userScore);


        for(int i = 0; i < myQRCodeIDs.size(); i++) {
            //Create loop to grab and instantiate all QR codes scanned by a user
            userDBInst.collection("GameQRCodes")
                    .whereEqualTo(myQRCodeIDs.get(i), selectedUserID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    GameQRCode tempQR = new GameQRCode((String)document.getData().get("hash"));
                                    myQRCodes.add(tempQR);
                                    //get highest scoring QR
                                }
                            }
                        }
                    });
        }







        }




    public Integer getTotalScore(){
        return userScore;
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
