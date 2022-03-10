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
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.O)
/**
 * QRGODBUtil is a util class that helps with manipulating the db and getting values of the db.
 */
public class QRGoDBUtil {
    /**global variables
     *
     */
    ArrayList<GameQRCode>  QRCodeList = new ArrayList<GameQRCode>();
    GameQRCode temp, qrcode;
    String qrID, UserID;
    Player player;
    FirebaseFirestore db = MapsActivity.db;

    {
        try {
            player = new Player();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }



    /** Gets QR  from database then it executes add QR to database
     *
     * @Author Darius Fang
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    void GrabQRFromDatabase(String qrIDInput, String UserIDInput)throws NoSuchAlgorithmException {
        qrcode = new GameQRCode();
        UserID = UserIDInput;
        qrID = qrIDInput;
        temp = new GameQRCode(qrID);
        String hash = temp.getHash();
        DocumentReference docRef = db.collection("GameQRCodes").document(hash);
        QRCodeList.clear();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    GameQRCode qrcode = documentSnapshot.toObject(GameQRCode.class);
                    QRCodeList.add(qrcode);
                    AddQRtoDatabase();

                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                    QRCodeList.clear();
                    AddQRtoDatabase();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AddQRtoDatabase();
            }
        });;


    }

    /** ADDs QR to database
     * IF the QR exists, it will update the QR, otherwise it will add it
     * it also adds the userid
     *
     * @Author Darius Fang
     */
    void AddQRtoDatabase(){
        if (QRCodeList.isEmpty()){
            qrcode = temp;
            qrcode.addUser(UserID);
            db.collection("GameQRCodes").document(qrcode.getHash()).set(qrcode);
        }
        else{
            qrcode = QRCodeList.get(0);
            if (!qrcode.getUserIds().contains(UserID)) {
                qrcode.addUser(UserID);
                db.collection("GameQRCodes").document(qrcode.getHash()).update("userIds", qrcode.getUserIds());


            }
        }
        try {
            UpdateUser();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void UpdateUser() throws NoSuchAlgorithmException{

        player.addQRCode(qrcode);
        db.collection("Players").document(player.getUserid()).set(player);


    }

    /** this does not work help
     *
     * @throws NoSuchAlgorithmException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    void test1()throws NoSuchAlgorithmException{
        GrabQRFromDatabase("BFG5DGW54\n", "test");
    }
}
