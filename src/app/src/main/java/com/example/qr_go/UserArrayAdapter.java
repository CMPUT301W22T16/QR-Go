package com.example.qr_go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<UserListDisplayContainer> {
    private Context context;
    private ArrayList<UserListDisplayContainer> userDisplays;
    private Integer sortPos;

    public UserArrayAdapter(@NonNull Context context, ArrayList<UserListDisplayContainer> userDisplays, Integer sortPos) {
        super(context, 0, userDisplays);
        this.context = context;
        this.userDisplays = userDisplays;
        this.sortPos = sortPos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        UserListDisplayContainer userToDisplay = userDisplays.get(position);

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_list_content, parent, false);
        }

        TextView usernameView = view.findViewById(R.id.username_view);
        TextView scoreView = view.findViewById(R.id.user_score_view);

        usernameView.setText(userToDisplay.getUsername());
        switch(sortPos) {
            case 0:
                scoreView.setText("Total Score: " + userToDisplay.getTotalScore().toString());
                break;
            case 1:
                scoreView.setText(userToDisplay.getNumQRs().toString() + " scanned");
                break;
            default:
                // Nothing else for now
                scoreView.setText("Total Score: " + userToDisplay.getTotalScore().toString());
                break;
        }


        return view;
    }
}
