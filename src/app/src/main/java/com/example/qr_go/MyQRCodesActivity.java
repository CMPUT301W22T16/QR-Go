package com.example.qr_go;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MyQRCodesActivity extends AppCompatActivity {

    private Button tempButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_codes);

//        TODO: replace this and add OnClickListener for a QRCode list
        tempButton = (Button) findViewById(R.id.bt_temp_qr);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyQRCodesActivity.this, QRInfoActivity.class);
//                QRCode selectedGame = gameDataList.get(position);
//                intent.putExtra("selectedGameID", selectedGame.getID());
                startActivity(intent);
            }
        });

    }
}
