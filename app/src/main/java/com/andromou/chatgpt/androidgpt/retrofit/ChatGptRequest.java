package com.andromou.chatgpt.androidgpt.retrofit;
import com.google.gson.annotations.SerializedName;

public class ChatGptRequest {
    @SerializedName("model")
    private String model;

    @SerializedName("prompt")
    private String prompt;

    @SerializedName("max_tokens")
    private int maxTokens;

    @SerializedName("temperature")
    private double temperature;

    public ChatGptRequest(String model, String prompt, int maxTokens, double temperature) {
        this.model = model;
        this.prompt = prompt;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
    }
}
