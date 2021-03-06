package com.example.qr_go.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_go.R;

import java.util.ArrayList;
import java.util.Arrays;

public class QRSearchSpinnerAdapter extends ArrayAdapter<String> {
    private ArrayList<String> optionsList;
    private Context context;

    public QRSearchSpinnerAdapter(Context context, ArrayList<String> options) {
        super(context, 0, options);
        optionsList = options;
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
            view = LayoutInflater.from(context).inflate(R.layout.qr_search_spinner_item, parent, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.qr_search_option_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.qr_search_option_icon);
        textView.setText(optionsList.get(position));
        if (position == 0) {
            imageView.setImageResource(R.drawable.ic_baseline_public_24);
        } else {
            imageView.setImageResource(R.drawable.ic_baseline_my_location_24);
        }
        return view;
    }
}