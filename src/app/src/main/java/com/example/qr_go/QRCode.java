package com.example.qr_go;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.Hex;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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
    private boolean isInvisable;
    /**
     * QR code
     *
     * @param qrCodeContents text contents scanned from QR code
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public QRCode(String qrCodeContents) {
        // https://stackoverflow.com/a/5531479
        // Convert qr code string content to SHA256 hash
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(qrCodeContents.getBytes(StandardCharsets.UTF_8));
            // Convert bytes to string
            this.hash = bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            this.hash = null;
        }
        boolean isInvisable = false;

    }
    public QRCode(){boolean isInvisable = false; }

    /**
     * Converts bytes[] from SHA-256 hashing into string
     *
     * @param bytes from SHA-256 hashing
     * @return hex string representation of bytes array
     */
    private String bytesToHex(byte[] bytes) {
        // https://stackoverflow.com/a/32943539
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    /**
     * Getter for hash
     *
     * @return hash string
     */
    public String getHash() {
        return hash;
    }

    /**
     * Get id of QR code
     *
     * @return id of QR code
     */
    public String getId() {
        return hash;
    }

    /**
     * Converts plain text to a QR code
     * https://stackoverflow.com/a/27010646
     *
     * @param text   text to encode as QR code
     * @param width  width of QR code
     * @param height height of QR code
     * @return QR code Bitmap
     */
    protected static Bitmap encodeToQrCode(String text, int width, int height) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 100, 100);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
    public Boolean isDeleted(){ return isInvisable;}
    public void deleteQR(){isInvisable = true;}
}
