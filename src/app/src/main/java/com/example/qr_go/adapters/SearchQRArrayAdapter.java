package com.example.qr_go.adapters;
import android.content.Context;
import androidx.annotation.NonNull;
import com.example.qr_go.containers.QRListDisplayContainer;
import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying qr codes in a list
 */
public class SearchQRArrayAdapter extends QRArrayAdapter {
    private ArrayList<QRListDisplayContainer> allQrDisplays;
    private ArrayList<QRListDisplayContainer> qrDisplays;
    private Integer sortPos;
    private SearchQRArrayAdapter adapter;
    private final int localDistanceBoundary = 10000;

    public SearchQRArrayAdapter(Context context, ArrayList<QRListDisplayContainer> qrDisplays, Integer sortPos) {
        super(context,qrDisplays,sortPos);
        this.context = context;
        this.adapter = this;
        this.allQrDisplays = new ArrayList<>(qrDisplays);
        this.qrDisplays = qrDisplays;
        this.sortPos = sortPos;
    }

    public void filter(int filterOption) {
        qrDisplays.clear();
        if (filterOption == 1) {
            for (QRListDisplayContainer qr : allQrDisplays) {
                if (qr.getDistance() != null && qr.getDistance() <= localDistanceBoundary) {
                    qrDisplays.add(qr);
                }
            }
        } else {
            qrDisplays.addAll(allQrDisplays);
        }
        notifyDataSetChanged();
    }
}