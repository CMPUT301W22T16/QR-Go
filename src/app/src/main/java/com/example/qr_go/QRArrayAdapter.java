package com.example.qr_go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying qr codes in a list
 */
public class QRArrayAdapter extends ArrayAdapter<QRListDisplayContainer> implements Filterable {
    private Context context;
    private Integer sortPos;
    private ArrayList<QRListDisplayContainer> allQrDisplays;
    private ArrayList<QRListDisplayContainer> qrDisplays;

    public QRArrayAdapter(@NonNull Context context, ArrayList<QRListDisplayContainer> qrDisplays, Integer sortPos) {
        super(context, 0, qrDisplays);
        this.context = context;
        this.allQrDisplays = new ArrayList<>(qrDisplays);
        this.qrDisplays = qrDisplays;
        this.sortPos = sortPos;
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

        switch(sortPos) {
            case 0:
                scoreView.setText("Score: " + qrToDisplay.getScore().toString());
                break;
            default:
                Float distance =  qrToDisplay.getDistance();
                String distanceString;
                if (distance > 1000) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    Float distanceInKM = distance/1000;
                    distanceString = df.format(distanceInKM) + "km";
                } else {
                    distanceString = distance.toString() + "m";
                }
                scoreView.setText(distanceString + " away");
                break;
        }

        return view;
    }

    // Help with filtering from https://gist.github.com/codinginflow/eec0211b4fab5e5426319389377d71af

    @Override
    public Filter getFilter() { return qrFilter;}

    private Filter qrFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<QRListDisplayContainer> filteredList = new ArrayList<>();


            filteredList.addAll(allQrDisplays);

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            qrDisplays.clear();
            qrDisplays.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
