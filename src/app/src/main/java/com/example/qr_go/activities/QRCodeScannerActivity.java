package com.example.qr_go.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.qr_go.R;
import com.example.qr_go.objects.LoginQRCode;
import com.example.qr_go.objects.StatusQRCode;
import com.example.qr_go.objects.User;
import com.google.zxing.Result;

import java.util.function.Consumer;


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
                            // Create a new QR code object depending on type of QR code
                            if (text.startsWith(LoginQRCode.QR_IDENTIFIER)) {
                                Toast.makeText(QRCodeScannerActivity.this, "Validating login QR\nPlease wait", Toast.LENGTH_LONG).show(); // show loading
                                LoginQRCode loginQRCode = new LoginQRCode(text);
                                Consumer<String> successCb = (String msg) -> {
                                    // Change user to the new user in shared preferences
                                    SharedPreferences prefs = getApplication().getSharedPreferences(User.CURRENT_USER, MODE_PRIVATE);
                                    SharedPreferences.Editor ed = prefs.edit();
                                    ed.putString(User.USER_ID, loginQRCode.getUserId());
                                    ed.putString(User.USER_PWD, loginQRCode.getPassword());
                                    ed.apply(); // apply changes
                                    Toast.makeText(getApplicationContext(), "Switched player accounts", Toast.LENGTH_LONG).show();
                                    // Go to account activity
                                    startActivity(new Intent(QRCodeScannerActivity.this, PlayerProfileActivity.class));
                                };
                                Consumer<String> failureCb = (String msg) -> {
                                    Toast.makeText(QRCodeScannerActivity.this, msg, Toast.LENGTH_LONG).show(); // show error
                                };

                                loginQRCode.isLoginValid(successCb, failureCb);
                            } else if (text.startsWith(StatusQRCode.QR_IDENTIFIER)) {
                                StatusQRCode statusQRCode = new StatusQRCode(text);
                                // Go to the player's profile
                                Intent intent = new Intent(QRCodeScannerActivity.this, PlayerInfoActivity.class);
                                intent.putExtra("SELECTED_USER", statusQRCode.getData());
                                startActivity(intent);
                            } else {
                                // New Game QR code > go to NewGameQRActivity
                                Intent intent = new Intent(QRCodeScannerActivity.this, NewGameQRActivity.class);
                                intent.putExtra("QR", text);
                                startActivity(intent);
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

    /**
     * After resuming, start the codeScanner if it is defined
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner != null) mCodeScanner.startPreview();
    }

    /**
     * If pausing, then release the resources of the code scanner
     */
    @Override
    protected void onPause() {
        if (mCodeScanner != null) mCodeScanner.releaseResources();
        super.onPause();
    }

    /**
     * Called after user has accepted/denied permissions
     * If camera permission is granted, then start the code scanner and camera
     * If permission is denied, exit the activity, and show an error toast
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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