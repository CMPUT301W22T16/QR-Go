package com.example.qr_go;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GeoLocation extends Application{
    protected Context currentContext;
    private double longitude;
    private double latitude;
    private Address address;
    private Integer locatedQRId;

    public GeoLocation(double latitude, double longitude) {
        this.currentContext = getApplicationContext();

        Geocoder geocoder = new Geocoder(currentContext, Locale.getDefault());
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.address = addressList.get(0);
        this.longitude=longitude;
        this.latitude=latitude;
    }
//    public GeoLocation(Context context, Address address) {
//        Geocoder geocoder = new Geocoder(currentContext, Locale.getDefault());
//        this.currentContext=context;
//        this.address=address;
//    }


    public Address getAddress() {
        return this.address;
    }

    public double getLongitude() {
        return this.longitude;
    }
    public double getLatitude() {
        return this.latitude;
    }
}
