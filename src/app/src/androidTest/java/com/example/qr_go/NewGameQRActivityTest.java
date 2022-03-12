package com.example.qr_go;

import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class NewGameQRActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<NewGameQRActivity> rule = new ActivityTestRule<>(NewGameQRActivity.class, true, true);

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
        // If the geo location checkbox is checked, then geolocation should be record
        solo.clickOnCheckBox(R.id.location_checkbox);
        assertTrue(solo.isCheckBoxChecked(R.id.location_checkbox));

        solo.clickOnButton("Save");
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
