package com.devidea.grigoapplication;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//사용될 URL 저장. URL 확정이 안되있음으로 추후 변경
public interface RetrofitService {

    @Headers("Content-Type: application/json")
    @POST("join")
    Call<JsonObject> signup(@Body JsonObject param);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<JsonObject> login(@Body JsonObject param);

    @POST("test")
    Call<JsonObject> test(@Body JsonObject param);

    @POST("test")
    Call<Map<String, String>> getToken();

    //생일, 전화번호 수정
    @Headers("Content-Type: application/json")
    @POST("settings/profile")
    Call<JsonObject> updateProfile(@Body JsonObject param);

    //비밀번호 수정
    @Headers("Content-Type: application/json")
    @POST("settings/password")
    Call<JsonObject> updatePass(@Body JsonObject param);

}
