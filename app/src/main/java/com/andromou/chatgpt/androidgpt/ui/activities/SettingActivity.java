package com.andromou.chatgpt.androidgpt.ui.activities;

import static android.content.ContentValues.TAG;
import static com.andromou.chatgpt.androidgpt.utils.AdsManager.loadAd;
import static com.andromou.chatgpt.androidgpt.utils.AdsManager.mInterstitialAd;
import static com.andromou.chatgpt.androidgpt.utils.Util.getMoreApps;
import static com.andromou.chatgpt.androidgpt.utils.Util.rateApp;
import static com.andromou.chatgpt.androidgpt.utils.Util.shareApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.andromou.chatgpt.androidgpt.R;
import com.andromou.chatgpt.androidgpt.databinding.ActivitySettingBinding;


public class SettingActivity extends AppCompatActivity {

     private ActivitySettingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


  //     AdsManager.loadInterstitialAd(SettingActivity.this);


        binding.moreapps.setOnClickListener(v -> getMoreApps(SettingActivity.this));

        binding.ratetheapp.setOnClickListener(v -> rateApp(getApplicationContext()));

        binding.shareapp.setOnClickListener(v -> { shareApp(getApplicationContext()); });

        binding.letsStarted.setOnClickListener(v -> { showInterstitial();  });


    }



    private void showInterstitial() {
         if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
             startActivity(new Intent(this, MainActivity.class));
             finish();
        }
      loadAd(this, MainActivity.class);
    }





}