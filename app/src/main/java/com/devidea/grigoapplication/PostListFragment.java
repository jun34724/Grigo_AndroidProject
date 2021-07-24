package com.devidea.grigoapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class PostListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomRecyclerView adapter;

    //private int totalCount = 0; // 전체 아이템 개수
    private boolean isNext = true; // 다음 페이지 유무
    private int page = 0;       // 현재 페이지
    private final int limit = 10;    // 한 번에 가져올 아이템 수

    private PostBodyFragment pb = new PostBodyFragment();
    /*

    ArrayList<PostListDTO> postListDTOS = new ArrayList<PostListDTO>();
    
    public ArrayList<PostListDTO> pp(){

        for(int i=0; i<100; i++) {
            postListDTOS.add(new PostListDTO(i, i + "a", i + "a", i + "a", i + "a", null, null, null));
        }
        return postListDTOS;
    }

     */

    public PostListFragment() {
    }


    public static PostListFragment newInstance(String param1, String param2) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post_list, container, false);


        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {
                    if (isNext) {
                        getPostList();
                        adapter.notifyDataSetChanged();

                    }

                }

            }
        });

        getPostList();

        return rootView;
    }

    public void getPostList() {

        retrofitService.getQuestionList(page, limit).enqueue(new Callback<ArrayList<PostListDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<PostListDTO>> call, Response<ArrayList<PostListDTO>> response) {
                if (response.body() != null) {
                    page = response.body().get(response.body().size() - 1).getId();

                    adapter = new CustomRecyclerView(response.body());
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new CustomRecyclerView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int pos) {
                            ((MainActivity) getActivity()).replaceFragment(pb);
                        }
                    });

                } else {
                    isNext = false;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PostListDTO>> call, Throwable t) {

            }
        });

    }

}