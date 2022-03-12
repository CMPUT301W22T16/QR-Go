package com.example.qr_go;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Fragment that displays the results of user searches
 */
public class UserSearchFragment extends SortableFragment {
    private ListView userListView;
    private ArrayAdapter<UserListDisplayContainer> userAdapter;
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
                // Start new activity
                /*
                Intent intent = new Intent(view.getContext(), OpenSessionActivity.class);
                intent.putExtra("SELECTED_USER", userDisplays.get(position).getUserid());
                view.getContext().startActivity(intent);
                 */
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateSort(Integer sortPos) {
        userDisplays = SearchActivity.getUserDisplays();
        switch(sortPos) {
            case 0:
                userDisplays.sort(new UserListTotalScoreComparator());
                break;
            case 1:
                userDisplays.sort(new UserListNumScannedComparator());
                break;
            default:
                // Nothing else for now
                userDisplays.sort(new UserListTotalScoreComparator());
                break;
        }
        userAdapter = new UserArrayAdapter(view.getContext(), userDisplays, sortPos);
        userAdapter.notifyDataSetChanged();
        userListView.setAdapter(userAdapter);
    }
}
