package com.example.qr_go.objects;

import java.io.Serializable;
import java.text.DecimalFormat;

public class GeoLocation implements Serializable {
    /**
     * Class for GeoLocation, contains location data such as address, latitude, longitude, and the
     * QR ID associated with the specified location.
     *
     * @param locatedQRID
     * @author LPugh
     */

    private double longitude;
    private double latitude;
    private String address;
    private Long score;
    private String locatedQRId;

    public GeocodedLocation getGeocodedLocation() {
        return geocodedLocation;
    }

    public void setGeocodedLocation(GeocodedLocation geocodedLocation) {
        this.geocodedLocation = geocodedLocation;
    }

    private GeocodedLocation geocodedLocation;

    public GeoLocation(String locatedQRId) {

        this.locatedQRId = locatedQRId;
    }
    private void calculateAddress() {
        if (geocodedLocation == null) {
            // Return lat lon if geocodedLocation is not set
            DecimalFormat df = new DecimalFormat("#.###"); // 3 decimal places
            this.address = df.format(latitude) + ", " + df.format(longitude);
        } else {
            this.address = geocodedLocation.getDisplay_name();
        }
    }

    public GeoLocation() {

    }

    public String getAddress() {
        calculateAddress();
        return this.address;
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

    public void setCoords(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
