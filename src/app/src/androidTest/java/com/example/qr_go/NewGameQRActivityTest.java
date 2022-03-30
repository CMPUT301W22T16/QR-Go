package com.example.qr_go;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.app.Activity;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.qr_go.activities.NewGameQRActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class NewGameQRActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<NewGameQRActivity> rule = new ActivityTestRule<>(NewGameQRActivity.class, true, true);
    @Rule
    public GrantPermissionRule coarseLocationRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);
    @Rule
    public GrantPermissionRule fineLocationRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule cameraRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);


    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testRecordGeolocation() {
        int CHECKBOX_INDEX = 0;
        // Checkbox should be checked by default
        assertTrue(solo.isCheckBoxChecked(CHECKBOX_INDEX)); // checked
        // If the geo location checkbox is checked, then geolocation should be record
        solo.clickOnCheckBox(CHECKBOX_INDEX); // click on first checkbox
        assertFalse(solo.isCheckBoxChecked(CHECKBOX_INDEX)); // unchecked
    }

    @Test
    public void testTakePhoto() {
        // When take photo button is clicked, should launch camera
       assertNotNull(solo.searchButton("Take photo of object"));
        // Cannot click button because you lose all control over the camera application
    }

    @Test
    public void testDisplayScore() {
        TextView displayedScore =(TextView)  solo.getView(R.id.score);
        assertEquals("27", displayedScore.getText());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
