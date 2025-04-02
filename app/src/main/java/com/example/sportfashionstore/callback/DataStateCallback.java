package com.example.sportfashionstore.callback;

public interface DataStateCallback<T> {
    void onSuccess(T data);
    void onError(String message);
}
