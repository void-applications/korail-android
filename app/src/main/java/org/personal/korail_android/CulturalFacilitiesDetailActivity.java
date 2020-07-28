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

    String result,id;
    Handler handler;
    RatingBar reviewRB;
    EditText reviewET;
    TextView stationNameTV, locationTV, equipmentTV, rentalCostTV, hoursOfUseTV, phoneNumberTV;
    ArrayList<FacilityReviewItem> facilityReviewItemArrayList;
    FacilityReviewAdapter facilityReviewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Button writeButton;
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
        writeButton = findViewById(R.id.writeButton);
        reviewET=findViewById(R.id.reviewET);
        reviewRB=findViewById(R.id.reviewRB);

        facilityReviewItemArrayList = new ArrayList<>();
        facilityReviewAdapter = new FacilityReviewAdapter(facilityReviewItemArrayList, this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(facilityReviewAdapter);

        handler = new Handler();
        okHttpThread = new OkHttp();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        new Thread(new Runnable() {
            @Override
            public void run() {

                result = okHttpThread.getData("http://52.79.146.35/cultural_facility_detail/?id=" + id);
                displayFacility(result);
            }
        }).start();

        getReview();


        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.accumulate("id",id);
                    jsonObject.accumulate("review",reviewET.getText());
                    jsonObject.accumulate("star",reviewRB.getNumStars());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String json=jsonObject.toString();

                postReview(json);
                reviewET.setText("");
            }
        });


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

                    locationTV.setText("위치 : "+floor + " " + location);
                    stationNameTV.setText(lineName + " " + stationName + "역");
                    if(!equipment.equals("null")){
                        equipmentTV.setText("장비 : "+equipment);
                    }
                    else{
                        equipmentTV.setVisibility(View.GONE);
                    }
                    if(!phoneNumber.equals("null")){
                        phoneNumberTV.setText("전화번호 : "+phoneNumber);
                    }
                    else{
                        phoneNumberTV.setVisibility(View.GONE);


                    }
                    if(!hoursOfUse.equals("null")) {
                        hoursOfUseTV.setText("이용시간 : " + hoursOfUse);
                    }
                    else{
                        hoursOfUseTV.setVisibility(View.GONE);
                    }
                    if(!rentalCost.equals("null")){
                        rentalCostTV.setText("대여비용 : "+rentalCost+" 원");
                    }
                    else{
                        rentalCostTV.setVisibility(View.GONE);
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
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getReview(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                result = okHttpThread.getData("http://52.79.146.35/facility_review/?id=" + id);
                displayFacilityReview(result);
            }
        }).start();
    }

    public void postReview(final String json){
        new Thread(new Runnable() {
            @Override
            public void run() {

                okHttpThread.postData("http://52.79.146.35/facility_review",json);
                displayFacilityReview(result);
                getReview();
            }
        }).start();
    }
}