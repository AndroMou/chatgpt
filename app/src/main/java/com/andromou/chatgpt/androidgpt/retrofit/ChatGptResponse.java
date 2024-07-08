package com.andromou.chatgpt.androidgpt.retrofit;

import com.google.gson.annotations.SerializedName;


public class ChatGptResponse {
    @SerializedName("choices")
    private final ChatGptChoice[] choices;

    public ChatGptResponse(ChatGptChoice[] choices) {
        this.choices = choices;
    }

    public String getResult() {
        if (choices != null && choices.length > 0) {
            return choices[0].getText().trim();
        }
        return null;
    }

    private static class ChatGptChoice {
        @SerializedName("text")
        private final String text;

        private ChatGptChoice(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}

