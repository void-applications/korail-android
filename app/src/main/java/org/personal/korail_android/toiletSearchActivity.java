package org.personal.korail_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class toiletSearchActivity extends AppCompatActivity {

    EditText stationET;
    ImageView searchIV;
    Intent searchIntent;
    String TAG = "화장실 찾기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_search);
        stationET = findViewById(R.id.toiletStationET);
        searchIV = findViewById(R.id.searchIconIB);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "엔터 클릭 실행" );

                String stationName = stationET.getText().toString();
                Log.d(TAG, "역 확인 : " + stationName);
                searchIntent = new Intent(getApplicationContext(), ToiletActivity.class);
                searchIntent.putExtra("stationName", stationName);
                startActivity(searchIntent);
            }
        });

        stationET.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d(TAG, "엔터 클릭 onkey이벤트" );

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
    }


}