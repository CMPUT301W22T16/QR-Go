package com.example.qr_go;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Fragment that displays the results of qr searches
 */

public class QRSearchFragment extends SortableFragment {
    private ListView qrListView;
    private QRArrayAdapter qrAdapter;
    private ArrayList<QRListDisplayContainer> qrDisplays;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        qrListView = (ListView) view.findViewById(R.id.qr_list);
        this.view = view;
        qrListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), QRInfoActivity.class);
                intent.putExtra("QRid", qrDisplays.get(position).getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void retrieveData() {
        qrDisplays = new ArrayList<>(SearchActivity.getQrDisplays());
        setSearchFiltering();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateSort(Integer sortPos) {
        if (sortPos == 0) {
            qrDisplays.sort(new QRListScoreComparator());
        } else {
            // Remove from the list to display if distance is null
            qrDisplays.removeIf(q -> (q.getDistance() == null));
            qrDisplays.sort(new QRListDistanceComparator());
        }
        qrAdapter = new QRArrayAdapter(view.getContext(), qrDisplays, sortPos);
        qrListView.setAdapter(qrAdapter);
        qrAdapter.notifyDataSetChanged();
    }

    // TODO: Implement searching? how tho
    public void setSearchFiltering() {
        SearchActivity.getSearchBar().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
