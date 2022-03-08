package com.example.qr_go;
import android.location.Address;


public class GeoLocation{
    /**
     * Class for GeoLocation, contains location data such as address, latitude, longitude, and the
     * QR ID associated with the specified location.
     * @param locatedQRID
     * @author LPugh
     */

    /*
    we need to compress the images to the database
    we need to be able to load the image into the activity
     */
    private double longitude;
    private double latitude;
    private Address address;
    private Integer locatedQRId;

    public GeoLocation(Integer locatedQRId) {
        this.locatedQRId = locatedQRId;
    }

    public Address getAddress() {
        return this.address;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public Integer getQRId() {
        return this.locatedQRId;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public void setCoords(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
