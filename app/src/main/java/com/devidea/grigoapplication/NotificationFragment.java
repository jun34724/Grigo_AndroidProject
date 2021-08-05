package com.devidea.grigoapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationViewer adapter;
    private ArrayList<NotificationDTO> notifications = new ArrayList<NotificationDTO>();

    public NotificationFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notification_list, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new NotificationViewer(notifications);
        recyclerView.setAdapter(adapter);
        getNotification();


        //터치된 리스트의 포지션을 이용해 게시글의 id 확인, 해당 id의 게시글을 받아 postbody 프래그먼트로 전달
        adapter.setOnItemClickListener(new NotificationViewer.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                getPostBody(notifications.get(pos).getPostId());
            }
        });



        return rootView;
    }

    public void getNotification() {

        retrofitService.getNotification().enqueue(new Callback<ArrayList<NotificationDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationDTO>> call, Response<ArrayList<NotificationDTO>> response) {
                Log.d("hi", String.valueOf(response.body().get(0).getPostId()));
                notifications.addAll(response.body());
                adapter = new NotificationViewer(response.body());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationDTO>> call, Throwable t) {
                Log.d("bey", String.valueOf(t.getCause()));
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
                    ((MainActivity) requireActivity()).replaceFragment(postBodyFragment);

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
