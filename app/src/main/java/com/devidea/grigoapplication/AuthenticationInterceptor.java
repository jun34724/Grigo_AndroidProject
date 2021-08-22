package com.devidea.grigoapplication;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//로그인 성공후 JWT토큰 받아온 경우 이후 통신요청에 대해 토큰 자동 삽입.
public class AuthenticationInterceptor implements Interceptor {

    private final String authToken;

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);

        Log.d("Token", authToken);

        Request request = builder.build();
        return chain.proceed(request);
    }
}