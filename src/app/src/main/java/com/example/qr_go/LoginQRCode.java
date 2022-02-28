package com.example.qr_go;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;

/**
 * LoginQRCode is the QR code that users can use to login to their account on another device
 */
public class LoginQRCode extends QRCode implements  GeneratesNewQR {
    User user;
    /**
     * On create, convert qr content to hash
     * @param user string content of qr code
     * @throws NoSuchAlgorithmException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LoginQRCode(User user) throws NoSuchAlgorithmException {
        super(user.getUserid()+"\n"+user.getPassword());
        this.user = user;
    }

    /**
     * Validates if login QR code and password is valid
     * @return whether qr code is valid
     */
    public boolean isLoginValid() {
        // TODO need to validate if hash is valid
        return false;
    }

    /***
     * Converts the user's id and password to a QR code
     * @return QR code to display
     */
    @Override
    public Bitmap getQRCode(){
        return encodeToQrCode(user.getUserid()+"\n"+user.getPassword(), 100, 100);
    }
}
