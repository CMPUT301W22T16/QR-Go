package com.example.qr_go;
import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qr_go.activities.MapsActivity;
import com.example.qr_go.activities.MyQRCodesActivity;
import com.example.qr_go.activities.PlayerProfileActivity;
import com.example.qr_go.activities.QRCodeScannerActivity;
import com.example.qr_go.activities.SearchActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapsActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testLaunchSearchActivity() {
        solo.clickOnMenuItem("Search");
        solo.assertCurrentActivity("Not in Search Activity", SearchActivity.class);
    }

    @Test
    public void testLaunchMyQRCodesActivity() {
        solo.clickOnMenuItem("My Codes");
        solo.assertCurrentActivity("Not in My Codes Activity", MyQRCodesActivity.class);
    }

    @Test
    public void testLaunchMapsActivity() {
        solo.clickOnMenuItem("Home");
        solo.assertCurrentActivity("Not in Maps Activity", MapsActivity.class);
    }

    @Test
    public void testLaunchQRCodeScannerActivity() {
        solo.clickOnMenuItem("Scan Code");
        solo.assertCurrentActivity("Not in QR Code Scanner Activity", QRCodeScannerActivity.class);
    }

    @Test
    public void testLaunchPlayerProfileActivity() {
        solo.clickOnMenuItem("My Account");
        solo.assertCurrentActivity("Not in My Account Activity", PlayerProfileActivity.class);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
