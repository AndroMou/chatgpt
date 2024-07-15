package com.andromou.chatgpt.androidgpt.retrofit;

import com.google.gson.annotations.SerializedName;


public class ChatGptResponse {
    @SerializedName("choices")
    private ChatGptChoice[] choices;

    public String getResult() {
        if (choices != null && choices.length > 0) {
            return choices[0].getText().trim();
        }
        return null;
    }

    public void setResult(String s) {
        if (choices != null && choices.length > 0) {
            choices[0].setText(s);
        }
    }

    private static class ChatGptChoice {
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
