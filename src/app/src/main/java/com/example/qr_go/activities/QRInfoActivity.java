package com.example.qr_go.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.qr_go.adapters.ListCommentsAdapter;
import com.example.qr_go.R;
import com.example.qr_go.containers.CommentDisplayContainer;
import com.example.qr_go.objects.CommentsQR;
import com.example.qr_go.objects.GameQRCode;
import com.example.qr_go.objects.GeoLocation;
import com.example.qr_go.objects.Player;
import com.example.qr_go.utils.QRGoDBUtil;
import com.example.qr_go.utils.StringUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class QRInfoActivity extends BaseActivity {

    FirebaseFirestore db;
    private Player currentUser = new Player();
    private Bitmap UserImage = null;
    private GameQRCode selectedQR;
    private String selectedQRId;

    String currentUserId = MapsActivity.getUserId();
    private ListCommentsAdapter comment;
    final long ONE_MEGABYTE = 4 * 1024 * 1024;

    private Intent usersActivityIntent;

    private CommentsQR comments;
    QRGoDBUtil dbUtil = new QRGoDBUtil(this);
    private ListView commentList;
    private ArrayAdapter<CommentDisplayContainer> commentAdapter;
    private ArrayList<CommentDisplayContainer> commentDataList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_info);
        initializeNavbar();

        usersActivityIntent = new Intent(QRInfoActivity.this, ScannedUsersActivity.class);

        db = FirebaseFirestore.getInstance();



        TextView tvQRName = (TextView) findViewById(R.id.qrName);
        TextView tvQRLocation = (TextView) findViewById(R.id.qrLocation);
        TextView tvScore = (TextView) findViewById(R.id.qrScore);

        // Get information from extras
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
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
                                if (selectedQR == null) {
                                    finish(); // RESTART this activity
                                    startActivity(getIntent());
                                    return;
                                }; // ABORT: an error occurred

                                tvQRName.setText(selectedQR.getId().substring(0,8));
                                tvScore.setText("Score: " + selectedQR.getScore());

                                GeoLocation geoLocation = selectedQR.getGeoLocation();
                                if (geoLocation != null)
                                    tvQRLocation.setText(geoLocation.getAddress());

                                // set image Darius Fang
                                ImageView profileImage = findViewById(R.id.profile_photo);
                                FirebaseStorage storage = MapsActivity.storage;
                                StringUtil stringUtil = new StringUtil();
                                StorageReference storageRef = storage.getReference();
                                String ImageRef = stringUtil.ImageQRRef(selectedQR.getId(), selectedQR.getUserObjects().get(0));
                                StorageReference islandRef = storageRef.child(ImageRef);
                                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        profileImage.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        return;
                                    }
                                });


                            }
                        }
                    });

            // get comments
            db.collection("Comments")
                    .document(selectedQRId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            try {
                                comments = documentSnapshot.toObject(CommentsQR.class);
                                commentDataList = comments.getCommentObjects();
                                commentAdapter = new ListCommentsAdapter(QRInfoActivity.this, commentDataList);
                                commentList.setAdapter(commentAdapter);
                                addImages();
                            }catch (Exception e){
                                //sometimes the db picks up that it exists while in fact it does not... strange
                                comments = new CommentsQR();
                                commentDataList = comments.getCommentObjects();
                                commentAdapter = new ListCommentsAdapter(QRInfoActivity.this, commentDataList);
                                commentList.setAdapter(commentAdapter);
                                addImages();
                                e.printStackTrace();
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    comments = new CommentsQR();
                    commentDataList = comments.getCommentObjects();
                    commentAdapter = new ListCommentsAdapter(QRInfoActivity.this, commentDataList);
                    commentList.setAdapter(commentAdapter);
                    addImages();
                }
            });

        }

        commentList = findViewById(R.id.myQRList);
    }
    private void addImages() {
        FirebaseStorage storage = MapsActivity.storage;
        StringUtil stringUtil = new StringUtil();
        StorageReference storageRef = storage.getReference();
        for (CommentDisplayContainer comment : commentDataList) {
            String ImageRef = stringUtil.ImagePlayerRef(comment.getUserid());
            StorageReference islandRef = storageRef.child(ImageRef);
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    comment.setPicture(bitmap);
                    commentAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    return;
                }
            });
        }
        String ImageRef = stringUtil.ImagePlayerRef(currentUserId);
        StorageReference islandRef = storageRef.child(ImageRef);
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                UserImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                return;
            }
        });

    }


    public void showUsersList(View view) {
        startActivity(usersActivityIntent);
    }

    /**
     * Adds a new comment to this page
     */
    public void addComment(View view) {
        EditText inputComment = (EditText) findViewById(R.id.inputComment);
        String message = inputComment.getText().toString();



        db.collection("Players")
                .whereEqualTo("userid", currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    // code from https://stackoverflow.com/questions/65465335/get-specific-field-from-firestore-with-whereequalto
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentUser = document.toObject(Player.class);

                                CommentDisplayContainer comment = new CommentDisplayContainer(currentUser.getUsername(), message, currentUser.getUserid());
                                comment.setPicture(UserImage);
                                commentAdapter.add(comment);

                                dbUtil.addCommenttoDB(currentUser, comment, selectedQR);
                            }
                        }
                    }
                });

        inputComment.setText(""); // clear input after send
    }

}
