package com.andromou.chatgpt.androidgpt.ui.activities;

import static com.andromou.chatgpt.androidgpt.utils.AdsManager.loadInterstitialAd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.andromou.chatgpt.androidgpt.databinding.ActivitySplashBinding;
import com.google.android.gms.ads.MobileAds;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        com.andromou.chatgpt.androidgpt.databinding.ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new Thread(() -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                }).start();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }






}