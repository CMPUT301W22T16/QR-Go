package com.example.qr_go;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PlayerInfoActivity extends AppCompatActivity {
    private User selectedUser;
    private String selectedUserID;
    private GameQRCode myQRCodes;
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

        //reused from UsernameGenerator.java
        final Boolean[] valid = new Boolean[1];
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
                            }
                        }
                    }
                });

        }




    public Integer getTotalScore(){
        return selectedUser.getTotalScore();
    }
    public Integer getQRCount() {
        return selectedUser.getScannedQRCodeIds().size();
    }
    public Integer getHighestQRScore() {
        return 0;
    }
    public void showScannedQRList() {
        selectedUser.getScannedQRCodeIds();
    }


}
