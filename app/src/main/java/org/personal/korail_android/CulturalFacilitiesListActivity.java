package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.personal.korail_android.background.OkHttp;

public class CulturalFacilitiesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_facilities_list);

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttp okHttpThread = new OkHttp();
                okHttpThread.getData("http://52.79.146.35/cultural_facilities");
            }
        }).start();

    }
}