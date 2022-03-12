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

/**
 * Custom ArrayAdapter for displaying qr codes in a list
 */
public class QRArrayAdapter extends ArrayAdapter<QRListDisplayContainer> {
    private Context context;
    private ArrayList<QRListDisplayContainer> qrDisplays;

    public QRArrayAdapter(@NonNull Context context, ArrayList<QRListDisplayContainer> qrDisplays) {
        super(context, 0, qrDisplays);
        this.context = context;
        this.qrDisplays = qrDisplays;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        QRListDisplayContainer qrToDisplay = qrDisplays.get(position);

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.qr_list_content, parent, false);
        }

        TextView idView = view.findViewById(R.id.qr_id_view);
        TextView scoreView = view.findViewById(R.id.qr_score_view);

        idView.setText(qrToDisplay.getId().substring(0, 8));
        scoreView.setText(qrToDisplay.getScore().toString());
        
        return view;
    }
}
