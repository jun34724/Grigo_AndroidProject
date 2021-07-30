package com.devidea.grigoapplication;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceGenerator {

    public RetrofitService retrofitService = retrofit.create(RetrofitService.class);
    public static final String BASE_URL = "http://solac.iptime.org:1234/";

    //테스트
    //public static final String BASE_URL = "http://34.64.218.179:8080/";

    //public static final String BASE_URL = "http://10.0.2.2:8080/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }
/*
    public void signup() {
        jsonObject.addProperty("email", "email");
        jsonObject.addProperty("password", "password");
        jsonObject.addProperty("name", "name");
        jsonObject.addProperty("birth", "birth");
        jsonObject.addProperty("student_id", "student_id");
        jsonObject.addProperty("sex", "sex");
        jsonObject.addProperty("phone", "phone");

        retrofitService.signup(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("서버", String.valueOf(response.body()));

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("서버", "실패");
            }
        });
    }

    public void login() {
        jsonObjectLogin = new JsonObject();

        jsonObjectLogin.addProperty("email", "solchan@gmail.com");
        jsonObjectLogin.addProperty("password", "12345678");

        retrofitService.login(jsonObjectLogin).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Headers headers = response.headers();
                String token = headers.get("Authorization");
                String[] body = token.split(" ");


                System.out.println(response.headers());

                tokenManager.set(body[1]);
                Log.d("token", tokenManager.get());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("서버", "실패");
            }
        });


    }

    public void test() {

        RetrofitService r = createService(RetrofitService.class, tokenManager.get());

        r.getToken().enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {

            }
        });

    }

 */

}