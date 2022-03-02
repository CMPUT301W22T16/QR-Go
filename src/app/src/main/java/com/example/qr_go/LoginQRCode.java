package com.example.qr_go;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * LoginQRCode is the QR code that users can use to login to their account on another device
 */
public class LoginQRCode extends QRCode implements  GeneratesNewQR {
    User user;
    /**
     * On create, convert qr content to hash
     * @param user string content of qr code
     * @throws NoSuchAlgorithmException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LoginQRCode(User user) throws NoSuchAlgorithmException {
        super(user.getUserid()+"\n"+user.getPassword());
        this.user = user;
    }
    /**
     * QR code
     * @param qrCodeContents text contents scanned from QR code
     * @throws NoSuchAlgorithmException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LoginQRCode(String qrCodeContents) throws NoSuchAlgorithmException {
        /* Constructor expected to be called when creating LoginQRCode for verifying if loginQR is
           correct, i.e., isLoginValid is called, this constructor could also not exist and
           isLoginValid could be a static method */
        super(qrCodeContents);
    }

    /**
     * Validates if login QR code and password is valid
     * @return whether qr code is valid
     */
    public boolean isLoginValid(String qrCodeContent) {
        // turn qrCodeContent into uid and password
        // assuming form of id + "\n" + password
        String[] input = qrCodeContent.split("\n");
        String uid = input[0];
        String password = input[1];

        // check database to see if user id and password exist
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(uid); // correct name?
        Map<String, Object> data = new HashMap<>();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("SUCCESS", "DocumentSnapshot data: " + document.getData());
                        data.put("password", document.getData().get("password"));
                    } else {
                        Log.d("FAILURE", "No such document");
                    }
                } else {
                    Log.d("FAILURE", "get failed with ", task.getException());
                }
            }
        });

        // return result
        return (data.get("password").equals(password));
    }

    /***
     * Converts the user's id and password to a QR code
     * @return QR code to display
     */
    @Override
    public Bitmap getQRCode(){
        return encodeToQrCode(user.getUserid()+"\n"+user.getPassword(), 100, 100);
    }
}
