package com.example.qr_go;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.Hex;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class QRCode {
    protected String hash;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public QRCode(String qrCodeContents) throws NoSuchAlgorithmException {
        // Convert qr code string content to SHA256 hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(qrCodeContents.getBytes(StandardCharsets.UTF_8));
        // Convert bytes to string
        this.hash = Base64.getEncoder().encodeToString(hashBytes);
    }

    public String getHash() {
        return hash;
    }

    public String getId() {
        return hash;
    }

}
