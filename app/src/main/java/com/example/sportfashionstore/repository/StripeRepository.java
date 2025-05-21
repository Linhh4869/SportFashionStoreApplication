package com.example.sportfashionstore.repository;

import androidx.annotation.NonNull;

import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.model.StripePaymentModel;
import com.example.sportfashionstore.network.ApiClient;
import com.example.sportfashionstore.network.StripeApiService;
import com.example.sportfashionstore.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StripeRepository {
    private final StripeApiService apiService;

    public StripeRepository() {
        apiService = ApiClient.getClient().create(StripeApiService.class);
    }

    public void getCustomer(DataStateCallback<StripePaymentModel> callback) {
        apiService.createCustomer().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<StripePaymentModel> call, Response<StripePaymentModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<StripePaymentModel> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void createEphemeralKey(String customerId, DataStateCallback<StripePaymentModel> callback) {
        apiService.createEphemeralKey(Constants.STRIPE_VERSION, customerId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<StripePaymentModel> call, @NonNull Response<StripePaymentModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<StripePaymentModel> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void createPaymentIntent(String customerId, int amount, String currency, DataStateCallback<StripePaymentModel> callback) {
        apiService.createPaymentIntent(customerId, amount, currency).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<StripePaymentModel> call, @NonNull Response<StripePaymentModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<StripePaymentModel> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
