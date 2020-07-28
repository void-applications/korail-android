package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.personal.korail_android.background.OkHttp;

public class lostAndFoundCallNLocation extends AppCompatActivity {
    TextView locationTV;
    TextView callTV;
    ImageView callIV;
    String result;
    Handler handler;
    String phoneNumber;
    String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_call_n_location);

        locationTV = findViewById(R.id.locationTV);
        callTV = findViewById(R.id.callTV);
        callIV = findViewById(R.id.call);
        handler=new Handler();


        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttp okHttpThread = new OkHttp();
                result = okHttpThread.getData("http://52.79.146.35/lost_found_center");
                display(result);
            }
        }).start();

        //전화기 모양 아이콘 클릭시 유실물 센터 전화 연결 됨.
        callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             startActivity(new Intent("android.action.CALL", Uri.parse(tel)));
            }
        });
    }
    public void display(final String result){

            handler.post(new Runnable() {
                @Override
                public void run() {

                    phoneNumber = result.split("+//+")[0];
                    String name = result.split("+//+")[1];
                    tel = "tel:"+result.split("+////+")[0];
                    callTV.setText(phoneNumber);
                    locationTV.setText(name);


                    }
                }
            );
        };
    };




