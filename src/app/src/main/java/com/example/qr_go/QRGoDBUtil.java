package com.example.qr_go;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


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
    void updateScannedQRtoDB(@NonNull GameQRCode gameqrcode, Player  player, QRPhoto qrphoto) {

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
        if (QRCodeList.isEmpty()){
            gameqrcode.addUser(player);
            db.collection("GameQRCodes").document(gameqrcode.getHash()).set(gameqrcode);
        }
        else{
            gameqrcode = QRCodeList.get(0);
            // Add GameQR to DB
            if (!gameqrcode.getUserIds().containsKey(player.getUserid())) {
                gameqrcode.addUser(player);
                db.collection("GameQRCodes").document(gameqrcode.getHash()).update("userIds", gameqrcode.getUserIds());
                // Update Player to DB

                if (!player.getScannedQRCodeIds().containsKey(gameqrcode.getId())){
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
     * @param comments
     * @param gameqrcode
     * @Author Darius Fang
     */
    void addCommenttoDB( Comment comments, GameQRCode gameqrcode){
        db.collection("Comments").document(gameqrcode.getHash()).set(comments.getComments());
    }

    /**
     * Adding comment list back to the database
     * @param comments
     * @param gameQRCode
     * @Author Darius Fang
     */


    /**
     * deletes the player by flagging them to be invisable
     * @param player
     * @Author Darius Fang
     */
    public void deletePlayerFromDB(Player player){
        DocumentReference docRef = db.collection("Players").document(player.getUserid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    Player player = documentSnapshot.toObject(Player.class);
                    player.deletePlayer();
                    db.collection("Players").document(player.getUserid()).set(player);
                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                }
            }
        });

    }
    public void deleteGameQRcodeFromPlayer(){
        return;
    }
    /**
     * deletes the gameqrcode by flagging it
     * @param gameqrcode
     * @Author Darius Fang
     */
    public void deleteGameQRFromDB(GameQRCode gameqrcode){
        DocumentReference docRef = db.collection("GameQRCodes").document(gameqrcode.getHash());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    GameQRCode gameqrcode = documentSnapshot.toObject(GameQRCode.class);
                    gameqrcode.deleteQR();
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
    void test2() {
        GameQRCode qrcode = new GameQRCode("BFG5DGW54\n");
        deleteGameQRFromDB(qrcode);
    }
    void test3() {
        GameQRCode qrcode = new GameQRCode("BFG5DGW54\n");
        Player player = new Player();
        /**
         * Assumption: in qrinfo activity, comments are loaded, input updated comments
         */
        Comment comments = new Comment();
        comments.addComment(player, "hi this is working", null);

//        playerids of qrcode
//        HashMap<String, HashMap<String, String>>  playerIDs = new HashMap<>();
//        details.put("Username", "Darius");
//        details.put("PhotoRef", "adf");
//        playerIDs.put(player.getUserid(), details);

        //QRIds of player
//        HashMap<String,Int>>  qrids = new HashMap<>();
//        qrids.put(qrid, score)

        //qrPhoto
//        HashMap<String,Int>>  qrids = new HashMap<>();
//        qrids.put(playerid, reflink)
//         String qrid

        //profilepic
//        string ref
//        String qrid
        addCommenttoDB(comments, qrcode);
    }

}