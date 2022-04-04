package com.example.qr_go;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qr_go.activities.MapsActivity;
import com.example.qr_go.activities.PlayerInfoActivity;
import com.example.qr_go.activities.QRInfoActivity;
import com.example.qr_go.activities.ScannedUsersActivity;
import com.example.qr_go.activities.SearchActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchActivityTest {
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
    public void testSearchPlayers() {
        solo.clickOnMenuItem("Search");
        solo.assertCurrentActivity("Not in MyQRCode Activity", SearchActivity.class);
        solo.clickOnMenuItem("Players");
        solo.sleep(5000); //allow db to load
        while (solo.scrollUp()){
            solo.scrollUp(); // scroll to top
        }
        solo.clickInList(0);  //only works if something in list
        solo.assertCurrentActivity("Not in Player Info Activity", PlayerInfoActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Not in QR Info Activity", QRInfoActivity.class);
        solo.clickOnButton("See other players >");
        solo.assertCurrentActivity("Not in Scanned Users Activity", ScannedUsersActivity.class);

    }

    @Test
    public void testSearchQRCodes() {
        solo.clickOnMenuItem("Search");
        solo.assertCurrentActivity("Not in MyQRCode Activity", SearchActivity.class);
        solo.clickOnMenuItem("QR Codes");
        solo.sleep(5000); //allow db to load
        solo.clickInList(0); //only works if something in list
        solo.assertCurrentActivity("Not in QR Info Activity", QRInfoActivity.class);
        solo.clickOnButton("See other players >");
        solo.assertCurrentActivity("Not in Scanned Users Activity", ScannedUsersActivity.class);

    }

    @Test
    public void testSearchQRCodesSorter() {
        solo.clickOnMenuItem("Search");
        solo.assertCurrentActivity("Not in MyQRCode Activity", SearchActivity.class);
        solo.clickOnMenuItem("QR Codes");
        solo.pressSpinnerItem(0, 1); // Proximity
        solo.sleep(5000); //allow db to load
        assertTrue(solo.searchText("away"));
        solo.clickInList(0); //only works if something in list
        solo.assertCurrentActivity("Not in QR Info Activity", QRInfoActivity.class);
        solo.clickOnButton("See other players >");
        solo.assertCurrentActivity("Not in Scanned Users Activity", ScannedUsersActivity.class);

    }

    @Test
    public void testSearchPlayersSorter() {
        solo.clickOnMenuItem("Search");
        solo.assertCurrentActivity("Not in MyQRCode Activity", SearchActivity.class);
        solo.clickOnMenuItem("Players");
        solo.sleep(5000); //allow db to load
        assertTrue(solo.searchText("Total Score"));
        solo.pressSpinnerItem(0, 1); // # QR Codes
        assertTrue(solo.searchText("scanned"));
        solo.pressSpinnerItem(0, 2); // Unique Score
        assertTrue(solo.searchText("Highest QR Score"));
        solo.clickInList(0);  //only works if something in list
        solo.assertCurrentActivity("Not in Player Info Activity", PlayerInfoActivity.class);

    }

    @Test
    public void testSearchPlayersSearchUsername() {
        solo.clickOnMenuItem("Search");
        solo.assertCurrentActivity("Not in MyQRCode Activity", SearchActivity.class);
        solo.clickOnMenuItem("Players");
        solo.sleep(5000); //allow db to load
        solo.typeText(0, "FuzzyWorm3924");
        solo.clickInList(0);  //only works if something in list
        solo.assertCurrentActivity("Not in Player Info Activity", PlayerInfoActivity.class);

    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
