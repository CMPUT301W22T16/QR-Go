package com.example.qr_go.objects;

import android.location.Address;

import java.text.DecimalFormat;

public class GeoLocation {
    /**
     * Class for GeoLocation, contains location data such as address, latitude, longitude, and the
     * QR ID associated with the specified location.
     *
     * @param locatedQRID
     * @author LPugh
     */

    private double longitude;
    private double latitude;
    private Long score;
    private String address;
    private String locatedQRId;

    public GeoLocation(String locatedQRId) {

        this.locatedQRId = locatedQRId;
    }

    public GeoLocation() {

    }

    public String getAddress() {
        if (address == null) {
            // Return lat lon if address is not set
            DecimalFormat df = new DecimalFormat("#.###"); // 3 decimal places
            return df.format(latitude) + ", " + df.format(longitude);
        }
        return address;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public String getQRId() {
        return this.locatedQRId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCoords(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
