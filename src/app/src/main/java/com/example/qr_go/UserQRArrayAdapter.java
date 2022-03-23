package com.example.qr_go;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;

public class UserQRArrayAdapter extends BaseAdapter {
    private HashMap<String, Integer> qrMap = new HashMap<String,Integer>();
    private String[] qrIds;
    public UserQRArrayAdapter(HashMap<String,Integer> qrMap){
        this.qrMap = qrMap;
        this.qrIds = this.qrMap.keySet().toArray(new String[qrMap.size()]);
    }

    @Override
    public int getCount() {
        return this.qrMap.size();
    }

    @Override
    public Object getItem(int position) {
        return qrMap.get(qrIds[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String key = qrIds[position];
        String value = getItem(position).toString();

        return convertView;
    }
}
