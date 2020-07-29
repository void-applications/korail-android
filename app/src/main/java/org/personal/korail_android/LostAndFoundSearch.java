package org.personal.korail_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LostAndFoundSearch extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    EditText stationNameET;
    ImageView searchIV;
    BottomNavigationView bottomNavigation;
    Handler handler;
    String result;
    String centerName;
    String centerNumber;
    String TAG = "유실물 전화 액티비티";

    Intent searchIntent;
    TextView questionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_search);

        stationNameET = findViewById(R.id.searchStation);
        searchIV = findViewById(R.id.searchIV);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        questionTV = findViewById(R.id.questionTV);

        questionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent questionIntent = new Intent(getApplicationContext(), LostAndFoundStepMain.class);
                startActivity(questionIntent);
            }
        });
        handler = new Handler();

        handler=new Handler();

        //돋보기 아이콘 클릭시 유실물 센터 위치 및 전화 걸기 액티비티로 전환, 서버에 역 이름 전송
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stationName = stationNameET.getText().toString();

                searchIntent = new Intent(getApplicationContext(), lostAndFoundCallNLocation.class);
                searchIntent.putExtra("stationName", stationName);
                Log.d(TAG, "인텐트로 보내는 결과값 확인 : "+ stationName);
                startActivity(searchIntent);

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
                Intent toHome = new  Intent(this, HomeActivity.class);
                startActivity(toHome);
                break;

            case R.id.event:
                Intent toEvent = new Intent(this, EventListActivity.class);
                startActivity(toEvent);
                break;

            case R.id.chat:
                Intent toChat  = new Intent(this, ChatListActivity.class);
                startActivity(toChat);
                break;

            case R.id.culturalFacilities:
                Intent toCulturalFacilities = new Intent(this, CulturalFacilitiesListActivity.class);
                startActivity(toCulturalFacilities);
                break;
        }
        overridePendingTransition(0, 0);
        return true;
    }
}


