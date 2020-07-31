package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.personal.korail_android.background.OkHttp;

public class ServiceCenterActivity extends AppCompatActivity {

    TextView locationTV;
    TextView callNumberTV;
    TextView stationNameTV;
    String number;
    String stationName;
    String TAG= "고객센터 상세 페이지";
    String result;
    String centerNumber;
    String centerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        stationNameTV = findViewById(R.id.stationNameTV);
        locationTV = findViewById(R.id.centerLocationTV);
        callNumberTV = findViewById(R.id.numberTV);

        callNumberTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
                startActivity(callIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent getInfoIntent = getIntent();
        stationName = getInfoIntent.getStringExtra("stationName");
        Log.d(TAG, "인텐트로 받은 결과값 확인 : " + stationName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttpThread = new OkHttp();
                result=okHttpThread.getData("http://52.79.146.35/service_center/?station_name="+stationName);
                Log.d(TAG, "결과값 확인 : "+ result);
                centerName = result.split("#")[0];
                Log.d(TAG, "센터 이름 확인 : "+ centerName);
                centerNumber = result.split("#")[1];
                Log.d(TAG, "센터 전화번호 확인 : "+ centerNumber);
                callNumberTV.setText(centerNumber);
                locationTV.setText(centerName);
                stationNameTV.setText(stationName);

                number = centerNumber;
                Log.d(TAG, "연락 전화번호 확인 : "+ number);

            }
        }).start();
    }
}