package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;

/**
 * StatusQRCode allows other plays to scan in order to view the status page of a player
 */
public class StatusQRCode extends QRCode {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public StatusQRCode(String qrCodeContents) throws NoSuchAlgorithmException {
        super(qrCodeContents);
    }
}
