package org.personal.korail_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        searchIV = findViewById(R.id.searchIconIB);
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

        //돋보기 아이콘 클릭시 유실물 센터 위치 및 전화 걸기 액티비티로 전환, 서버에 역 이름 전송
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stationName = stationNameET.getText().toString();

                searchIntent = new Intent(getApplicationContext(), lostAndFoundCallNLocation.class);
                searchIntent.putExtra("stationName", stationName);
                Log.d(TAG, "인텐트로 보내는 결과값 확인 : " + stationName);
                startActivity(searchIntent);

            }
        });

        stationNameET.setOnKeyListener(new View.OnKeyListener() {


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
                finish();
                break;

            case R.id.hiddenRestArea:
                Intent toHiddenRestArea = new Intent(this, HiddenRestAreaListActivity.class);
                startActivity(toHiddenRestArea);
                finish();
                break;

            case R.id.chat:
                Intent toChat = new Intent(this, ChatListActivity.class);
                startActivity(toChat);
                finish();
                break;

            case R.id.facilities:
                Intent toFacilities = new Intent(this, FacilitiesActivity.class);
                startActivity(toFacilities);
                finish();
                break;
        }
        overridePendingTransition(0, 0);
        return true;
    }
}


