package com.example.qr_go;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PlayerProfileActivity extends BaseActivity {

    public static FirebaseFirestore db;
    private Player currentUser = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_profile_activity);
        initializeNavbar();
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

    /**
     * Opens an AlertDialog and displays the qr code bitmap into the ImageView
     * @param qrBitmap
     */
    private void openQrDialog(String title, Bitmap qrBitmap){
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_show_qr_code, null);
        ImageView imageView = view.findViewById(R.id.qr_code_image);
        imageView.setImageBitmap(qrBitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(title).setPositiveButton("Close",null).create().show();
    }

    /**
     * Generate game status qr code and display to the user
     * @param view
     */
    public void generateStatusQR(View view) {
        StatusQRCode statusQRCode = new StatusQRCode(MapsActivity.getUserId());
        openQrDialog("My Game Status QR Code", statusQRCode.getQRCode());
    }

    /**
     * Generate login qr code and display to the user
     * @param view
     */
    public void generateLoginQR(View view){
        String loginQrData = MapsActivity.getUserId() + "\n" + MapsActivity.getPassword();
        LoginQRCode loginQRCode = new LoginQRCode(loginQrData);
        openQrDialog("My Login QR Code", loginQRCode.getQRCode());
    }
}
