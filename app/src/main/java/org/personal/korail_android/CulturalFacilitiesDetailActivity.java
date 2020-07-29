package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.personal.korail_android.adapter.FacilityReviewAdapter;
import org.personal.korail_android.background.OkHttp;
import org.personal.korail_android.item.FacilityReviewItem;

import java.util.ArrayList;

public class CulturalFacilitiesDetailActivity extends AppCompatActivity {

    String result, id;
    Handler handler;
    RatingBar reviewRB, averageRB;
    TextView stationNameTV, locationTV, equipmentTV, rentalCostTV, hoursOfUseTV, phoneNumberTV, averageFloatTV;
    ImageView locationIV, equipmentIV, rentalCostIV, hoursOfUseIV, phoneNumberIV;
    ArrayList<FacilityReviewItem> facilityReviewItemArrayList;
    FacilityReviewAdapter facilityReviewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    OkHttp okHttpThread;

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
        recyclerView = findViewById(R.id.facilitiesReviewRecyclerview);
        reviewRB = findViewById(R.id.reviewRB);
        averageRB = findViewById(R.id.averageStarRB);
        phoneNumberIV = findViewById(R.id.phoneNumberIV);
        locationIV = findViewById(R.id.locationIV);
        rentalCostIV = findViewById(R.id.rentalCostIV);
        equipmentIV = findViewById(R.id.equipmentIV);
        hoursOfUseIV = findViewById(R.id.hoursOfUseIV);
        averageFloatTV = findViewById(R.id.averageFloatTV);

        facilityReviewItemArrayList = new ArrayList<>();
        facilityReviewAdapter = new FacilityReviewAdapter(facilityReviewItemArrayList, this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(facilityReviewAdapter);

        handler = new Handler();
        okHttpThread = new OkHttp();
        reviewRB.setOnRatingBarChangeListener(new Listener());

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        new Thread(new Runnable() {
            @Override
            public void run() {

                result = okHttpThread.getData("http://52.79.146.35/cultural_facility_detail/?id=" + id);
                displayFacility(result);
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getReview();
        reviewRB.setRating(0);
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
                    String phoneNumber = searchItem.getString("phone_number");
                    String hoursOfUse = searchItem.getString("hours_of_use");
                    String rentalCost = searchItem.getString("rental_cost");

                    locationTV.setText("위치 : " + floor + " " + location);
                    stationNameTV.setText(lineName + " " + stationName + "역");
                    if (!equipment.equals("null")) {
                        equipmentTV.setText("장비 : " + equipment);
                    } else {
                        equipmentTV.setVisibility(View.GONE);
                        equipmentIV.setVisibility(View.GONE);
                    }
                    if (!phoneNumber.equals("null")) {
                        phoneNumberTV.setText("전화번호 : " + phoneNumber);
                    } else {
                        phoneNumberTV.setVisibility(View.GONE);
                        phoneNumberIV.setVisibility(View.GONE);
                    }
                    if (!hoursOfUse.equals("null")) {
                        hoursOfUseTV.setText("이용시간 : " + hoursOfUse);
                    } else {
                        hoursOfUseTV.setVisibility(View.GONE);
                        hoursOfUseIV.setVisibility(View.GONE);
                    }
                    if (!rentalCost.equals("null")) {
                        rentalCostTV.setText("대여비용 : " + rentalCost + " 원");
                    } else {
                        rentalCostTV.setVisibility(View.GONE);
                        rentalCostIV.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void displayFacilityReview(final String result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                ArrayList<Integer> starList = new ArrayList();
                facilityReviewItemArrayList.clear();
                facilityReviewAdapter.notifyDataSetChanged();
                try {
                    jsonObject = new JSONObject(result);
                    //그중에서 data를 키값으로 갖는 jsonarray를 가져옴
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject searchItem = jsonArray.getJSONObject(i);
                        String id = searchItem.getString("id");
                        String review = searchItem.getString("review");
                        String date = searchItem.getString("date");
                        String star = searchItem.getString("star");

                        FacilityReviewItem facilityReviewItem = new FacilityReviewItem(id, review, date, Integer.parseInt(star));
                        facilityReviewItemArrayList.add(facilityReviewItem);
                        facilityReviewAdapter.notifyDataSetChanged();

                        starList.add(Integer.parseInt(star));
                    }

                    int sum = 0;
                    for (int i = 0; i < starList.size(); i++) {
                        sum += starList.get(i);
                    }
                    float average = sum / (float) starList.size();
                    averageFloatTV.setText(average+" 점");
                    averageRB.setRating(average);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getReview() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                result = okHttpThread.getData("http://52.79.146.35/facility_review/?id=" + id);
                displayFacilityReview(result);
            }
        }).start();
    }


    class Listener implements RatingBar.OnRatingBarChangeListener {

        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            int starNum = (int) reviewRB.getRating();
            Intent ratingIntent = new Intent(getApplicationContext(), WriteReviewActivity.class);
            ratingIntent.putExtra("id", id);
            ratingIntent.putExtra("starNum", starNum);
            startActivity(ratingIntent);
        }
    }
}