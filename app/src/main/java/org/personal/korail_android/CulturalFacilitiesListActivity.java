package org.personal.korail_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.personal.korail_android.adapter.FacilitiesAdapter;
import org.personal.korail_android.background.OkHttp;
import org.personal.korail_android.deco.RecyclerDecoration;
import org.personal.korail_android.item.FacilitiesItem;

import java.util.ArrayList;

public class CulturalFacilitiesListActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ArrayList<FacilitiesItem> facilitiesItemArrayList, changedFacilityList;
    FacilitiesAdapter facilitiesAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String result;
    Handler handler;
    TextView resultTV;
    EditText searchET;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_facilities_list);

        recyclerView = findViewById(R.id.facilitiesRecyclerview);
        resultTV = findViewById(R.id.resultCountTV);
        searchET = findViewById(R.id.searchStationET);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(50);
        recyclerView.addItemDecoration(recyclerDecoration);

        changedFacilityList=new ArrayList<>();

        handler = new Handler();
        facilitiesItemArrayList = new ArrayList<>();
        facilitiesAdapter = new FacilitiesAdapter(facilitiesItemArrayList, this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(facilitiesAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttp okHttpThread = new OkHttp();
                result = okHttpThread.getData("http://52.79.146.35/cultural_facilities");
                displayFacilities(result, "");
            }
        }).start();

        facilitiesAdapter.setOnItemClickListener(new FacilitiesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

                Intent intent = new Intent(getApplicationContext(), CulturalFacilitiesDetailActivity.class);
                intent.putExtra("id", facilitiesItemArrayList.get(position).getId());
                startActivity(intent);
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String searchText = searchET.getText().toString();
                searchFilter(searchText);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.culturalFacilities);
    }

    public void searchFilter(String searchText) {
        changedFacilityList.clear();

        for (int i = 0; i < facilitiesItemArrayList.size(); i++) {
            if (facilitiesItemArrayList.get(i).getStationName().toLowerCase().contains(searchText.toLowerCase())) {
                changedFacilityList.add(facilitiesItemArrayList.get(i));
            }
        }
        facilitiesAdapter.filterList(changedFacilityList);
    }

    public void displayFacilities(final String result, final String searchStation) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                facilitiesItemArrayList.clear();
                facilitiesAdapter.notifyDataSetChanged();
                try {
                    jsonObject = new JSONObject(result);
                    //그중에서 data를 키값으로 갖는 jsonarray를 가져옴
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (!searchStation.equals("")) {
                        resultTV.setText(searchStation + " 검색결과 총 " + jsonArray.length() + "건");
                        resultTV.setVisibility(View.VISIBLE);
                    }
                    //데이터를 jsonarray길이만큼 반복하여 name값을 가져옴
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject searchItem = jsonArray.getJSONObject(i);
                        String id = searchItem.getString("id");
                        String lineName = searchItem.getString("line_name");
                        String stationName = searchItem.getString("station_name");
                        String floor = searchItem.getString("floor");
                        String location = searchItem.getString("location");
                        String equipment = searchItem.getString("equipment");
                        FacilitiesItem facilitiesItem = new FacilitiesItem(id, lineName, stationName, floor, location, equipment);
                        facilitiesItemArrayList.add(facilitiesItem);
                        facilitiesAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent toHome = new Intent(this, HomeActivity.class);
                startActivity(toHome);
                break;

            case R.id.event:
                Intent toEvent = new Intent(this, EventListActivity.class);
                startActivity(toEvent);
                break;

            case R.id.chat:
                Intent toChat = new Intent(this, ChatListActivity.class);
                startActivity(toChat);
                break;

            case R.id.lostAndFound:
                Intent toLostAndFound = new Intent(this, LostAndFoundSearch.class);
                startActivity(toLostAndFound);
                break;
        }
        overridePendingTransition(0, 0);
        return true;
    }
}