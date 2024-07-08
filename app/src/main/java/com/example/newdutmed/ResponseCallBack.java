package com.example.newdutmed;

public interface ResponseCallBack {
    void onResponse(String response);

    void onError(Throwable throwable);
}
