package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.personal.korail_android.background.OkHttp;
import org.personal.korail_android.item.FacilitiesItem;

public class CulturalFacilitiesDetailActivity extends AppCompatActivity {

    String result;
    Handler handler;
    TextView stationNameTV, locationTV, equipmentTV, rentalCostTV, hoursOfUseTV, phoneNumberTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_facilities_detail);

        stationNameTV = findViewById(R.id.stationNameTV);
        locationTV = findViewById(R.id.locationTV);
        equipmentTV = findViewById(R.id.equipmentTV);
        rentalCostTV = findViewById(R.id.rentalCostTV);
        hoursOfUseTV = findViewById(R.id.hoursOfUseTV);
        phoneNumberTV = findViewById(R.id.phoneNumberTV);

        handler=new Handler();

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
        handler.post(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    //그중에서 data를 키값으로 갖는 jsonarray를 가져옴
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                        JSONObject searchItem = jsonArray.getJSONObject(0);
                        String lineName = searchItem.getString("line_name");
                        String stationName = searchItem.getString("station_name");
                        String floor = searchItem.getString("floor");
                        String location = searchItem.getString("location");
                        String equipment = searchItem.getString("equipment");
                        String phoneNumber=searchItem.getString("phone_number");
                        String hoursOfUse=searchItem.getString("hours_of_use");
                        String rentalCost=searchItem.getString("rental_cost");

                        locationTV.setText(floor+" "+location);
                        stationNameTV.setText(lineName+" "+stationName+"역");
                        equipmentTV.setText(equipment);
                        phoneNumberTV.setText(phoneNumber);
                        hoursOfUseTV.setText(hoursOfUse);
                        rentalCostTV.setText(rentalCost);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}