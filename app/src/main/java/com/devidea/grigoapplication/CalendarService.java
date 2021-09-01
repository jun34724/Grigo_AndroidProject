package com.devidea.grigoapplication;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://104.197.49.222:30002/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RetrofitService CreateService() {
        return retrofit.create(RetrofitService.class);
    }


}
