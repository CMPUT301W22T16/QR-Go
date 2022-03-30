package com.example.qr_go.objects;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoLocationTest {
    String mockQRId = "test-id";
    @Test
    public void checkConstructor() {
         GeoLocation testGeoLocation = new GeoLocation(mockQRId);
         assertEquals(testGeoLocation.getQRId(), mockQRId);
    }
}
