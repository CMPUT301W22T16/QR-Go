package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;

/**
 * LoginQRCode is the QR code that users can use to login to their account on another device
 */
public class LoginQRCode extends QRCode {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LoginQRCode(String qrCodeContents) throws NoSuchAlgorithmException {
        super(qrCodeContents);
    }

    public boolean isLoginValid() {
        // TODO need to validate if hash is valid
        return false;
    }
}
