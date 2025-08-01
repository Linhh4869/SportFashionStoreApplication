package com.example.sportfashionstore.di;

import static com.example.sportfashionstore.util.Constants.SK_KEY;
import static com.example.sportfashionstore.util.Constants.STRIPE_URL;

import com.example.sportfashionstore.network.StripeApiService;
import com.example.sportfashionstore.util.Helper;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain -> chain.proceed(
                        chain.request().newBuilder()
                                .header("Authorization", "Bearer " + Helper.decodeBase64ToString(SK_KEY))
                                .build()
                ))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideStripeRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(STRIPE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public StripeApiService provideStripeApiService(Retrofit retrofit) {
        return retrofit.create(StripeApiService.class);
    }
}
