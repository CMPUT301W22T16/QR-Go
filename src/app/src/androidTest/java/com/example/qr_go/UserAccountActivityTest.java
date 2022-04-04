package com.example.qr_go;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.qr_go.objects.QRCode.encodeToQrCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.test.rule.ActivityTestRule;

import com.example.qr_go.activities.MapsActivity;
import com.example.qr_go.activities.PlayerProfileActivity;
import com.example.qr_go.objects.LoginQRCode;
import com.example.qr_go.objects.StatusQRCode;
import com.example.qr_go.objects.User;
import com.example.qr_go.utils.UsernameGenerator;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserAccountActivityTest {
    private Solo solo;
    private String mockUserId = "user-id";
    private String mockPassword = "password";
    private SharedPreferences.Editor editor;

    @Rule
    public ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
        Context context = getInstrumentation().getTargetContext();
        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(User.USER_ID, mockUserId);
        editor.putString(User.USER_PWD, mockPassword);
        editor.apply(); // apply changes
        solo.clickOnMenuItem("My Account");
        solo.assertCurrentActivity("Not in My Account Activity", PlayerProfileActivity.class);
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testGetLoginQRCode() {
        LoginQRCode loginQRCode = new LoginQRCode(mockUserId + "\n" + mockPassword);
        assertTrue(encodeToQrCode(LoginQRCode.QR_IDENTIFIER + mockUserId + "\n" + mockPassword, 800, 800).sameAs(loginQRCode.getQRCode()));
    }

    @Test
    public void testGetStatusQRCode() {
        StatusQRCode statusQRCode = new StatusQRCode(mockUserId + "\n" + mockPassword);
        assertTrue(encodeToQrCode(StatusQRCode.QR_IDENTIFIER + mockUserId + "\n" + mockPassword, 800, 800).sameAs(statusQRCode.getQRCode()));
    }

    @Test
    public void testUserNameEmailChange() {
        String username = UsernameGenerator.generateUsername();
        String email = "test@test.test";
        solo.clearEditText(1);
        solo.clearEditText(0);
        solo.typeText(1, username);
        solo.typeText(0, email);
        solo.clickOnButton("Save");
        solo.clickOnMenuItem("Home");
        solo.clickOnMenuItem("My Account");
        assertTrue(solo.searchText(username));
        assertTrue(solo.searchText(email));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
