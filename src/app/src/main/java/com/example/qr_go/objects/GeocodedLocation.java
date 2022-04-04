package com.example.qr_go.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The api response from https://nominatim.openstreetmap.org/reverse?lat=53.5&lon=-113.3&format=json
 */

/**
 * Example data
 * {
 * "place_id": 23591814,
 * "licence": "Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright",
 * "osm_type": "way",
 * "osm_id": 98293382,
 * "lat": "53.49909987505078",
 * "lon": "-113.29996962373356",
 * "display_name": "38, 52310 Range Road 232, West Whitecroft, Strathcona County, Alberta, T8B 1B8, Canada",
 * "address": {
 * "house_number": "38",
 * "road": "52310 Range Road 232",
 * "residential": "West Whitecroft",
 * "county": "Strathcona County",
 * "state": "Alberta",
 * "postcode": "T8B 1B8",
 * "country": "Canada",
 * "country_code": "ca"
 * },
 * "boundingbox": [
 * "53.499049875051",
 * "53.499149875051",
 * "-113.30001962373",
 * "-113.29991962373"
 * ]
 * }
 */
public class GeocodedLocation implements Serializable {
    private int place_id;
    private String lat;
    private String lon;
    private String display_name;
    private Address address;
    private ArrayList<String> boundingbox;

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ArrayList<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(ArrayList<String> boundingbox) {
        this.boundingbox = boundingbox;
    }
}
