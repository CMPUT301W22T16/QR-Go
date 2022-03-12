package com.example.qr_go;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Fragment that displays the results of user searches
 */
public class UserSearchFragment extends SortableFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView testTextView = (TextView) view.findViewById(R.id.userTextView);
    }
    @Override
    public void updateSort(Integer sortPos) {
        ArrayList<UserListDisplayContainer> userDisplays = SearchActivity.getUserDisplays();
        TextView testTextView = (TextView) getView().findViewById(R.id.userTextView);
        testTextView.setText("user search, sort using: " + sortPos + " \nfound " + userDisplays.size() + "users");
    }
}
