package com.andromou.chatgpt.androidgpt

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Call {

    private fun callAPI(question: String) {
        // Retrofit call
        val jsonBody = JSONObject()
        jsonBody.put("model", "gpt-3.5-turbo")
        jsonBody.put("prompt", question)
        jsonBody.put("max_tokens", 100)
        jsonBody.put("temperature", 0)


    }
}