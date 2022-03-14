package com.example.qr_go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<UserListDisplayContainer> implements Filterable {
    private Context context;
    private ArrayList<UserListDisplayContainer> allUserDisplays;
    private ArrayList<UserListDisplayContainer> userDisplays;
    private Integer sortPos;
    private Integer userPos = 0;

    public UserArrayAdapter(@NonNull Context context, ArrayList<UserListDisplayContainer> userDisplays, Integer sortPos) {
        super(context, 0, userDisplays);
        this.context = context;
        this.allUserDisplays = new ArrayList<>(userDisplays);
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

        Integer userRank = userToDisplay.getRankPosition();
        if (userToDisplay.getIsCurrentUser()) {
            usernameView.setText(String.valueOf(userRank) + "| " + userToDisplay.getUsername() + " (you)");
            userPos = position;
        } else {
            usernameView.setText(String.valueOf(userRank) + "| " + userToDisplay.getUsername());
        }
        scoreView.setText(userToDisplay.getTotalScore().toString());
        switch(sortPos) {
            case 0:
                scoreView.setText("Total Score: " + userToDisplay.getTotalScore().toString());
                break;
            case 1:
                scoreView.setText(userToDisplay.getNumQRs().toString() + " scanned");
                break;
            default:
                scoreView.setText("Highest QR Score: " + userToDisplay.getHighestScore().toString());
                break;
        }


        return view;
    }

    // Help with filtering from https://gist.github.com/codinginflow/eec0211b4fab5e5426319389377d71af

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<UserListDisplayContainer> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(allUserDisplays);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (UserListDisplayContainer item : allUserDisplays) {
                    if (item.getUsername().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userDisplays.clear();
            userDisplays.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public Integer getUserPos() {
        return userPos;
    }
}
