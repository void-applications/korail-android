package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.personal.korail_android.background.OkHttp;

public class lostAndFoundCallNLocation extends AppCompatActivity {
    TextView locationTV;
    TextView callTV;
    ImageView callIV;
    String stationName;
    String number;
    String centerNumber;
    String centerName;
    String TAG = "유실물 센터 정보 제공 액티비티";

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_call_n_location);

        locationTV = findViewById(R.id.location);
        callTV = findViewById(R.id.callTV);
        callIV = findViewById(R.id.call);


        //전화기 모양 아이콘 클릭시 유실물 센터 전화 연결 됨.
        callIV.setOnClickListener(new View.OnClickListener() {
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
                result=okHttpThread.getData("http://52.79.146.35/lost_found_center/?station_name="+stationName);
                Log.d(TAG, "결과값 확인 : "+ result);
                centerName = result.split("#")[0];
                Log.d(TAG, "센터 이름 확인 : "+ centerName);
                centerNumber = result.split("#")[1];
                Log.d(TAG, "센터 전화번호 확인 : "+ centerNumber);
                callTV.setText(centerNumber);
                locationTV.setText(centerName);
                number = centerNumber;
                Log.d(TAG, "연락 전화번호 확인 : "+ number);


            }
        }).start();
    }
}
