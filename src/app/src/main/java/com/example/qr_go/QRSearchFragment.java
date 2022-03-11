package com.example.qr_go;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragment that displays the results of qr searches
 */
public class QRSearchFragment extends SortableFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView testTextView = (TextView) view.findViewById(R.id.qrTextView);
    }

    @Override
    public void updateSort(Integer sortPos) {
        TextView testTextView = (TextView) getView().findViewById(R.id.qrTextView);
        testTextView.setText("qr search, sort using" + sortPos);
    }
}
