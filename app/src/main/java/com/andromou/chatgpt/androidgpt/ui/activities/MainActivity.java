package com.andromou.chatgpt.androidgpt.ui.activities;

import static com.andromou.chatgpt.androidgpt.adapters.MessageAdapter.copiedText;
 import static com.andromou.chatgpt.androidgpt.utils.Util.copyText;
import static com.andromou.chatgpt.androidgpt.utils.Util.isOnline;
import static com.andromou.chatgpt.androidgpt.utils.Util.onPaste;
import static com.andromou.chatgpt.androidgpt.utils.Util.showToast;

import android.annotation.SuppressLint;
 import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andromou.chatgpt.androidgpt.BuildConfig;
import com.andromou.chatgpt.androidgpt.adapters.MessageAdapter;
import com.andromou.chatgpt.androidgpt.data.Message;
import com.andromou.chatgpt.androidgpt.databinding.ActivityMainBinding;
import com.andromou.chatgpt.androidgpt.kt.ApiService;
import com.andromou.chatgpt.androidgpt.kt.CompletionResponse;
import com.andromou.chatgpt.androidgpt.retrofit.ApiClient;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptApi;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptRequest;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private ActivityMainBinding binding;
    private Call<ChatGptResponse> call;
    private ApiService apiService = null;
    private Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messageList = new ArrayList<>();
//PcXazsvrP9ol6ZWhA
        // Setup recycler view
        messageAdapter = new MessageAdapter(messageList, this);
        binding.recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(llm);

        binding.sendBtn.setOnClickListener(v -> {
            try {
                sendMessage();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        binding.imageButton.setOnClickListener(v -> {
            binding.messageEditText.setText("");
            onPaste(MainActivity.this, binding.messageEditText);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addToChat(String message, String sentByMe) {
        messageList.add(new Message(message, sentByMe));
        messageAdapter.notifyDataSetChanged();
        binding.recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
    }

    public void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    private void sendMessage() throws JSONException {
        String question = binding.messageEditText.getText().toString().trim();
        if (isOnline(this)) {
            addToChat(question, Message.SENT_BY_ME);
              sendChatGptRequest(question);
            //   callAPI(question);
            binding.messageEditText.setText("");
        } else {
            showToast(getApplicationContext(), "Please Connect to the Internet");
        }
        binding.welcomeText.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position) {
        copyText(MainActivity.this, messageList.get(position).getMessage());
    }

    public void sendChatGptRequest(String question) {
        messageList.add(new Message("Wait... ", Message.SENT_BY_BOT));
        ChatGptApi chatGptApi = ApiClient.getClient().create(ChatGptApi.class);

        int TEMPERATURE = 0;
        String GPT_MODEL = "gpt-3.5-turbo";
        int MAX_TOKENS = 100;
        ChatGptRequest request = new ChatGptRequest(GPT_MODEL, question, MAX_TOKENS, TEMPERATURE);

        String AUTH_TOKEN = "Bearer " + BuildConfig.CHATGPT_API_KEY;
        Call<ChatGptResponse> call = chatGptApi.getChatGptResponse(AUTH_TOKEN, request);
        call.enqueue(new Callback<ChatGptResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatGptResponse> call, @NonNull Response<ChatGptResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addResponse(response.body().getResult());
                } else {
                    String error;
                    if (response.errorBody() != null) {
                        try {
                            error = response.errorBody().string();
                        } catch (IOException e) {
                            error = "Error reading error message.";
                            e.printStackTrace();
                        }
                    } else {
                        error = "Unknown error occurred.";
                    }
                    addResponse("Failed to load response due to " + error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatGptResponse> call, @NonNull Throwable t) {
                addResponse("Failed to load response due to " + t.getMessage());
            }
        });
    }

    private void callAPI(String question) throws JSONException {
        // Retrofit call
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gpt-3.5-turbo");
        jsonBody.put("prompt", question);
        jsonBody.put("max_tokens", 100);
        jsonBody.put("temperature", 0);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());


        Call<CompletionResponse> call = getApiService().getCompletion("Bearer " + BuildConfig.CHATGPT_API_KEY, body);
        call.enqueue(new Callback<CompletionResponse>() {
            @Override
            public void onResponse(Call<CompletionResponse> call, Response<CompletionResponse> response) {
                if (response.isSuccessful()) {
                    String result = response.body() != null ? response.body().getChoices().get(0).getText() : "No response";
                    addResponse(result.trim());
                } else {
                    try {
                        addResponse("Failed to load response due to " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        addResponse("Failed to load response due to an error.");
                    }
                }
            }

            @Override
            public void onFailure(Call<CompletionResponse> call, Throwable t) {
                addResponse("Failed to load response due to " + t.getMessage());
            }
        });
    }


    private ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }


    private Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openai.com/v1/")
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
        }
    }
}





















