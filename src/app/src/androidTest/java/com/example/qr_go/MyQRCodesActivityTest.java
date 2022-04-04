package com.example.qr_go;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qr_go.activities.MapsActivity;
import com.example.qr_go.activities.MyQRCodesActivity;
import com.example.qr_go.activities.PlayerInfoActivity;
import com.example.qr_go.activities.QRInfoActivity;
import com.example.qr_go.activities.ScannedUsersActivity;
import com.example.qr_go.activities.SearchActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MyQRCodesActivityTest {
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
    public void testMyQRCodes() {
        solo.clickOnMenuItem("My Codes");
        solo.assertCurrentActivity("Not in MyQRCode Activity", MyQRCodesActivity.class);

    }

    @Test
    public void testPlayerProfile() {
        // going through search ensures player will have QR codes associated with account
        solo.clickOnMenuItem("Search");
        solo.assertCurrentActivity("Not in MyQRCode Activity", SearchActivity.class);
        solo.clickOnMenuItem("Players");
        solo.sleep(5000); //allow db to load
        solo.typeText(0, "FuzzyWorm3924");
        solo.clickInList(0);  //only works if something in list
        solo.assertCurrentActivity("Not in Player Info Activity", PlayerInfoActivity.class);
        assertTrue(solo.searchText("Highest Score"));
        assertTrue(solo.searchText("Lowest Score"));
        assertTrue(solo.searchText("Total Score"));
        assertTrue(solo.searchText("QR Codes"));
        solo.clickInList(0);  //only works if something in list
        solo.assertCurrentActivity("Not in QR Info Activity", QRInfoActivity.class);
        solo.goBack();
        solo.clickOnText("ebffaaaa"); // hardcoded for highest QR
        solo.assertCurrentActivity("Not in QR Info Activity", QRInfoActivity.class);

    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
