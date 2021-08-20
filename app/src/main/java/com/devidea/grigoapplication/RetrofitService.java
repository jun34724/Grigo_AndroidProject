package com.devidea.grigoapplication;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    //TODO : 테그 요청말고 개인 profile 요청
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

    //게시판 리스트를 요청하는 uri. 마지막으로 가지고있는 게시판 ID와 한번에 가져올 size, 가저올 종류를 보내준다.
    @GET("posts/board")
    Call<CursorPageDTO> getList(
            @Query("id")
                    Long id,
            @Query("size")
                    Integer size,
            @Query("type")
                    String type

    );

    //해당 ID를 가진 게시글을 가져오는 URI
    @GET("posts/{postID}")
    Call<PostDTO> getPostBody(
            @Path("postID")
                    Long postID
    );

    //댓글 등록
    @Headers("Content-Type: application/json")
    @POST("{commentId}/comment")
    Call<ResponseBody> postComment(@Path("commentId") Long postID, @Body JsonObject param);

    //댓글 수정
    @POST("/comment/change/{commentId}")
    Call<ResponseBody> updateComment(@Path("commentId") Long postID, @Body JsonObject param);

    //댓글 삭제
    @POST("comment/{commentId}")
    Call<ResponseBody> deleteComment(@Path("commentId") Long postID);

    //게시글 등록
    @Headers("Content-Type: application/json")
    @POST("posts/save")
    Call<ResponseBody> writePost(@Body JsonObject param);

    //게시글 수정
    @Headers("Content-Type: application/json")
    @POST("posts/{postId}/update")
    Call<ResponseBody> updatePost(@Path("postId") Long postID, @Body JsonObject param);

    //게시글 삭제
    @POST("posts/{postId}/delete")
    Call<JsonObject> deletePost(@Path("postId") Long postID);

    //알람 확인
    @GET("notification")
    Call<ArrayList<NotificationDTO>> getNotification();

    //알람 읽음 요청
    @GET("notification/{postId}")
    Call<JsonObject> NotificationRead(@Path("postId") Long postID);

    //학사일정
   @GET("schedule")
   Call<ArrayList<ScheduleDTO>> getSchedule();
}
