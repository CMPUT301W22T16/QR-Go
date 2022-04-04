package com.example.qr_go.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_go.R;
import com.example.qr_go.activities.BaseActivity;
import com.example.qr_go.activities.SearchActivity;
import com.example.qr_go.containers.QRListDisplayContainer;
import com.example.qr_go.adapters.SearchQRArrayAdapter;
import com.example.qr_go.adapters.UserQRArrayAdapter;
import com.example.qr_go.utils.QRGoDBUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;


public abstract class QRArrayAdapter extends ArrayAdapter<QRListDisplayContainer>{
    protected Context context;
    private Integer sortPos;
    private ArrayList<QRListDisplayContainer> allQrDisplays;
    private ArrayList<QRListDisplayContainer> qrDisplays;
    private QRArrayAdapter adapter;



    public QRArrayAdapter(@NonNull Context context, ArrayList<QRListDisplayContainer> qrDisplays, Integer sortPos) {
        super(context, 0, qrDisplays);
        this.adapter = this;
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

        Button delButton = (Button) view.findViewById(R.id.qr_del_button);
        TextView idView = view.findViewById(R.id.qr_id_view);
        TextView scoreView = view.findViewById(R.id.qr_score_view);
        TextView hoodView = view.findViewById(R.id.qr_hood_view);
        ImageView imageView = view.findViewById(R.id.qr_picture);

        if (qrToDisplay.getPicture() != null) {
            imageView.setImageBitmap(qrToDisplay.getPicture());
        } else {
            imageView.setImageResource(R.drawable.round_qr_code_scanner_24);
        }
        if (qrToDisplay.getNeighborhood() != null) {
            hoodView.setText(qrToDisplay.getNeighborhood() + ", " + qrToDisplay.getCity());
            hoodView.setVisibility(View.VISIBLE);
        } else {
            hoodView.setVisibility(View.INVISIBLE);
        }


        idView.setText(qrToDisplay.getId().substring(0, 8) + "...");

        // Show the delete button if the current user is an owner
        AddDeleteButton(scoreView,delButton,qrToDisplay);
        switch(sortPos) {
            case 0:
                scoreView.setText("Score:\n" + qrToDisplay.getScore().toString());
                break;
            default:
                Float distance =  qrToDisplay.getDistance();
                String distanceString;
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                if (distance > 1000) {
                    Float distanceInKM = distance/1000;
                    distanceString = df.format(distanceInKM) + "km";
                } else {
                    distanceString = df.format(distance) + "m";
                }
                scoreView.setText(distanceString + "\naway");
                break;
        }

        return view;
    }

    protected void AddDeleteButton(TextView scoreView, Button delButton, QRListDisplayContainer qrToDisplay) {
        RelativeLayout.LayoutParams scoreParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if (SearchActivity.getUserOwner()) {
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
                    SearchActivity activity = (SearchActivity) context;
                    activity.deleteQR(qrToDisplay);
                    adapter.notifyDataSetChanged();
                    db.deleteGameQRFromDB(qrid);
                }
            });
        } else {
            scoreParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            delButton.setVisibility(View.GONE);
        }
        scoreParams.addRule(RelativeLayout.CENTER_VERTICAL);
        scoreView.setLayoutParams(scoreParams);
    }

    public ArrayList<String> getIds() {
        ArrayList<String> returnList = new ArrayList<String>();
        for(int i = 0; i < qrDisplays.size(); i++) {
            returnList.add(qrDisplays.get(i).getId());
        }
        return returnList;

    }

    public ArrayList<Integer> getScores() {
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        for(int i = 0; i < qrDisplays.size(); i++) {
            returnList.add(qrDisplays.get(i).getScore());
        }
        return returnList;

    }

}
