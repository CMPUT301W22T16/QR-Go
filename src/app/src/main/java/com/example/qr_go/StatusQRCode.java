package com.example.qr_go;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Map;


/**
 * StatusQRCode allows other plays to scan in order to view the status page of a player
 */
public class StatusQRCode extends QRCode implements GeneratesNewQR {
    static String QR_IDENTIFIER = "status-qr-code-"; // should be pre-pended to all StatusQRCodes
    private String data;

    /**
     * QR code that will go to player info page when scanned
     *
     * @param qrCodeContents text contents scanned from QR code
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public StatusQRCode(String qrCodeContents) {
        super(qrCodeContents);
        data = qrCodeContents;
    }

    /***
     * Converts data text string to a QR code as bitmap
     * @return QR code to display
     */
    @Override
    public Bitmap getQRCode() {
        // Data string is pre-pended with the QR_IDENTIFIER
        String qrString = QR_IDENTIFIER + data;
        return encodeToQrCode(qrString, 800, 800);
    }

    /**
     * Return the data of qr code
     * If data has the QR Identifier then remove it
     */
    public String getData() {
        // Remove the identifier
        return data.replaceFirst("^" + QR_IDENTIFIER, "");
    }
}
