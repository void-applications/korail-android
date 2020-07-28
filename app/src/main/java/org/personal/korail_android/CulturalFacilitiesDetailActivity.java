package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.personal.korail_android.background.OkHttp;

public class CulturalFacilitiesDetailActivity extends AppCompatActivity {

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_facilities_detail);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttp okHttpThread = new OkHttp();
                result = okHttpThread.getData("http://52.79.146.35/cultural_facility_detail/?id=" + id);
                displayFacility(result);
            }
        }).start();
    }

    public void displayFacility(final String result) {
        Log.e("result", result);
    }
}