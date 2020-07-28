package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class lostAndFoundSearch extends AppCompatActivity {

    EditText searchStationET;
    String stationName;
    ImageView searchIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_search);

        searchStationET = findViewById(R.id.searchStation);
        searchIV = findViewById(R.id.searchIV);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getApplicationContext(), lostAndFoundCallNLocation.class);
                stationName = searchStationET.getText().toString();

                searchIntent.putExtra("stationName", stationName);
                startActivity(searchIntent);
            }
        });




    }
}