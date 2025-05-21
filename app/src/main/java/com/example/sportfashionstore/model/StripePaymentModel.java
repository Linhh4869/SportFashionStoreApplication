package com.example.sportfashionstore.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StripePaymentModel implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("error")
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getId() {
        return id != null ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Error implements Serializable {
        @SerializedName("message")
        private String message;

        public String getMessage() {
            return message != null ? message : "";
        }
    }
}
