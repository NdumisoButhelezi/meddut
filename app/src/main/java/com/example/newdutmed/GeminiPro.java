package com.example.newdutmed;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.HarmCategory;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.concurrent.Executor;

public class GeminiPro {

    // Method to send a query to the Gemini API and handle the response
    public static void getResponse(ChatFutures chatModel, String query, ResponseCallBack callBack) {
        Content.Builder userMessageBuilder = new Content.Builder();
        userMessageBuilder.setRole("user");
        userMessageBuilder.addText(query);
        Executor executor = Runnable::run;  // Executor for handling the response callback
        Content userMessage = userMessageBuilder.build();

        ListenableFuture<GenerateContentResponse> response = chatModel.sendMessage(userMessage);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                callBack.onResponse(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                callBack.onError(t);
            }
        }, executor);
    }

    // Method to configure and create a generative model
    public GenerativeModelFutures getModel() {
        String apiKey = BuildConfig.apiKey;  // Retrieve the API key from the build configuration

        // Configure safety settings to prevent harassment
        SafetySetting harassmentSafety = new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH);

        // Configure generation settings
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.9f;
        configBuilder.topK = 16;
        configBuilder.topP = 0.1f;
        GenerationConfig generationConfig = configBuilder.build();

        // Create and return the generative model
        GenerativeModel gm = new GenerativeModel(
                "gemini-pro",
                apiKey,
                generationConfig,
                Collections.singletonList(harassmentSafety)
        );

        return GenerativeModelFutures.from(gm);
    }
}