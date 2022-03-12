package com.example.qr_go;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        TextView testTextView = (TextView) view.findViewById(R.id.qrTextView);
        qrListView = (ListView) view.findViewById(R.id.qr_list);
        this.view = view;
    }

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
