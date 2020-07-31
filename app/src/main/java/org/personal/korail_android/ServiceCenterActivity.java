package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.personal.korail_android.background.OkHttp;

public class ServiceCenterActivity extends AppCompatActivity {

    TextView locationTV;
    TextView callNumberTV,locationTitle;
    ImageView callButton,locationImage;
    String number;
    String stationName;
    String TAG = "고객센터 상세 페이지";
    String result;
    String centerNumber;
    String centerName;
    Handler handler;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        locationTV = findViewById(R.id.location);
        callNumberTV = findViewById(R.id.callTV);
        callButton = findViewById(R.id.call);
        locationImage=findViewById(R.id.locationImage);
        locationTitle=findViewById(R.id.locationTitle);
        linearLayout=findViewById(R.id.linear);

        handler = new Handler();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
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
                result = okHttpThread.getData("http://52.79.146.35/service_center/?station_name=" + stationName);
                display(result);
            }
        }).start();
    }

    public void display(String result) {
        handler.post(new Runnable() {

            @Override
            public void run() {

                if (!result.equals("#")) {

                    centerName = result.split("#")[0];
                    centerNumber = result.split("#")[1];
                    callNumberTV.setText(centerNumber);
                    locationTV.setText(centerName);

                    number = centerNumber;
                    Log.d(TAG, "연락 전화번호 확인 : " + number);
                } else {
                    callNumberTV.setText("정보가 없습니다");
                    locationTV.setVisibility(View.INVISIBLE);
                    locationImage.setVisibility(View.INVISIBLE);
                    locationTitle.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}