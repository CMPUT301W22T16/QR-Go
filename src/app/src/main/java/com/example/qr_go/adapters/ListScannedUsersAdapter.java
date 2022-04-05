package com.example.qr_go.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_go.R;
import com.example.qr_go.containers.ScannedUserListDisplayContainer;

import java.util.ArrayList;

/**
 * ArrayAdapter for displaying users that scanned a QR code
 */
public class ListScannedUsersAdapter extends ArrayAdapter {

    private ArrayList<ScannedUserListDisplayContainer> playersInfoArray;
    private Context context;

    public ListScannedUsersAdapter(Context context, ArrayList<ScannedUserListDisplayContainer> playersInfoArray) {
        super(context, 0, playersInfoArray);
        this.playersInfoArray = playersInfoArray;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_scanned_users_content, parent, false);
        }

        ScannedUserListDisplayContainer playersInfo = playersInfoArray.get(position);

        ImageView userPicture = (ImageView) view.findViewById(R.id.user_picture);
        TextView username = (TextView) view.findViewById(R.id.username);

        if (playersInfo.getPicture() != null) {
            userPicture.setImageBitmap(playersInfo.getPicture());
        } else {
            userPicture.setImageResource(R.drawable.round_account_circle_24);
        }
        username.setText(playersInfo.getUsername());

        return view;
    }

}
