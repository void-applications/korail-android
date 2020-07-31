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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.personal.korail_android.adapter.NursingRoomAdapter;
import org.personal.korail_android.background.OkHttp;
import org.personal.korail_android.item.NursingRoomItem;

import java.util.ArrayList;

public class NursingRoomDetailActivity extends AppCompatActivity {

    String result,id;
    OkHttp http;
    Handler handler;
    TextView nursingRoomLocationTV,stationNameTV,babyTV,sofaTV,microwaveTV,washTV,numberTV;
    ImageView badyIV,sofaIV,microwaveIV,numberIV,washIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_room_detail);

        http=new OkHttp();
        handler=new Handler();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        Log.e("id?",id);

        nursingRoomLocationTV=findViewById(R.id.nursingRoomLocationTV);
        stationNameTV=findViewById(R.id.stationNameTV);
        babyTV=findViewById(R.id.babyTV);
        sofaTV=findViewById(R.id.sofaTV);
        microwaveTV=findViewById(R.id.microwaveTV);
        washTV=findViewById(R.id.washTV);
        numberTV=findViewById(R.id.numberTV);
        numberIV=findViewById(R.id.numberIV);
        badyIV=findViewById(R.id.babyIV);
        washIV=findViewById(R.id.washIV);
        sofaIV=findViewById(R.id.sofaIV);
        microwaveIV=findViewById(R.id.microwaveIV);

        new Thread(new Runnable() {
            @Override
            public void run() {

                result=http.getData("http://52.79.146.35/nursing_room_detail/?id=" + id);
                displayNursingRoom(result);
            }
        }).start();
    }


    public void displayNursingRoom(String result){

        Log.e("result",result);
        handler.post(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject=null;

                Log.e("result",result);
                try {
                    jsonObject=new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    JSONObject searchItem=jsonArray.getJSONObject(0);
                    String stationName=searchItem.getString("station_name");
                    String location=searchItem.getString("location");
                    String crib=searchItem.getString("crib");
                    String microwave=searchItem.getString("microwave");
                    String simple_wash=searchItem.getString("simple_wash");
                    String phone_number=searchItem.getString("phone_number");
                    String sofa=searchItem.getString("sofa");

                    stationNameTV.setText(stationName);
                    nursingRoomLocationTV.setText("상세 위치: "+location);
                    if(!crib.equals("null")){
                        babyTV.setText("유아용 침대 개수: "+ crib+"개");
                    }
                    else{
                        babyTV.setVisibility(View.GONE);
                        badyIV.setVisibility(View.GONE);
                    }
                    if(!microwave.equals("null")){
                        microwaveTV.setText("전자렌지 개수 : "+microwave+"개");
                    }
                    else{
                        microwaveTV.setVisibility(View.GONE);
                        microwaveIV.setVisibility(View.GONE);
                    }
                    if(!simple_wash.equals("null")){
                        washTV.setText("간이 세면대 개수 : "+simple_wash+"개");
                    }
                    else{
                        washTV.setVisibility(View.GONE);
                        washIV.setVisibility(View.GONE);
                    }
                    if(!phone_number.equals("null")){
                        numberTV.setText("전화번호 : "+phone_number);
                    }
                    else{
                        numberTV.setVisibility(View.GONE);
                        numberIV.setVisibility(View.GONE);
                    }
                    if(!sofa.equals("null")){
                        sofaTV.setText("소파 개수 : "+sofa+"개");
                    }
                    else{
                        sofaTV.setVisibility(View.GONE);
                        sofaTV.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}