package com.example.sportfashionstore.commonbase;

public class Resource<T> {
    public enum State {
        LOADING,
        SUCCESS,
        ERROR
    }

    public final State state;
    public final T data;
    public final String message;

    private Resource(State state, T data, String message) {
        this.state = state;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(State.SUCCESS, data, "");
    }

    public static <T> Resource<T> error(String message, T data) {
        return new Resource<>(State.ERROR, data, message);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(State.LOADING, data, "");
    }
}
