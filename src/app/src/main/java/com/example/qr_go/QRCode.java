package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.Hex;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * QRCode is the super class from which GameQRCode and LoginQRCode subclasses extend from
 * This class will take the string contents of a QR code and convert it into a SHA256 hash
 * This hash is stored into the protected variable `hash`
 */
public class QRCode {
    protected String hash;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public QRCode(String qrCodeContents) throws NoSuchAlgorithmException {
        // https://stackoverflow.com/a/5531479
        // Convert qr code string content to SHA256 hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(qrCodeContents.getBytes(StandardCharsets.UTF_8));
        // Convert bytes to string
        this.hash = bytesToHex(hashBytes);
    }
    private String bytesToHex(byte[] bytes) {
        // https://stackoverflow.com/a/32943539
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public String getHash() {
        return hash;
    }

    public String getId() {
        return hash;
    }

}
