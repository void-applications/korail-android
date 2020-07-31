package org.personal.korail_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.personal.korail_android.background.OkHttp;
import org.personal.korail_android.deco.RecyclerDecoration;

import java.util.ArrayList;

public class ToiletActivity extends AppCompatActivity {


    ArrayList<ToiletItem> toiletItemArrayList;
    ToiletAdapter toiletAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Handler handler;
    BottomNavigationView bottomNavigation;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet);

        recyclerView = findViewById(R.id.infoRecyclerview);
        bottomNavigation = findViewById(R.id.bottomNavigation);
//        bottomNavigation.setOnNavigationItemSelectedListener(this);

        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(50);
        recyclerView.addItemDecoration(recyclerDecoration);

        toiletItemArrayList = new ArrayList<>();

        handler = new Handler();
        toiletAdapter = new ToiletAdapter(getApplicationContext(),toiletItemArrayList);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(toiletAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttp okHttpThread = new OkHttp();
                result = okHttpThread.getData("http://52.79.146.35/toilet");
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.home:
//                Intent toHome = new Intent(this, HomeActivity.class);
//                startActivity(toHome);
//                break;
//
//            case R.id.event:
//                Intent toEvent = new Intent(this, EventListActivity.class);
//                startActivity(toEvent);
//                break;
//
//            case R.id.chat:
//                Intent toChat = new Intent(this, ChatListActivity.class);
//                startActivity(toChat);
//                break;
//
//            case R.id.facilities:
//                Intent toFacilities = new Intent(this, FacilitiesActivity.class);
//                startActivity(toFacilities);
//                break;
//        }
//        overridePendingTransition(0, 0);
//        return true;
//    }
}