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
import java.util.Arrays;

public class QRSearchSpinnerAdapter extends ArrayAdapter<String> {
    private ArrayList<String> optionsList;
    private Context context;

    public QRSearchSpinnerAdapter(Context context, String[] options) {
        super(context, 0, options);
        optionsList = new ArrayList<>();
        optionsList.addAll(Arrays.asList(options));
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = (TextView) view;
        textView.setText(optionsList.get(position));
        return view;
    }
}
