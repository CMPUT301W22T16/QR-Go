package com.example.qr_go.containers;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_go.R;

import java.util.ArrayList;

public class ListScannedUsersContainer extends ArrayAdapter {

    private ArrayList<Pair> playersInfoArray;
    private Context context;

    public ListScannedUsersContainer(Context context, ArrayList<Pair> playersInfoArray) {
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

        Pair<String, String> playersInfo = playersInfoArray.get(position);

//        ImageView commenterPicture = (ImageView) view.findViewById(R.id.user_picture);
        TextView username = (TextView) view.findViewById(R.id.username);

//        commenterPicture.set ... playerInfo.get("PhotoRef");
        username.setText(playersInfo.second);

        return view;
    }

}
