package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.personal.korail_android.background.OkHttp;

public class lostAndFoundSearch extends AppCompatActivity {

    EditText stationNameET;
    ImageView searchIV;
    Handler handler;
    String result;
    String centerName;
    String centerNumber;
    String TAG = "유실물 전화 액티비티";

    Intent searchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_search);

        stationNameET = findViewById(R.id.searchStation);
        searchIV = findViewById(R.id.searchIV);
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
}


