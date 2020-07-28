package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.personal.korail_android.background.OkHttp;

public class lostAndFoundCallNLocation extends AppCompatActivity {
    TextView locationTV;
    TextView callTV;
    ImageView callIV;

    String number;
    String centerNumber;
    String centerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_call_n_location);

        locationTV = findViewById(R.id.locationTV);
        callTV = findViewById(R.id.callTV);
        callIV = findViewById(R.id.call);

        Intent getInfoIntent = getIntent();
        centerName = getInfoIntent.getStringExtra("centerName");
        centerNumber = getInfoIntent.getStringExtra("centerNumber");

        callTV.setText(centerNumber);
        locationTV.setText(centerName);
        number = centerNumber;

        //전화기 모양 아이콘 클릭시 유실물 센터 전화 연결 됨.
        callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
                startActivity(callIntent);

            }
        });

    }
    }
