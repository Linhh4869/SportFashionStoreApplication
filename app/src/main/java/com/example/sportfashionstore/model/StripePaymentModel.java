package com.example.sportfashionstore.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StripePaymentModel implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("error")
    private Error error;

    @SerializedName("client_secret")
    private String clientSecret;

    @SerializedName("secret")
    private String secret;

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

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getEphemeralKey() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public static class Error implements Serializable {
        @SerializedName("message")
        private String message;

        public String getMessage() {
            return message != null ? message : "";
        }
    }
}
