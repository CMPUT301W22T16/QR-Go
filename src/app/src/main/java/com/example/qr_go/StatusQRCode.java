package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;

public class StatusQRCode extends QRCode {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public StatusQRCode(String qrCodeContents) throws NoSuchAlgorithmException {
        super(qrCodeContents);
    }
}
