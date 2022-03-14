package com.example.qr_go;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QRInfoActivity extends AppCompatActivity {

    private QRGoDBUtil db;
    private Player thisTempPlayer; // TODO: temporary, replace with currently logged in user

    private GameQRCode selectedQR;
    private QRPhoto[] QRPhotos;
    private ListComments comment;
//    private GeoLocation location;     // TODO: uncomment after GeoLocation is implemented

    CommentsQR comments;

    ListView commentList;
    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);

        db = new QRGoDBUtil(this);

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

}
