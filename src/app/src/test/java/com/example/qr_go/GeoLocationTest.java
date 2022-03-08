package com.example.qr_go;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import android.content.Context;
import android.location.Address;


class GeoLocationTest {
    Context currentContext;
    @Test
    void testCreate() {
        GeoLocation geoLocation = new GeoLocation(53.631611, -113.323975);
        Address address = geoLocation.getAddress();
        String selectedCountry = address.getCountryName();
        assertEquals(selectedCountry, "Canada");
    }

}
