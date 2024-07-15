package com.andromou.chatgpt.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.andromou.chatgpt.BuildConfig
import com.andromou.chatgpt.data.CompletionResponse
import com.andromou.chatgpt.databinding.ActivityMainBinding
import com.andromou.chatgpt.utils.Util
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatGptActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var copiedText = "Copied Text"
    // Retrofit instance and interface setup
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        binding?.sendBtn?.setOnClickListener { v ->
            sendMessage()

        }

        binding?.btnCopy?.setOnClickListener { v ->
            Util.copyText(this, copiedText)
        }
    }

    private fun callChatGptAPI(question: String) {
        // Retrofit call
        val jsonBody = JSONObject()
        jsonBody.put("model", "gpt-3.5-turbo")
        jsonBody.put("prompt", question)
        jsonBody.put("max_tokens", 100)
        jsonBody.put("temperature", 0)

        val body = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val CHATGPT_API_KEY = BuildConfig.CHATGPT_API_KEY
        val call = apiService.getCompletion("Bearer $CHATGPT_API_KEY", body)
        call.enqueue(object : Callback<CompletionResponse> {
            override fun onResponse(call: Call<CompletionResponse>, response: Response<CompletionResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.choices?.get(0)?.text ?: "No response"
                    addResponse(result.trim())
                    copiedText = result.trim()
                } else {
                    addResponse("Failed to load response due to ${response.errorBody()?.string()}")
                    copiedText = "Failed to load response due to ${response.errorBody()?.string()}"

                }
            }

            override fun onFailure(call: Call<CompletionResponse>, t: Throwable) {
                addResponse("Failed to load response due to ${t.message}")
                copiedText = "Failed to load response due to ${t.message}"

            }
        })
    }

    private fun addResponse(response: String) {
        binding?.responseView?.visibility = View.VISIBLE
        binding?.responseTextView?.text = response
    }
    private fun addRequest(request: String) {
        binding?.requestView?.visibility = View.VISIBLE
        binding?.requestTextView?.text = request
    }

    private fun sendMessage() {
        val question = binding?.messageEditText?.text.toString().trim()
        if (question != null) {
            if (Util.isOnline(this)) {
                // rewardNum++
                addRequest(question)
                callChatGptAPI(question)
                binding?.messageEditText?.setText("")
            } else {
                Util.showToast("Please Connect to the Internet", applicationContext)
            }
        }

    }


}






