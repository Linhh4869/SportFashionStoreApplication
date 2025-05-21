package com.example.sportfashionstore.network;

import com.example.sportfashionstore.model.StripePaymentModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface StripeApiService {
    @POST("v1/customers")
    Call<StripePaymentModel> createCustomer();

    @POST("v1/ephemeral_keys")
    @FormUrlEncoded
    Call<StripePaymentModel> createEphemeralKey(
            @Header("Stripe-Version") String stripeVersion,
            @Field("customer") String customerId
    );

    @POST("v1/payment_intents")
    @FormUrlEncoded
    Call<StripePaymentModel> createPaymentIntent(
            @Field("customer") String customerId,
            @Field("amount") int amount,
            @Field("currency") String currency
    );
}
