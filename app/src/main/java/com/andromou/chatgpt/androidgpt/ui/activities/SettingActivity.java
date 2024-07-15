package com.andromou.chatgpt.androidgpt.ui.activities;

import static com.andromou.chatgpt.androidgpt.utils.Util.startAppCompatActivity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.andromou.chatgpt.androidgpt.databinding.ActivitySettingBinding;
import com.andromou.chatgpt.androidgpt.utils.Util;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize Ad

        // Set click listeners
        binding.moreapps.setOnClickListener(v -> Util.getMoreApps(SettingActivity.this));
        binding.ratetheapp.setOnClickListener(v -> Util.rateApp(getApplicationContext()));
        binding.shareapp.setOnClickListener(v -> Util.shareApp(getApplicationContext()));
        binding.letsStarted.setOnClickListener(v ->    startAppCompatActivity( this, MainActivity.class) );
    }


}
