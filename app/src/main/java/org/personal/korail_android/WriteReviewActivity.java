package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.personal.korail_android.background.OkHttp;

public class WriteReviewActivity extends AppCompatActivity {

    OkHttp okHttpThread;
    TextView cancelButton, writeButton;
    EditText reviewET;
    RatingBar reviewRB;
    String id;
    int starNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        cancelButton = findViewById(R.id.cancelButton);
        writeButton = findViewById(R.id.writeButton);
        reviewET = findViewById(R.id.reviewET);
        reviewRB = findViewById(R.id.reviewRB);

        okHttpThread=new OkHttp();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        starNum=intent.getIntExtra("starNum",3);

        reviewRB.setRating(starNum);

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("id", id);
                    jsonObject.accumulate("review", reviewET.getText());
                    jsonObject.accumulate("star", reviewRB.getRating());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String json = jsonObject.toString();

                postReview(json);
                reviewET.setText("");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void postReview(final String json) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                okHttpThread.postData("http://52.79.146.35/facility_review", json);
                finish();
            }
        }).start();
    }
}
