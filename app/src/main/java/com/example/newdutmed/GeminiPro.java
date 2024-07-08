package com.example.newdutmed;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiPro {

    private static final Executor executor = Executors.newSingleThreadExecutor(); // Use a single thread executor for callbacks

    public static void getResponse(ChatFutures chatModel, String query, ResponseCallBack callBack) {
        Content userMessage = new Content.Builder()
                .setRole("user")
                .setBody(query)  // Try setContent // Try setMessage // Assuming setText is the correct method
                .build();

        ListenableFuture<GenerateContentResponse> response = chatModel.sendMessage(userMessage);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                callBack.onResponse(result.getText());
            }

            @Override
            public void onFailure(Throwable t) {
                callBack.onError(t);
            }
        }, executor);
    }

    public static GenerativeModelFutures getModel() {
        SafetySetting harassmentSafety = new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH);
        GenerationConfig generationConfig = new GenerationConfig.Builder()
                .temperature(0.9f)
                .topK(16)
                .topP(0.1f)
                .build();

        GenerativeModel gm = new GenerativeModel(
                "gemini-pro",
                BuildConfig.apiKey,
                generationConfig,
                Collections.singletonList(harassmentSafety)
        );

        return GenerativeModelFutures.from(gm);
    }
}
