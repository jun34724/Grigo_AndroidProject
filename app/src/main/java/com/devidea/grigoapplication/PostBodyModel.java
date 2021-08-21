package com.devidea.grigoapplication;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;
import static com.devidea.grigoapplication.PostBodyFragment.getCommentList;

public class PostBodyModel {
    private static final PostListFragment postListFragment = new PostListFragment();


    //댓글 등록
    public void postComment(JsonObject jsonObject, Long postID) {
        retrofitService.postComment(postID, jsonObject).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    Log.d("comment_m", (response.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getCommentList();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("resion", String.valueOf(t.getCause()));

            }
        });
    }

    //댓글 삭제
    public void deleteComment(Long postID) {

        retrofitService.deleteComment(postID).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("comment_m", (response.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getCommentList();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("resion", String.valueOf(t.getCause()));

            }

        });
    }

    //글 삭제
    public void deletePost(Long PostID) {
        retrofitService.deletePost(PostID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("url", String.valueOf(call.request()));
                postListFragment.updatePostList();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("resion", String.valueOf(t.getCause()));
            }
        });

    }

}
