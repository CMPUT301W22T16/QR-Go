package com.example.qr_go;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.nio.file.FileAlreadyExistsException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.O)
/**
 * QRGODBUtil is a util class that helps with manipulating the db
 */
public class QRGoDBUtil {
    /**global variables
     *
     */
    ArrayList<GameQRCode>  QRCodeList = new ArrayList<GameQRCode>();
    FirebaseFirestore db = MapsActivity.db;
    private Context context;
    public QRGoDBUtil(Context context){
        this.context = context; // Set context to do activity actions
    }
    public QRGoDBUtil(){
        super();
    }
    /** Gets QR  from database then it executes updateScannedQRtoDBContinue
     *
     * @Author Darius Fang
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateScannedQRtoDB(GameQRCode gameqrcode, Player  player, QRPhoto qrphoto) {

        DocumentReference docRef = db.collection("GameQRCodes").document(gameqrcode.getHash());

        QRCodeList.clear();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    GameQRCode qrcode = documentSnapshot.toObject(GameQRCode.class);
                    QRCodeList.add(qrcode);
                    updateScannedQRtoDBContinue(null, player, qrphoto);
                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                    QRCodeList.clear();
                    updateScannedQRtoDBContinue(gameqrcode, player, qrphoto);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateScannedQRtoDBContinue(gameqrcode, player, qrphoto);
            }
        });;


    }
    /** ADDs QR to database
     * IF the QR exists, it will update the QR, otherwise it will add it
     * it also adds the userid
     *
     * If user alreay scanned the qr, it will return a message to the user saying they already scanned it
     *
     * @param gameqrcode
     * @param player
     * @param qrphoto
     * @Author Darius Fang
     */
    void updateScannedQRtoDBContinue(GameQRCode gameqrcode, Player player, QRPhoto qrphoto){
        String UserID = player.getUserid();
        if (QRCodeList.isEmpty()){
            gameqrcode.addUser(UserID);
            db.collection("GameQRCodes").document(gameqrcode.getHash()).set(gameqrcode);
        }
        else{
            gameqrcode = QRCodeList.get(0);
            // Add GameQR to DB
            if (!gameqrcode.getUserIds().contains(UserID)) {
                gameqrcode.addUser(UserID);
                db.collection("GameQRCodes").document(gameqrcode.getHash()).update("userIds", gameqrcode.getUserIds());
                // Update Player to DB

                if (!player.getScannedQRCodeIds().contains(gameqrcode.getId())){
                    player.addQRCode(gameqrcode);
                }
                db.collection("Players").document(player.getUserid()).set(player);
                // Update Photo to DB
                if (qrphoto != null){
                    db.collection("QRPhotos").document(qrphoto.getQRID()).set(qrphoto);
                }
            }else{
                Toast.makeText(context.getApplicationContext(), "You already scanned this :/", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * starts by getting the comment from the db, if it cannot find one it will make one, method continues to addCommenttoDBContinue
     * @param comment
     * @param gameqrcode
     * @Author Darius Fang
     */
    void addCommenttoDB(Comment comment, GameQRCode gameqrcode){
        DocumentReference docRef = db.collection("Comments").document(gameqrcode.getHash());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            //@RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    ListComments comments = documentSnapshot.toObject(ListComments.class);
                    comments.addComment(comment);
                    addCommenttoDBContinue(comments, gameqrcode);
                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
//                    ListComments comments = new ListComments();
//                    comments.addComment(comment);
//                    addCommenttoDBContinue(comments, gameqrcode);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                ListComments comments = new ListComments();
//                comments.addComment(comment);
//                addCommenttoDBContinue(comments, gameqrcode);
            }
        });;



    }

    /**
     * Adding comment list back to the database
     * @param comments
     * @param gameQRCode
     * @Author Darius Fang
     */
    public void addCommenttoDBContinue(ListComments comments, GameQRCode gameQRCode){
        if (comments.size() >= 1){
            db.collection("Comments").document(gameQRCode.getHash()).set(comments);
        }
        else{
            //it failed send error message
        }

    }

    /**
     * deletes the player by flagging them to be invisable
     * @param player
     * @Author Darius Fang
     */
    void deletePlayerFromDB(Player player){
        DocumentReference docRef = db.collection("Players").document(player.getUserid());

        QRCodeList.clear();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    Player player = documentSnapshot.toObject(Player.class);
//                    player.deletePlayer();
                    db.collection("Players").document(player.getUserid()).set(player);
                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                }
            }
        });

    }

    /**
     * deletes the gameqrcode by flagging it
     * @param gameqrcode
     * @Author Darius Fang
     */
    void deleteGameQRFromDB(GameQRCode gameqrcode){
        DocumentReference docRef = db.collection("GameQRCodes").document(gameqrcode.getHash());

        QRCodeList.clear();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    GameQRCode qrcode = documentSnapshot.toObject(GameQRCode.class);
//                    qrcode.deleteQR();
                    db.collection("GameQRCodes").document(gameqrcode.getHash()).set(gameqrcode);
                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                }
            }
        });

    }
    /**this is to test the db will be thrown out**/

    void test1(){
        GameQRCode qrcode = new GameQRCode("BFG5DGW54\n");
        Player player = new Player();
        db.collection("Players").document(player.getUserid()).set(player);
        updateScannedQRtoDB(qrcode,player, null);

    }
}