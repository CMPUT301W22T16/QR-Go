package com.example.qr_go;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;


/**
 * StatusQRCode allows other plays to scan in order to view the status page of a player
 */
public class StatusQRCode extends QRCode implements  GeneratesNewQR {
    static String QR_IDENTIFIER = "status-qr-code-"; // should be pre-pended to all StatusQRCodes
    User user;

    /**
     * Status QR Code
     * @param user user to track status of
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public StatusQRCode(User user)  {
        super(user.getUserid());
        this.user = user;
    }

    /**
     * QR code
     * @param qrCodeContents text contents scanned from QR code
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public StatusQRCode(String qrCodeContents)  {
        super(qrCodeContents);
    }

    /***
     * Converts the user's id and password to a QR code
     * @return QR code to display
     */
    @Override
    public Bitmap getQRCode() {
        return encodeToQrCode(user.getUserid(), 100, 100);
    }
}