package com.example.qr_go;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;

public class QRGoDBUtil {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void AddQRtoDatabase(String qrID, String UserID)throws NoSuchAlgorithmException {
        FirebaseFirestore db = MapsActivity.db;

        QRCode temp = new QRCode(qrID);
        String hash = temp.getHash();
        DocumentReference docRef = db.collection("GameQRCodes").document(hash);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                GameQRCode code = documentSnapshot.toObject(GameQRCode.class);
                /** NEED TO CHECK IF USERID IS REGISTERED
                 *
                 *
                 *
                 *
                 *
                 * **/
                db.collection("GameQRCodes").document(code.getHash()).set(code).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("SUCCESS", "Data has been added successfully!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FAILURE", "Data could not be added: " + e.toString());
                    }
                });
            }
        });
    }
}
