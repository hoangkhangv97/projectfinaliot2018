package com.group3.androidthings.Network;

public interface ApiResponseListener<T> {

    void onSuccess(T response);

    void onError(String error);
}
