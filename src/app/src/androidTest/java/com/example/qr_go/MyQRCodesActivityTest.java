package com.example.qr_go;
import android.app.Activity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
        solo.clickOnButton("QR Code #1");
        solo.assertCurrentActivity("Not in QR Info Activity", QRInfoActivity.class);
        solo.clickOnButton("See other players >");
        solo.assertCurrentActivity("Not in ScannedUsersActivity Activity", ScannedUsersActivity.class);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
