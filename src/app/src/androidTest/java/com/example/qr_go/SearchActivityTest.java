package com.example.qr_go;
import android.app.Activity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
        solo.clickInList(0);  //only works if something in list
        solo.assertCurrentActivity("Not in Player Info Activity", PlayerInfoActivity.class);

    }

    @Test
    public void testSearchQRCodes() {
        solo.clickOnMenuItem("Search");
        solo.assertCurrentActivity("Not in MyQRCode Activity", SearchActivity.class);
        solo.clickOnMenuItem("QR Codes");
        solo.sleep(5000); //allow db to load
        solo.clickInList(0); //only works if something in list
        solo.assertCurrentActivity("Not in QR Info Activity", QRInfoActivity.class);

    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
