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
 * Fragment that displays the results of qr searches
 */
public class QRSearchFragment extends SortableFragment {
    private ListView qrListView;
    private ArrayAdapter<QRListDisplayContainer> qrAdapter;
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
                // Start new activity
                /*
                Intent intent = new Intent(view.getContext(), OpenSessionActivity.class);
                intent.putExtra("SELECTED_QR", qrDisplays.get(position).getId());
                view.getContext().startActivity(intent);
                 */
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateSort(Integer sortPos) {
        qrDisplays = SearchActivity.getQrDisplays();
        if (sortPos == 0) {
            qrDisplays.sort(new QRListScoreComparator());
        } else {
            // Nothing so far
        }
        qrAdapter = new QRArrayAdapter(view.getContext(), qrDisplays);
        qrAdapter.notifyDataSetChanged();
        qrListView.setAdapter(qrAdapter);
    }
}
