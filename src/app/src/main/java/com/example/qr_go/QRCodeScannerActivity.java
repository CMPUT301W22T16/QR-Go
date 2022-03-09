package com.example.qr_go;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.common.collect.Maps;
import com.google.zxing.Result;

import java.security.NoSuchAlgorithmException;


/**
 * QRCodeScanner is the activity that scans QR codes
 * Library: https://github.com/yuriy-budiyev/code-scanner
 */
public class QRCodeScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        initializeCamera();

    }

    /**
     * Initialize camera
     * Check that app has camera permissions
     * If not, request for permissions
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeCamera() {
        // Check if app has camera permission
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // No camera access - request permission
            // `java.lang.RuntimeException: Fail to connect to camera service` - need to allow camera permission in emulator settings
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } else {
            // Have camera access
            CodeScannerView scannerView = findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(this, scannerView);

            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            String text = result.getText();
                            try {
                                // Create a new QR code object depending on type of QR code
                                if (text.startsWith(LoginQRCode.QR_IDENTIFIER)) {
                                    LoginQRCode loginQRCode = new LoginQRCode(text);
                                    Toast.makeText(QRCodeScannerActivity.this, "DEBUG LOGIN QR", Toast.LENGTH_SHORT).show();
                                } else if (text.startsWith(StatusQRCode.QR_IDENTIFIER)) {
                                    StatusQRCode statusQRCode = new StatusQRCode(text);
                                    Toast.makeText(QRCodeScannerActivity.this, "DEBUG STATUS QR", Toast.LENGTH_SHORT).show();
                                } else {
                                    // New Game QR code > go to NewGameQRActivity
                                    Intent intent = new Intent(QRCodeScannerActivity.this, NewGameQRActivity.class);
                                    intent.putExtra("QR", text);
                                    startActivity(intent);
                                }
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeScanner.startPreview();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner != null) mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if (mCodeScanner != null) mCodeScanner.releaseResources();
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                initializeCamera(); // re-initialize camera
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                // exit and go back to previous activity
                finish();
            }
        }
    }

}