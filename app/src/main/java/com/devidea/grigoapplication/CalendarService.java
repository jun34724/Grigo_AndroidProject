package com.devidea.grigoapplication;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://35.192.3.96:30001/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();



    public RetrofitService CreateService() {
        return retrofit.create(RetrofitService.class);
    }



}
