package com.andromou.chatgpt.androidgpt.ui.activities;

import static android.content.ContentValues.TAG;
import static com.andromou.chatgpt.androidgpt.adapters.MessageAdapter.copiedText;
import static com.andromou.chatgpt.androidgpt.utils.AdsManager.loadRewordedAd;
import static com.andromou.chatgpt.androidgpt.utils.AdsManager.rewardedAd;
import static com.andromou.chatgpt.androidgpt.utils.Util.copyText;
import static com.andromou.chatgpt.androidgpt.utils.Util.isOnline;
import static com.andromou.chatgpt.androidgpt.utils.Util.onPaste;
import static com.andromou.chatgpt.androidgpt.utils.Util.showToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andromou.chatgpt.androidgpt.BuildConfig;
import com.andromou.chatgpt.androidgpt.R;
import com.andromou.chatgpt.androidgpt.adapters.MessageAdapter;
import com.andromou.chatgpt.androidgpt.data.Message;
import com.andromou.chatgpt.androidgpt.databinding.ActivityMainBinding;
import com.andromou.chatgpt.androidgpt.retrofit.ApiClient;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptApi;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptRequest;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptResponse;
import com.andromou.chatgpt.androidgpt.ui.dialogs.CustomDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    List<Message> messageList;
    MessageAdapter messageAdapter;

    public static int rewardNum = 0;

    private ActivityMainBinding binding;


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messageList = new ArrayList<>();

        sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);


        //setup recycler view
        messageAdapter = new MessageAdapter(messageList, this);
        binding.recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(llm);




        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        CustomDialog customDialog = new CustomDialog(MainActivity.this);

        customDialog.setCancelable(false);
        // Show the dialog

        if(!sharedPreferences.getString("sentpassword","").equals("ChatGPTBasedAppFreeLancer")){
         //   customDialog.show();
        }

    //    loadRewordedAds();
        binding.sendBtn.setOnClickListener((v)->{
        //   executeMethod(this);
            showRewordedAds();

        });

        binding.imageButton.setOnClickListener((v)->{
             binding.messageEditText.setText("");
            onPaste(MainActivity.this, binding.messageEditText);

        });
    }

    @SuppressLint("NotifyDataSetChanged")
    void addToChat(String message, String sentByMe){
        runOnUiThread(() -> {
            messageList.add(new Message(message,sentByMe));
            messageAdapter.notifyDataSetChanged();
            binding.recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

   public void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response,Message.SENT_BY_BOT);
    }




    private void showRewordedAds() {
        if (rewardedAd != null && rewardNum % 5 == 0) {
            Activity activityContext = MainActivity.this;
            rewardedAd.show(activityContext, rewardItem -> {
                // Handle the reward.
                Log.d(TAG, "The user earned the reward.");
                int rewardAmount = rewardItem.getAmount();
                String rewardType = rewardItem.getType();
                sendMessage();
                loadRewordedAd(MainActivity.this);
            });
        } else {
            sendMessage();
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }

    }

   private void sendMessage() {
     String question = binding.messageEditText.getText().toString().trim();
       if (isOnline(this)){
           rewardNum++;
           addToChat(question,Message.SENT_BY_ME);
           sendChatGptRequest(question);
           binding.messageEditText.setText("");
       } else {
           showToast(getApplicationContext(), "Please Connect to the Internet");
       }
     binding.welcomeText.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemClick(int i) {
        copyText(MainActivity.this, copiedText);
    }

    public void sendChatGptRequest(String question) {
        messageList.add(new Message("Wait... ", Message.SENT_BY_BOT));
        ChatGptApi chatGptApi = ApiClient.getClient().create(ChatGptApi.class);

        int TEMPERATURE = 0;
        String GPT_MODEL = "text-davinci-003";
        int MAX_TOKENS = 1000;
        ChatGptRequest request = new ChatGptRequest(GPT_MODEL, question, MAX_TOKENS, TEMPERATURE);


        String AUTH_TOKEN = "Bearer " + BuildConfig.GPT_KEY;
        Call<ChatGptResponse> call = chatGptApi.getChatGptResponse(AUTH_TOKEN, request);
        call.enqueue(new Callback<ChatGptResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatGptResponse> call, @NonNull Response<ChatGptResponse> response) {
                if (response.isSuccessful()) {
                    ChatGptResponse chatGptResponse = response.body();
                    if (chatGptResponse != null) {
                        String result = chatGptResponse.getResult();
                        addResponse(result);
                    }
                } else {
                    addResponse("Failed to load response due to " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<ChatGptResponse> call, @NonNull Throwable t) {
                addResponse("Failed to load response due to " + t.getMessage());
            }
        });
    }



}




















