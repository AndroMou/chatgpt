package com.andromou.chatgpt.androidgpt.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.openai.com/v1/";

    private static Retrofit retrofit = null;
    private static ChatGptApi chatGptApi = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ChatGptApi getChatGptApi() {
        if (chatGptApi == null) {
            chatGptApi = getClient().create(ChatGptApi.class);
        }
        return chatGptApi;
    }

    public static void setClient(ChatGptApi customChatGptApi) {
        chatGptApi = customChatGptApi;
    }
}

