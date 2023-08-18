package com.zam.sidik_padang.api;


import java.util.concurrent.TimeUnit;

import com.zam.sidik_padang.util.Config;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    private static Injection instances;
    private final ApiService service;

    private Injection() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.valueOf("url"));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient =
                new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(logging)
                        .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_PADANG)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(ApiService.class);
    }

    public static Injection getInstance() {
        if (instances == null)
            instances = new Injection();
        return instances;
    }

    public ApiService getService() {
        return service;
    }

}
