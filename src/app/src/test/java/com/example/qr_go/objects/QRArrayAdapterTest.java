package com.example.qr_go.objects;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_go.adapters.SearchQRArrayAdapter;
import com.example.qr_go.adapters.UserQRArrayAdapter;
import com.example.qr_go.containers.QRListDisplayContainer;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests QRArrayAdapter objects. Currently not functional as unable to mock context objects.
 */
public class QRArrayAdapterTest{
    private ArrayList<QRListDisplayContainer> qrDisplays = new ArrayList<QRListDisplayContainer>();
    private QRListDisplayContainer qrDispCont1 = new QRListDisplayContainer(123, "1234", null, null, null, null, null);

    UserQRArrayAdapter userQRAdapter;
    SearchQRArrayAdapter searchQRAdapter;
    @Test
    public void TestUserQRConstructor() {
        for(int i = 0; i < 10; i++) {
            qrDisplays.add(new QRListDisplayContainer(i+20, Integer.toString(i+10), null, null, null, null, null));
        }
        qrDisplays.add(qrDispCont1);
        userQRAdapter = new UserQRArrayAdapter(null, qrDisplays,0);
        for(int i = 0; i < 10; i++) {
            assertEquals(userQRAdapter.getIds().get(i), Integer.toString(i+10));
            assertEquals(userQRAdapter.getScores().get(i), (Integer)(i+20));
        }
    }

    @Test
    public void TestSearchQRConstructor() {
        for(int i = 0; i < 10; i++) {
            qrDisplays.add(new QRListDisplayContainer(i+20, Integer.toString(i+10), null, null, null, null, null));
        }
        qrDisplays.add(qrDispCont1);
        searchQRAdapter = new SearchQRArrayAdapter(null, qrDisplays,0);
        for(int i = 0; i < 10; i++) {
            assertEquals(searchQRAdapter.getIds().get(i), Integer.toString(i+10));
            assertEquals(searchQRAdapter.getScores().get(i), (Integer)(i+20));
        }
    }
}
