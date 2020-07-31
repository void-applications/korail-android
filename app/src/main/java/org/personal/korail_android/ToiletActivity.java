package org.personal.korail_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    String stationName;
    String location;
    String manAndWomen;
    String insideOut;
    String TAG = "화장실";
    TextView stationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet);

        recyclerView = findViewById(R.id.infoRecyclerview);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        stationTV = findViewById(R.id.stationNameTV);
        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(50);
        recyclerView.addItemDecoration(recyclerDecoration);

        toiletItemArrayList = new ArrayList<>();

        handler = new Handler();
        toiletAdapter = new ToiletAdapter(toiletItemArrayList, this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(toiletAdapter);

        toiletAdapter.setOnItemClickListener(new ToiletAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

                Intent intent = new Intent(getApplicationContext(), CulturalFacilitiesDetailActivity.class);
                intent.putExtra("id", toiletItemArrayList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent getInfoIntent = getIntent();
        stationName = getInfoIntent.getStringExtra("stationName");
        Log.d(TAG, "인텐트로 받은 결과값 확인 : " + stationName);
         stationTV.setText(stationName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttp okHttpThread = new OkHttp();
                result = okHttpThread.getData("http://52.79.146.35/restroom/?station_name=" + stationName);
                display(result);


            }
        }).start();

    }

    public void display(String result) {

        String TAG = "data";
        String TAG_ID = "id";
        String TAG_WC = "gender_classification";
        String TAG_LOCATION = "location";
        String TAG_INSIDE_OUTSIDE = "inside_outside";

        try {

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject json = jsonArray.getJSONObject(i);
                String id = json.getString(TAG_ID);
                String wc = json.getString(TAG_WC);
                Log.d(TAG, "남여 확인 : "+wc);
                String location = json.getString(TAG_LOCATION);
                String insideOutside = json.getString(TAG_INSIDE_OUTSIDE);

                ToiletItem dataClass = new ToiletItem(location, wc, insideOutside, id);
                dataClass.setId(id);
                dataClass.setLocation("위치 : "+location);
                dataClass.setManWomen(wc+"화장실");
                if(insideOutside.equals("외")){
                    dataClass.setInsideOrOut("개찰구 밖");

                }else{
                    dataClass.setInsideOrOut("개찰구 안");

                }

                toiletItemArrayList.add(dataClass);
                toiletAdapter.notifyDataSetChanged();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}