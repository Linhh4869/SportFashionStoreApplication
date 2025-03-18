package com.example.sportfashionstore.commonbase;

public interface DataStateCallback<T> {
    void onSuccess(T data);
    void onError(String message);
}
