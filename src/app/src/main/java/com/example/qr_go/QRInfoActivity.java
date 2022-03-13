package com.example.qr_go;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QRInfoActivity extends AppCompatActivity {

    private GameQRCode selectedQR;
    private QRPhoto[] QRPhotos;
    private ListComments comment;
//    private GeoLocation location;     // TODO: uncomment after GeoLocation is implemented

    ListView commentList;
    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);

        commentList = findViewById(R.id.myQRList);
        commentDataList = new ArrayList<>();
        commentAdapter = new ListComments(this, commentDataList);

        commentList.setAdapter(commentAdapter);
    }

    public void showUsersList() {

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
    public void addComment(View view) {

    }

}
