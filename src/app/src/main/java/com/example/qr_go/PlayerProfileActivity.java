package com.example.qr_go;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PlayerProfileActivity extends AppCompatActivity {

    public static FirebaseFirestore db;
    private Player currentUser = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_profile_activity);
        Button backButton = (Button) findViewById(R.id.back_button);

        // Back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        EditText usernameEditText = findViewById(R.id.player_username);
        EditText emailEditText = findViewById(R.id.player_email);

        String currentUserId = MapsActivity.getUserId();

        db = FirebaseFirestore.getInstance();

        db.collection("Players")
                .whereEqualTo("userid", currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentUser.setUsername((String) document.getData().get("username"));
                                currentUser.setEmail((String) document.getData().get("email"));
                                usernameEditText.setText(currentUser.getUsername());
                                emailEditText.setText(currentUser.getEmail());
                            }
                        }
                    }
                });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                db.collection("Players").document(currentUserId).update("username", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                db.collection("Players").document(currentUserId).update("email", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
