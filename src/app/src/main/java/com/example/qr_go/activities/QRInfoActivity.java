package com.example.qr_go.activities;

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

import com.example.qr_go.containers.ListCommentsContainer;
import com.example.qr_go.R;
import com.example.qr_go.objects.Comment;
import com.example.qr_go.objects.CommentsQR;
import com.example.qr_go.objects.GameQRCode;
import com.example.qr_go.objects.Player;
import com.example.qr_go.objects.QRPhoto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class QRInfoActivity extends BaseActivity {

    FirebaseFirestore db;
    private Player thisTempPlayer; // TODO: temporary, replace with currently logged in user

    private GameQRCode selectedQR;
    private String selectedQRId;

    private QRPhoto[] QRPhotos;
    private ListCommentsContainer comment;
//    private GeoLocation location;     // TODO: uncomment after GeoLocation is implemented

    private Intent usersActivityIntent;

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

        usersActivityIntent = new Intent(QRInfoActivity.this, ScannedUsersActivity.class);

        db = FirebaseFirestore.getInstance();

        comments = new CommentsQR();

        TextView tvQRName = (TextView) findViewById(R.id.qrName);
        TextView tvQRLocation = (TextView) findViewById(R.id.qrLocation);
        TextView tvScore = (TextView) findViewById(R.id.qrScore);

        // Get information from extras
        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            selectedQRId = bundle.getString("QRid");

            // set info from QRId
            db.collection("GameQRCodes")
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
                                usersActivityIntent.putExtra("selectedQR", selectedQR);

                                tvQRName.setText(selectedQR.getId());
//                                tvQRLocation.setText(selectedQR.getGeoLocation().getAddress().toString());
                                tvScore.setText("Score: "+selectedQR.getScore());

                            }
                        }
                    });

            // get comments
            db.collection("Comments")
                    .document(selectedQRId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> map = document.getData();
                                if(map != null) {
                                    for (Object commentsInfo : map.values()) {

                                        Map<String, Object> commentInfo = (Map<String, Object>) commentsInfo;

                                        Player player = new Player();
                                        player.setUsername((String) commentInfo.get("Username"));
                                        // TODO: add photolink when it is ready
                                        comments.addComment(player, (String) commentInfo.get("Message"), null);
                                    }
                                }

                                commentDataList = comments.getCommentObjects();

                                commentAdapter = new ListCommentsContainer(QRInfoActivity.this, commentDataList);
                                commentList.setAdapter(commentAdapter);
                            }
                        }
                    });

        }

        commentList = findViewById(R.id.myQRList);

        // TODO: temporary, set to currently logged in user
        thisTempPlayer = new Player();
        thisTempPlayer.setUsername("QRInfo Temp Player");
    }


    public void showUsersList(View view) {
        startActivity(usersActivityIntent);
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
