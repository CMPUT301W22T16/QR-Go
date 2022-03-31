package com.example.qr_go.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.qr_go.R;
import com.example.qr_go.activities.MapsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
@RequiresApi(api = Build.VERSION_CODES.O)
public class QRGoStorageUtil {
    //TODO send image to DB, resize image before sending to DB, adding a way to reference inside the classes
    // TODO convert between byte and bitmap
    FirebaseStorage storage = MapsActivity.storage;
    public void getImageFromStorage(String photoRef){
        //ImageView profileImage = findViewById(R.id.profile_photo);
        String currentUserId = MapsActivity.getUserId();
        FirebaseStorage storage = MapsActivity.storage;
        storage = MapsActivity.storage;
        StringUtil stringUtil = new StringUtil();
        StorageReference storageRef = storage.getReference();
        String ImageRef = stringUtil.ImagePlayerRef(currentUserId);
        StorageReference islandRef = storageRef.child(ImageRef);
        final long ONE_MEGABYTE = 5 * 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //profileImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                return;
            }
        });
    }

    public void updateImageFromStorage(Bitmap bitmap, String ref) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data1 = baos.toByteArray();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(ref);

        UploadTask uploadTask = imageRef.putBytes(data1);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }
}
