package com.example.qr_go;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qr_go.activities.MapsActivity;
import com.example.qr_go.activities.MyQRCodesActivity;
import com.example.qr_go.activities.PlayerProfileActivity;
import com.example.qr_go.activities.QRCodeScannerActivity;
import com.example.qr_go.activities.SearchActivity;
import com.example.qr_go.objects.User;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapsActivityTest {
    private Solo solo;
    private String mockUserId = "user-id";
    private String mockPassword = "password";
    private SharedPreferences.Editor editor;

    @Rule
    public ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        Context context = getInstrumentation().getTargetContext();
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(User.USER_ID, mockUserId);
        editor.putString(User.USER_PWD, mockPassword);
        editor.commit(); // apply changes

        rule.launchActivity(null); // start activity after shared preferences is added
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

    /**
     * Test that getUserId will get the user's id stored in sharedPreferences
     */
    @Test
    public void testGetUserId() {
        String userId = MapsActivity.getUserId();
        assertNotNull(userId);
    }

    /**
     * Test that getPassword will get the user's password stored in sharedPreferences
     */
    @Test
    public void testGetPassword() {
        String password = MapsActivity.getPassword();
        assertNotNull(password);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
