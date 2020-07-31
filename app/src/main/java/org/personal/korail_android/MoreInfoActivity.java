package org.personal.korail_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class MoreInfoActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    ImageView eventIV,facilityIV,nursingRoomIV,restroomIV,serviceCenterIV;
    BottomNavigationView bottomNavigationBV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        eventIV=findViewById(R.id.eventIV);
        facilityIV=findViewById(R.id.facilityIV);
        nursingRoomIV=findViewById(R.id.nursingRoomIV);
        restroomIV=findViewById(R.id.restroomIV);
        serviceCenterIV=findViewById(R.id.serviceCenterIV);
        bottomNavigationBV = findViewById(R.id.bottomNavigationBV);

        bottomNavigationBV.setOnNavigationItemSelectedListener(this);

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
                Intent intent=new Intent(getApplicationContext(),toiletSearchActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationBV.setSelectedItemId(R.id.facilities);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent toHome = new Intent(this, HomeActivity.class);
                toHome.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(toHome);
                break;

            case R.id.hiddenRestArea:
                Intent toHiddenRestArea = new Intent(this, HiddenRestAreaListActivity.class);
                toHiddenRestArea.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(toHiddenRestArea);
                break;

            case R.id.chat:
                Intent toChat = new Intent(this, ChatListActivity.class);
                toChat.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(toChat);
                break;

            case R.id.lostAndFound:
                Intent toLostAndFound = new Intent(this, LostAndFoundSearch.class);
                toLostAndFound.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(toLostAndFound);
                break;
        }
        overridePendingTransition(0, 0);
        return true;
    }
}