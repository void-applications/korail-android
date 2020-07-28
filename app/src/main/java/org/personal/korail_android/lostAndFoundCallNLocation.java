package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class lostAndFoundCallNLocation extends AppCompatActivity {
    TextView locationTV;
    TextView callTV;
    ImageView callIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_call_n_location);

        locationTV = findViewById(R.id.locationTV);
        callTV = findViewById(R.id.callTV);
        callIV = findViewById(R.id.call);


        //전화기 모양 아이콘 클릭시 유실물 센터 전화 연결 됨.
        callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }



    }
