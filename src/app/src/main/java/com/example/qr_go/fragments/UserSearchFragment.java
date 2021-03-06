package com.example.qr_go.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.qr_go.R;
import com.example.qr_go.activities.PlayerInfoActivity;
import com.example.qr_go.activities.SearchActivity;
import com.example.qr_go.adapters.UserArrayAdapter;
import com.example.qr_go.comparators.UserListNumScannedComparator;
import com.example.qr_go.comparators.UserListTotalScoreComparator;
import com.example.qr_go.comparators.UserListUniqueQRComparator;
import com.example.qr_go.containers.UserListDisplayContainer;

import java.util.ArrayList;

/**
 * Fragment that displays the results of user searches
 */
public class UserSearchFragment extends SortableFragment {
    private ListView userListView;
    private UserArrayAdapter userAdapter;
    private ArrayList<UserListDisplayContainer> userDisplays;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        userListView = (ListView) view.findViewById(R.id.user_list);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), PlayerInfoActivity.class);
//                intent.putExtra("SELECTED_USER", userDisplays.get(position).getUserid());
                intent.putExtra("SELECTED_USER", userAdapter.getIds().get(position));
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public void retrieveData() {
        userDisplays = new ArrayList<>(SearchActivity.getUserDisplays());
        setSearchFiltering();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateSort(Integer sortPos) {
        switch(sortPos) {
            case 0:
                userDisplays.sort(new UserListTotalScoreComparator());
                break;
            case 1:
                userDisplays.sort(new UserListNumScannedComparator());
                break;
            default:
                userDisplays.sort(new UserListUniqueQRComparator());
                break;
        }
        int rank = 1;
        int userPos = 0;
        for (UserListDisplayContainer user : userDisplays) {
            user.setRankPosition(new Integer(rank));
            if (user.getIsCurrentUser()) {
                userPos = rank-1;
            }
            rank++;
        }
        userAdapter = new UserArrayAdapter(view.getContext(), userDisplays, sortPos);
        userListView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFocus() {
        int userPos = 0;
        int i = 0;
        for (UserListDisplayContainer user : userDisplays) {
            if (user.getIsCurrentUser()) {
                userPos = i;
            }
            i++;
        }
        userListView.setSelection(userPos);
    }

    @Override
    public void updateView() {
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSearchFiltering() {
        SearchActivity.getSearchBar().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
