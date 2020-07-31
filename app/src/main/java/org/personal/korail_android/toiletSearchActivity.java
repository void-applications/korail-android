package org.personal.korail_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class toiletSearchActivity extends AppCompatActivity {

    EditText stationET;
    ImageView searchIV;
    Handler handler;
    Intent searchIntent;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_search);
        stationET = findViewById(R.id.searchStationET);
        searchIV = findViewById(R.id.searchIconIB);

        handler = new Handler();

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stationName = stationET.getText().toString();

                searchIntent = new Intent(getApplicationContext(), ServiceCenterActivity.class);
                startActivity(searchIntent);
            }
        });

        stationET.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {

                    searchIV.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.lostAndFound);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent toHome = new Intent(this, HomeActivity.class);
                startActivity(toHome);
                break;

            case R.id.event:
                Intent toEvent = new Intent(this, EventListActivity.class);
                startActivity(toEvent);
                break;

            case R.id.chat:
                Intent toChat = new Intent(this, ChatListActivity.class);
                startActivity(toChat);
                break;

            case R.id.facilities:
                Intent toFacilities = new Intent(this, FacilitiesActivity.class);
                startActivity(toFacilities);
                break;
        }
        overridePendingTransition(0, 0);
        return true;
    }
}