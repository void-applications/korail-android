package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LottieAnimationView lottieAnimationView=findViewById(R.id.app_lottie);
        lottieAnimationView.setAnimation("applogo.json");
        lottieAnimationView.playAnimation();
    }
}