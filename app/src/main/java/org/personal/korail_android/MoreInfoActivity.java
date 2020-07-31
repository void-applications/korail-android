package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MoreInfoActivity extends AppCompatActivity {


    ImageView eventIV,facilityIV,nursingRoomIV,restroomIV,serviceCenterIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        eventIV=findViewById(R.id.eventIV);
        facilityIV=findViewById(R.id.facilityIV);
        nursingRoomIV=findViewById(R.id.nursingRoomIV);
        restroomIV=findViewById(R.id.restroomIV);
        serviceCenterIV=findViewById(R.id.serviceCenterIV);

        eventIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),EventListActivity.class);
                startActivity(intent);
            }
        });

        facilityIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CulturalFacilitiesListActivity.class);
                startActivity(intent);
            }
        });

        nursingRoomIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),NursingRoomListActivity.class);
                startActivity(intent);
            }
        });

        restroomIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ToiletActivity.class);
                startActivity(intent);
            }
        });

        serviceCenterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ServiceCenterSearchActivity.class);
                startActivity(intent);
            }
        });

    }
}