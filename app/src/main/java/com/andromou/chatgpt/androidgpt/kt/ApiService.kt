package com.andromou.chatgpt.androidgpt.kt

import okhttp3.RequestBody
import retrofit2.Call

interface ApiService {
    @retrofit2.http.Headers("Content-Type: application/json")
    @retrofit2.http.POST("completions")
    fun getCompletion(
        @retrofit2.http.Header("Authorization") authHeader: String,
        @retrofit2.http.Body body: RequestBody
    ): Call<CompletionResponse>
}