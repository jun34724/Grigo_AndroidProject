package com.devidea.grigoapplication;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

//사용될 URL 저장. URL 확정이 안되있음으로 추후 변경
public interface RetrofitService {

    @Headers("Content-Type: application/json")
    @POST("join")
    Call<JsonObject> signup(@Body JsonObject param);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<UserDataDTO> login(@Body JsonObject param);

    @Headers("Content-Type: application/json")
    @POST("tag/setting")
    Call<JsonObject> tagPost(@Body JsonObject param);

    @GET("tag/setting")
    Call<JsonObject> tagGet();

    //생일, 전화번호 수정
    @Headers("Content-Type: application/json")
    @POST("settings/profile")
    Call<JsonObject> updateProfile(@Body JsonObject param);

    //비밀번호 수정
    @Headers("Content-Type: application/json")
    @POST("settings/password")
    Call<JsonObject> updatePass(@Body JsonObject param);

    @GET("posts/question")
    Call<CursorPageDTO> getQuestion(
            @Query("page")
            int page,
            @Query("limit")
            int limit
    );

    @GET("posts")
    Call<PostDTO> getPostBody(
            @Query("postId")
                    int postId
    );

    //게시글 등록
    @Headers("Content-Type: application/json")
    @POST("posts/save")
    Call<JsonObject> writePost(@Body JsonObject param);

}
