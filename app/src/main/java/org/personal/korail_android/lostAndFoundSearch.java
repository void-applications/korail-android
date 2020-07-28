package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String stationName = stationNameET.getText().toString();
                        OkHttp okHttpThread = new OkHttp();
                        result=okHttpThread.getData("http://52.79.146.35/lost_found_center/?station_name="+stationName);
                        centerName = result.split("+/+")[0];
                        centerNumber = result.split("+/+")[1];
                    }
                }).start();

                Intent searchIntent = new Intent(getApplicationContext(), lostAndFoundCallNLocation.class);
                searchIntent.putExtra("centerName", centerName);
                searchIntent.putExtra("centerNumber", centerNumber);
                  startActivity(searchIntent);

            }
        });

    }


}