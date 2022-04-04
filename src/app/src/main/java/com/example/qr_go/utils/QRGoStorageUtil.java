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
import java.util.ArrayList;
import java.util.Collections;

@RequiresApi(api = Build.VERSION_CODES.O)
public class QRGoStorageUtil {
    //TODO send image to DB, resize image before sending to DB, adding a way to reference inside the classes
    // TODO convert between byte and bitmap
    FirebaseStorage storage = MapsActivity.storage;

    public void getImageFromStorage(String photoRef) {
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
        Bitmap scaledBitmap = scaledDownBitmap(bitmap);
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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

    /**
     * Reduce memory of bitmap so that big images are not stored online
     * Source: Stack Overflow https://stackoverflow.com/a/17839663
     * Author: Geobits https://stackoverflow.com/users/752320/geobits
     *
     * @param bitmap
     * @return
     */
    private Bitmap scaledDownBitmap(Bitmap bitmap) {
        final int maxSize = 1024;
        int outWidth, outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
    }
    //https://stackoverflow.com/questions/15789049/crop-a-bitmap-image
    public Bitmap squareCropBitmap(Bitmap bitmap){
        int offset = 0;
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        int shorterSide = imageWidth < imageHeight ? imageWidth : imageHeight;
        int longerSide = imageWidth < imageHeight ? imageHeight : imageWidth;
        boolean portrait = imageWidth < imageHeight ? true : false;
        int stride = shorterSide * 1;
        int lengthToCrop = (longerSide - shorterSide) / 2;
        int[] pixels = new int[(shorterSide * shorterSide) + stride];
        bitmap.getPixels(pixels, 0, stride, portrait ? 0 : lengthToCrop, portrait ? lengthToCrop : 0, shorterSide, shorterSide);
        bitmap.recycle();
        Bitmap croppedBitmap = Bitmap.createBitmap(shorterSide, shorterSide, Bitmap.Config.ARGB_4444);
        croppedBitmap.setPixels(pixels, 0,stride, 0, 0, shorterSide, shorterSide);
        return croppedBitmap;
    }
}
