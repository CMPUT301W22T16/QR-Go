package com.example.qr_go.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.qr_go.R;
import com.example.qr_go.objects.LoginQRCode;
import com.example.qr_go.objects.Player;
import com.example.qr_go.objects.StatusQRCode;
import com.example.qr_go.utils.QRGoStorageUtil;
import com.example.qr_go.utils.StringUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PlayerProfileActivity extends BaseActivity {
    public StringUtil stringUtil = new StringUtil();
    public static FirebaseFirestore db;
    private Player currentUser = new Player();
    private String currentUserId;
    public static final int GET_FROM_GALLERY = 3;
    public FirebaseStorage storage;
    private EditText usernameEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_profile_activity);
        initializeNavbar();

        usernameEditText = findViewById(R.id.player_username);
        emailEditText = findViewById(R.id.player_email);
        ImageView profileImage = findViewById(R.id.profile_photo);
        currentUserId = MapsActivity.getUserId();
        db = FirebaseFirestore.getInstance();
        storage = MapsActivity.storage;
        StorageReference storageRef = storage.getReference();
        String ImageRef = stringUtil.ImagePlayerRef(currentUserId);
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
    }

    /**
     * When save button is clicked,
     * validate the email and username fields
     * If not valid, show errors
     * If valid, save to database
     */
    public void saveProfile(View view) {
        String emailStr = emailEditText.getText().toString();
        String usernameStr = usernameEditText.getText().toString();
        currentUser.setEmail(emailStr);
        currentUser.setUsername(usernameStr);

        boolean isValid = true;

        if (!currentUser.isEmailValid()) {
            emailEditText.setError("Invalid email address");
            isValid = false;
        }
        if (false) { // TODO: Add if username is not unique here!
            usernameEditText.setError("This username is already taken");
            isValid = false;
        }

        if (isValid) {
            // Save to database
            // save username
            db.collection("Players").document(currentUserId).update("username", usernameStr);
            // save email
            db.collection("Players").document(currentUserId).update("email", emailStr);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Opens an AlertDialog and displays the qr code bitmap into the ImageView
     *
     * @param qrBitmap
     */
    private void openQrDialog(String title, Bitmap qrBitmap) {
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_show_qr_code, null);
        ImageView imageView = view.findViewById(R.id.qr_code_image);
        imageView.setImageBitmap(qrBitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(title).setPositiveButton("Close", null).create().show();
    }

    /**
     * Generate game status qr code and display to the user
     *
     * @param view
     */
    public void generateStatusQR(View view) {
        StatusQRCode statusQRCode = new StatusQRCode(MapsActivity.getUserId());
        openQrDialog("My Game Status QR Code", statusQRCode.getQRCode());
    }

    /**
     * Generate login qr code and display to the user
     *
     * @param view
     */
    public void generateLoginQR(View view) {
        String loginQrData = MapsActivity.getUserId() + "\n" + MapsActivity.getPassword();
        LoginQRCode loginQRCode = new LoginQRCode(loginQrData);
        openQrDialog("My Login QR Code", loginQRCode.getQRCode());
    }

    /**
     * Open phone gallery to upload a new profile photo
     *
     * @param view
     */
    public void editProfilePhoto(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    /**
     * After user selects new profile photo from gallery, save it as the new photo
     * Source: https://stackoverflow.com/a/9107983
     * Author: Dhruv Gairola https://stackoverflow.com/users/495545/dhruv-gairola
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView profileImage = findViewById(R.id.profile_photo);
                profileImage.setImageBitmap(bitmap);
                QRGoStorageUtil StorageUtil = new QRGoStorageUtil();
                StorageUtil.updateImageFromStorage(bitmap, stringUtil.ImagePlayerRef(MapsActivity.getUserId()));
                Toast.makeText(this, "Updated image", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "ERROR: file not found", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(this, "ERROR: io error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}
