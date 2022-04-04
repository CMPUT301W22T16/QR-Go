package com.example.qr_go.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_go.R;
import com.example.qr_go.containers.QRListDisplayContainer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * CustomInfoWindowAdapter is used to create marker info windows with thumbnails for corresponding QR
 * codes. Code borrowed from Mitch Tabian from the CodingWithMitch Youtube channel (https://www.youtube.com/watch?v=DhYofrJPzlI)
 */
public class CustomInfoWindowAdapter implements InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private Bitmap bitmap;
    int qrPosition;



    public CustomInfoWindowAdapter(Context context, Bitmap bitmap) {
        this.mContext = context;
        this.bitmap = bitmap;
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);


    }
    private void renderWindowInfo(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.infoWindowTitle);

        if(!title.equals("")){
            tvTitle.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.qr_info);

        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }

        ImageView imageView = view.findViewById(R.id.qr_picture);
        if(!bitmap.equals(null)) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowInfo(marker,this.mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {

        renderWindowInfo(marker,this.mWindow);
        return mWindow;
    }
}
