package com.example.qr_go;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.O)
/**
 * QRGODBUtil is a util class that helps with manipulating the db
 * @Author Darius Fang
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
     *  if it exists, it will update the current, otherwise it will make a new one
     * @param gameqrcode the qrcode scanned
     * @param player the player that scanned it
     * @param qrphoto
     * @Author Darius Fang
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateScannedQRtoDB(@NonNull GameQRCode gameqrcode, Player  player, QRPhoto qrphoto) {

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
     * @param gameqrcode the qrcode that scanned it
     * @param player the player that scanned it
     * @param qrphoto
     * @Author Darius Fang
     */
    private void updateScannedQRtoDBContinue(GameQRCode gameqrcode, Player player, QRPhoto qrphoto){
        if (QRCodeList.isEmpty()){
            gameqrcode.addUser(player);
            db.collection("GameQRCodes").document(gameqrcode.getHash()).set(gameqrcode);
            player.addQRCode(gameqrcode);
            db.collection("Players").document(player.getUserid()).set(player);
            if (qrphoto != null) {
                db.collection("QRPhotos").document(qrphoto.getQRID()).set(qrphoto);
            }
        }
        else{
            gameqrcode = QRCodeList.get(0);
            // Add GameQR to DB
            if (!gameqrcode.getUserIds().containsKey(player.getUserid())) {
                gameqrcode.addUser(player);
                db.collection("GameQRCodes").document(gameqrcode.getHash()).update("userIds", gameqrcode.getUserIds());
                // Update Player to DB
                player.addQRCode(gameqrcode);
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
     * @param comments the comment structure being updated
     * @param gameqrcode qrcode of the comments
     * @Author Darius Fang
     */
    public void addCommenttoDB(@NonNull CommentsQR comments, @NonNull GameQRCode gameqrcode){
        db.collection("Comments").document(gameqrcode.getHash()).set(comments.getComments());
    }

    /**
     * deletes the player by flagging them to be invisable, then it continues to delete the player attached to GameQRcodes
     * @param player player being deleted
     * @Author Darius Fang
     */
    public void deletePlayerFromDB(@NonNull Player player){
        DocumentReference docRef = db.collection("Players").document(player.getUserid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    Player player = documentSnapshot.toObject(Player.class);
                    player.deletePlayer();
                    db.collection("Players").document(player.getUserid()).set(player);
                    deletePlayerFromDBContinue(player.getUserid());
                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                }
            }
        });
    }
    /**
     * this portion deletes the playerid from all qrids
     * @param userId the id of the player being deleted
     * @Author Darius Fang
     **/
    private void deletePlayerFromDBContinue(String userId){
        CollectionReference QRRef = db.collection("GameQRCodes");
        QRRef.orderBy("userIds."+userId).get() .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    try {
                        GameQRCode qrcode = snapshot.toObject(GameQRCode.class);
                        qrcode.deleteUser(userId);
                        db.collection("GameQRCodes").document(qrcode.getId()).set(qrcode);
                    }
                    catch (Exception e){
                        /** breaks if object is an older version**/
                    }
                }
            }
        });
    }
    /**
     * deletes the gameqrcode of the player
     * @param player player that is removing qrcode
     * @param qrid qrid being removed from player
     * @Author Darius Fang
     */
    public void deleteGameQRcodeFromPlayer(String qrid, @NonNull Player player){
        player.deleteQRCode(qrid);
        db.collection("Players").document(player.getUserid()).set(player);
        DocumentReference docRef = db.collection("GameQRCodes").document(qrid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        GameQRCode qrcode = documentSnapshot.toObject(GameQRCode.class);
                        qrcode.deleteUser(player.getUserid());
                        db.collection("GameQRCodes").document(qrcode.getHash()).set(qrcode);
                }});

    }
    /**
     * deletes the gameqrcode by flagging it, removed attach id to all players
     * @param gameqrcode the gameqrcode that is being deleted
     * @Author Darius Fang
     */
    public void deleteGameQRFromDB(@NonNull GameQRCode gameqrcode){
        DocumentReference docRef = db.collection("GameQRCodes").document(gameqrcode.getHash());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    GameQRCode gameqrcode = documentSnapshot.toObject(GameQRCode.class);
                    gameqrcode.deleteQR();
                    db.collection("GameQRCodes").document(gameqrcode.getHash()).set(gameqrcode);
                    deleteGameQRFromDBContinue(gameqrcode.getId());
                }catch (Exception e){
                    /** sometimes the db picks up that it exists while in fact it does not... strange
                     */
                }
            }
        });
    }
    /**
     this portion deletes the qrids from all players
     @param qrid the id of the gameqrcode
     @Author Darius Fang
     **/
    private void deleteGameQRFromDBContinue(String qrid){
        CollectionReference QRRef = db.collection("Players");
        QRRef.orderBy("scannedQRCodeIds."+qrid).get() .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    try {
                        Player player = snapshot.toObject(Player.class);
                        player.deleteQRCode(qrid);
                        db.collection("Players").document(player.getUserid()).set(player);
                    }
                    catch (Exception e){
                        /** breaks if object is an older version**/
                    }
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
        Player player1 = new Player();
        /**
         * Assumption: in qrinfo activity, comments are loaded, input updated comments
         */
        CommentsQR comments = new CommentsQR();
        comments.addComment(player, "hi this is working", null);
        comments.addComment(player1, "hi this is working adfdsf", null);

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
    void test4(){
        Player player = new Player("ea7f3186-c04a-4e41-8a77-e975065071f9", "fd7467b8-71fa-424f-b434-e1e9437eb1c6", "FunkyCoder8063", "");
        db.collection("Players").document(player.getUserid()).set(player);
        deletePlayerFromDB(player);
    }

    void test5(){
        Player player = new Player("ea7f3186-c04a-4e41-8a77-e975065071f9", "fd7467b8-71fa-424f-b434-e1e9437eb1c6", "FunkyCoder8063", "");
        GameQRCode qrcode = new GameQRCode("BFG5DGW50\n");
        updateScannedQRtoDB(qrcode,player, null);
    }
    void test5a(){
        Player player = new Player("ea7f3186-c04a-4e41-8a77-e975065071f9", "fd7467b8-71fa-424f-b434-e1e9437eb1c6", "FunkyCoder8063", "");
        GameQRCode qrcode = new GameQRCode("BFG5DGW50\n");
        deleteGameQRcodeFromPlayer(qrcode.getHash(),player);
    }

}