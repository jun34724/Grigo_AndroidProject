package com.devidea.grigoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class NotificationController {

    private boolean notificationProperty = false; //알람 유무 반환을 위한 변수.
    private ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();

    public boolean isNotificationProperty() {
        return notificationProperty;
    }

    public ArrayList<NotificationDTO> getNotificationDTOS() {
        return notificationDTOS;
    }

    public void getNotification() {

        retrofitService.getNotification().enqueue(new Callback<ArrayList<NotificationDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationDTO>> call, Response<ArrayList<NotificationDTO>> response) {
                notificationProperty = !response.body().isEmpty();
                notificationDTOS = response.body();

            }

            @Override
            public void onFailure(Call<ArrayList<NotificationDTO>> call, Throwable t) {
            }
        });

    }

    public void getPostBody(Long postId) {

        retrofitService.getPostBody(postId).enqueue(new Callback<PostDTO>() {

            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                Log.d("body", String.valueOf(call.request()));
                if (response.body() != null) {
                    Read(postId);
                    PostBodyFragment postBodyFragment = PostBodyFragment.newInstance(response.body());
                    ((MainActivity) postBodyFragment.requireActivity()).replaceFragment(postBodyFragment);


                }
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {

            }
        });

    }


    public void Read(Long postId) {

        retrofitService.NotificationRead(postId).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
}
