package com.example.qr_go;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QRInfoActivity extends BaseActivity {

    private QRGoDBUtil db;
    FirebaseFirestore gameQRDBInst;
    private Player thisTempPlayer; // TODO: temporary, replace with currently logged in user

    private GameQRCode selectedQR;
    private String selectedQRId;

    private QRPhoto[] QRPhotos;
    private ListComments comment;
//    private GeoLocation location;     // TODO: uncomment after GeoLocation is implemented

    CommentsQR comments;

    ListView commentList;
    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentDataList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);
        initializeNavbar();

        db = new QRGoDBUtil(this);
        gameQRDBInst = FirebaseFirestore.getInstance();

        TextView tvQRName = (TextView) findViewById(R.id.qrName);
        TextView tvQRLocation = (TextView) findViewById(R.id.qrLocation);
        TextView tvScore = (TextView) findViewById(R.id.qrScore);

        // Get information from extras
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            selectedQR = null;
        } else {
            selectedQRId = extras.getString("QRid");

            // set info from QRId
            gameQRDBInst.collection("GameQRCodes")
                    .whereEqualTo("id", selectedQRId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (DocumentSnapshot document : task.getResult()) {
                                    selectedQR = document.toObject(GameQRCode.class);
                                }

                                tvQRName.setText(selectedQR.getId());
//                                tvQRLocation.setText(selectedQR.getGeoLocation().getAddress().toString());
                                tvScore.setText("Score: "+selectedQR.getScore());

                            }
                            else {
                                selectedQR = null;
                            }
                        }
                    });
        }

        commentList = findViewById(R.id.myQRList);

        comments = new CommentsQR();

        // TODO: temporary, set to currently logged in user
        thisTempPlayer = new Player();
        thisTempPlayer.setUsername("QRInfo Temp Player");

        // TODO: temporary, set to currently viewing GameQRCode
        selectedQR = new GameQRCode();

        // TODO: these are temporary. grab data from DB.
        String []usernames = {"User1", "User2", "User3", "User4", "User5", "User6"};
        String []msgs = {"msg 1", "msg 2", "msg 3", "msg 4", "msg 5", "msg 6"};

        for(int i=0; i<usernames.length; i++) {
            Player player = new Player();
            player.setUsername(usernames[i]);
            comments.addComment(player, msgs[i], null);
        }

        commentDataList = comments.getCommentObjects();
        //TODO: sort commentDataList

        commentAdapter = new ListComments(this, commentDataList);
        commentList.setAdapter(commentAdapter);
    }


    public void showUsersList(View view) {
        Intent intent = new Intent(QRInfoActivity.this, ScannedUsersActivity.class);
        intent.putExtra("QRid", selectedQRId);
        startActivity(intent);
    }

    /**
     * Display all comments added to this page
     */
    public void showComments() {

    }

    public void showLocation() {

    }

    public void showIcon() {

    }

    /**
     * Adds a new comment to this page
     */
    public void addComment(View view) { // TODO: add to DB
        EditText inputComment = (EditText) findViewById(R.id.inputComment);
        String message = inputComment.getText().toString();

        Comment comment = new Comment(thisTempPlayer.getUsername(), message);
        comments.addComment(thisTempPlayer, message, null);
        commentAdapter.add(comment);
//        db.addCommenttoDB(comments, selectedQR);

        inputComment.setText(""); // clear input after send
    }

    public void setSelectedQR(String QRId) {

    }

}
