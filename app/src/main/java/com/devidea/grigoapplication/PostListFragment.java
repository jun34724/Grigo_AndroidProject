package com.devidea.grigoapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class PostListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomRecyclerView adapter;
    private ArrayList<PostDTO> postDTOArrayList = new ArrayList<PostDTO>();
    private static String ARG_PARAM;

    private boolean isNext = true; // 다음 페이지 유무
    private Long id = 100L;       // 현재 페이지
    private final int size = 10;    // 한 번에 가져올 아이템 수

    public static PostListFragment newInstance(String title) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        ARG_PARAM = title;
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

        Button btn_write = rootView.findViewById(R.id.btn_write);
        TextView tv_title = rootView.findViewById(R.id.bulletin_board_title);
        tv_title.setText(ARG_PARAM);

        //firstGetPost();
        getPostList();
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);



                if (!recyclerView.canScrollVertically(1)) {
                    if (isNext) {
                        getPostList();;
                        recyclerView.post(new Runnable() { public void run() { adapter.notifyDataSetChanged(); } });
                    }

                }

                adapter.setOnItemClickListener(new CustomRecyclerView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        getPostBody(postDTOArrayList.get(pos).getId());
                    }
                });

            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });

        return rootView;
    }

    public void getPostList() {
        retrofitService.getQuestion(id, size).enqueue(new Callback<CursorPageDTO>() {
            @Override
            public void onResponse(Call<CursorPageDTO> call, Response<CursorPageDTO> response) {

                Log.d("url", String.valueOf(call.request()));

                if (!response.body().getPostDTOS().isEmpty()) {

                    postDTOArrayList.addAll(response.body().getPostDTOS());
                    Log.d("postlist", String.valueOf(postDTOArrayList.get(0).getContent()));
                    id = postDTOArrayList.get(postDTOArrayList.size() - 1).getId();
                    isNext = response.body().getHasNext();

                    if(adapter == null){
                        adapter = new CustomRecyclerView(postDTOArrayList);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<CursorPageDTO> call, Throwable t) {

            }
        });
    }


    public void getPostBody(Long postId) {

        retrofitService.getPostBody(postId).enqueue(new Callback<PostDTO>() {

            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                Log.d("body", String.valueOf(call.request()));
                if (response.body()!=null) {
                    PostBodyFragment postBodyFragment = PostBodyFragment.newInstance(response.body());
                    ((MainActivity) requireActivity()).replaceFragment(postBodyFragment);

                }
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {

            }
        });

    }

}