package com.andromou.chatgpt.androidgpt;

import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.test.core.app.ApplicationProvider;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.andromou.chatgpt.androidgpt.retrofit.ApiClient;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptApi;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptRequest;
import com.andromou.chatgpt.androidgpt.retrofit.ChatGptResponse;
import com.andromou.chatgpt.androidgpt.ui.activities.MainActivity;

public class MainActivityTest {

    @Mock
    ChatGptApi chatGptApi;

    @Mock
    Call<ChatGptResponse> call;

    @Mock
    Response<ChatGptResponse> response;

    MainActivity mainActivity;
    Context context;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        context = ApplicationProvider.getApplicationContext();
        mainActivity = Mockito.spy(new MainActivity());

        // Initialize ApiClient with the mock ChatGptApi
        ApiClient.setClient(chatGptApi);
    }

    @Test
    public void testSendChatGptRequest() {
        String question = "What is Mockito?";

        // Mock the response
        ChatGptResponse chatGptResponse = new ChatGptResponse();
        chatGptResponse.setResult("Mockito is a mocking framework for unit tests in Java.");

        when(chatGptApi.getChatGptResponse(anyString(), any(ChatGptRequest.class))).thenReturn(call);
        when(response.body()).thenReturn(chatGptResponse);

        // Execute the method to be tested
        mainActivity.sendChatGptRequest(question);

        // Verify that the call is enqueued and the correct response is received
        verify(call).enqueue(any(Callback.class));

        call.enqueue(new Callback<ChatGptResponse>() {
            @Override
            public void onResponse(Call<ChatGptResponse> call, Response<ChatGptResponse> response) {
                if (response.isSuccessful()) {
                    verify(mainActivity).addResponse("Mockito is a mocking framework for unit tests in Java.");
                }
            }

            @Override
            public void onFailure(Call<ChatGptResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
