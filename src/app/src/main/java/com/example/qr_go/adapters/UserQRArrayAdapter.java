package com.example.qr_go.adapters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.qr_go.R;
import com.example.qr_go.activities.MyQRCodesActivity;
import com.example.qr_go.containers.QRListDisplayContainer;
import com.example.qr_go.utils.QRGoDBUtil;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying qr codes in a list
 */
public class UserQRArrayAdapter extends QRArrayAdapter {
    private ArrayList<QRListDisplayContainer> allQrDisplays;
    private ArrayList<QRListDisplayContainer> qrDisplays;
    private UserQRArrayAdapter adapter;

    public UserQRArrayAdapter(@NonNull Context context, ArrayList<QRListDisplayContainer> qrDisplays, Integer sortPos) {
        super(context,qrDisplays,sortPos);
        this.qrDisplays = qrDisplays;
        this.adapter = this;
        this.allQrDisplays = new ArrayList<>(qrDisplays);
    }
    
    @Override
    protected void AddDeleteButton(TextView scoreView, Button delButton, QRListDisplayContainer qrToDisplay) {
        RelativeLayout.LayoutParams scoreParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            scoreParams.addRule(RelativeLayout.LEFT_OF, R.id.qr_del_button);
            delButton.setVisibility(View.VISIBLE);
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // This is the id of the qr being deleted
                    String qrid = qrToDisplay.getId();
                    QRGoDBUtil db = new QRGoDBUtil();
                    qrDisplays.remove(qrToDisplay);
                    allQrDisplays.remove(qrToDisplay);
                    MyQRCodesActivity activity = (MyQRCodesActivity) context;
                    activity.deleteQR(qrToDisplay);
                    adapter.notifyDataSetChanged();
                    db.deleteGameQRFromDB(qrid);
                }
            });
        scoreParams.addRule(RelativeLayout.CENTER_VERTICAL);
        scoreView.setLayoutParams(scoreParams);
    }
    
}