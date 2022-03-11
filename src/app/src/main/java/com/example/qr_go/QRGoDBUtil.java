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
import com.google.firebase.firestore.WriteBatch;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.O)
/**
 * QRGODBUtil is a util class that helps with manipulating the db and getting values of the db.
 */
public class QRGoDBUtil {
    /**global variables
     *
     */
    ArrayList<GameQRCode>  QRCodeList = new ArrayList<GameQRCode>();
    FirebaseFirestore db = MapsActivity.db;
    /** Gets QR  from database then it executes add QR to database
     *
     * @Author Darius Fang
     *
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateScannedQRtoDB(GameQRCode gameqrcode, Player  player, QRPhoto qrphoto, GeoLocation geolocation) {

        DocumentReference docRef = db.collection("GameQRCodes").document(gameqrcode.getHash());
        QRCodeList.clear();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    GameQRCode qrcode = documentSnapshot.toObject(GameQRCode.class);
                    QRCodeList.add(qrcode);
                    updateScannedQRtoDBContinue(null, player, qrphoto, geolocation);
                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                    QRCodeList.clear();
                    updateScannedQRtoDBContinue(gameqrcode, player, qrphoto, geolocation);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateScannedQRtoDBContinue(gameqrcode, player, qrphoto, geolocation);
            }
        });;


    }
    /** ADDs QR to database
     * IF the QR exists, it will update the QR, otherwise it will add it
     * it also adds the userid
     *
     * @Author Darius Fang
     */
    /**
     *
     * @param gameqrcode
     * @param player
     * @param qrphoto
     * @param geolocation
     */
    void updateScannedQRtoDBContinue(GameQRCode gameqrcode, Player player, QRPhoto qrphoto, GeoLocation geolocation){
        String UserID = player.getUserid();
        if (QRCodeList.isEmpty()){
            gameqrcode.addUser(UserID);
            db.collection("GameQRCodes").document(gameqrcode.getHash()).set(gameqrcode);
        }
        else{
            GameQRCode newGameQRCode = QRCodeList.get(0);
            // Add GameQR to DB
            if (!newGameQRCode.getUserIds().contains(UserID)) {
                newGameQRCode.addUser(UserID);
                db.collection("GameQRCodes").document(newGameQRCode.getHash()).update("userIds", newGameQRCode.getUserIds());

            }
            // Update Player to DB

        }
        if (!player.getScannedQRCodeIds().contains(gameqrcode.getId())){
            player.addQRCode(gameqrcode);
        }
        db.collection("Players").document(player.getUserid()).set(player);
        // Update Photo to DB
        if (qrphoto != null){
            db.collection("QRPhotos").document(qrphoto.getQRID()).set(qrphoto);
        }
        //  Update GeoLocation to DB
        if (geolocation != null){
            db.collection("Geolocations").document(qrphoto.getQRID()).set(qrphoto);
        }

    }


    void test1(){
        GameQRCode qrcode = new GameQRCode("BFG5DGW54\n");
        Player player = new Player();
        db.collection("Players").document(player.getUserid()).set(player);
        updateScannedQRtoDB(qrcode,player, null, null);

    }
}
