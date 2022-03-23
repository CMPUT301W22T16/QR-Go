package com.example.qr_go;

import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * LoginQRCode is the QR code that users can use to login to their account on another device
 */
public class LoginQRCode extends QRCode implements GeneratesNewQR {
    static String QR_IDENTIFIER = "login-qr-code-"; // should be pre-pended to all LoginQRCodes
    private String uid;
    private String password;

    /**
     * QR code
     *
     * @param qrCodeContents text contents scanned from QR code
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LoginQRCode(String qrCodeContents) {
        /* Constructor expected to be called when creating LoginQRCode for verifying if loginQR is
           correct, i.e., isLoginValid is called, this constructor could also not exist and
           isLoginValid could be a static method */
        super(qrCodeContents);
        qrCodeContents = qrCodeContents.replaceFirst("^" + QR_IDENTIFIER, "");
        // turn qrCodeContent into uid and password
        // assuming form of id + "\n" + password
        String[] input = qrCodeContents.split("\n");
        uid = input[0];
        password = input[1];
    }


    /**
     * Validates if login QR code and password is valid
     *
     * @return whether qr code is valid
     */
    public void isLoginValid(Consumer<String> successCb, Consumer<String> failureCb) {
        // check database to see if user id and password exist
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Players").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String dbPassword = (String) document.getData().get("password");
                        if (password.equals(dbPassword)){
                            successCb.accept("Login is valid");
                        }else{
                            failureCb.accept("Invalid username and/or password");
                        }
                    } else {
                        // User does not exist
                        failureCb.accept("User does not exist");
                    }
                } else {
                    Log.d("FAILURE", "get failed with ", task.getException());
                    failureCb.accept("Database fetch failed");
                }
            }
        });
    }

    /***
     * Converts the user's id and password to a QR code
     * @return QR code to display
     */
    @Override
    public Bitmap getQRCode() {
        // qr data is the identifier and then the userid and password
        return encodeToQrCode(QR_IDENTIFIER + uid + "\n" + password, 800, 800);
    }

    /**
     * @return user's id
     */
    public String getUserId() {
        return uid;
    }

    /**
     * @return user's password
     */
    public String getPassword() {
        return password;
    }
}
