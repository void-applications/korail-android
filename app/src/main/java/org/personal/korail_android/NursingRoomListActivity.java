package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.personal.korail_android.adapter.NursingRoomAdapter;
import org.personal.korail_android.background.OkHttp;
import org.personal.korail_android.deco.RecyclerDecoration;
import org.personal.korail_android.item.NursingRoomItem;

import java.util.ArrayList;

public class NursingRoomListActivity extends AppCompatActivity {

    EditText searchStationET;
    ArrayList<NursingRoomItem> nursingRoomItemArrayList, changedNursingRoomList;
    NursingRoomAdapter nursingRoomAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Handler handler;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_room_list);

        searchStationET = findViewById(R.id.searchStationET);
        recyclerView = findViewById(R.id.nursingRoomRecyclerview);


        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(50);
        recyclerView.addItemDecoration(recyclerDecoration);

        handler = new Handler();
        nursingRoomItemArrayList = new ArrayList<>();
        changedNursingRoomList=new ArrayList<>();
        nursingRoomAdapter = new NursingRoomAdapter(nursingRoomItemArrayList, this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(nursingRoomAdapter);


        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttp okHttpThread = new OkHttp();
                result = okHttpThread.getData("http://52.79.146.35/nursing_room");
                displayNursingRoom(result);
            }
        }).start();

        nursingRoomAdapter.setOnItemClickListener(new NursingRoomAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), NursingRoomDetailActivity.class);
                intent.putExtra("id", nursingRoomItemArrayList.get(position).getId());
                startActivity(intent);
            }
        });

        searchStationET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String searchText = searchStationET.getText().toString();
                searchFilter(searchText);
            }
        });
    }

    public void displayNursingRoom(String result) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject=null;
                nursingRoomItemArrayList.clear();
                nursingRoomAdapter.notifyDataSetChanged();

                try {
                    jsonObject=new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject searchItem=jsonArray.getJSONObject(i);
                        String id=searchItem.getString("id");
                        String stationName=searchItem.getString("station_name");
                        String location=searchItem.getString("location");
                        String phoneNumber=searchItem.getString("phone_number");

                        NursingRoomItem nursingRoomItem=new NursingRoomItem(id,location,phoneNumber,stationName);
                        nursingRoomItemArrayList.add(nursingRoomItem);
                        nursingRoomAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void searchFilter(String text) {

        changedNursingRoomList.clear();

        for (int i = 0; i < nursingRoomItemArrayList.size(); i++) {
            if (nursingRoomItemArrayList.get(i).getStationName().toLowerCase().contains(text.toLowerCase())) {
                changedNursingRoomList.add(nursingRoomItemArrayList.get(i));
            }
        }

        nursingRoomAdapter.filterList(changedNursingRoomList);
    }

}