package com.andromou.chatgpt.androidgpt.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChatGptApi {
    @POST("completions") // Replace "endpoint" with the specific endpoint of the ChatGPT API.
    Call<ChatGptResponse> getChatGptResponse(@Header("Authorization") String token, @Body ChatGptRequest request);
}
