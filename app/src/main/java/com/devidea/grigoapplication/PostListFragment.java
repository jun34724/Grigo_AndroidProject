package com.devidea.grigoapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ArrayList<PostDTO> postlist = new ArrayList<>();
    private static String ARG_PARAM;

    private boolean isNext = true; // 다음 페이지 유무
    private int page = 0;       // 현재 페이지
    private final int limit = 10;    // 한 번에 가져올 아이템 수

    private final PostBodyFragment postBodyFragment = new PostBodyFragment();

    public static PostListFragment newInstance(String title) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM, title);
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

        getPostList();

        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        adapter = new CustomRecyclerView(postlist);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CustomRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ((MainActivity) requireActivity()).replaceFragment(postBodyFragment);
            }
        });

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

                adapter.setOnItemClickListener(new CustomRecyclerView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        ((MainActivity) requireActivity()).replaceFragment(postBodyFragment);
                    }
                });

            }
        });

        getPostList();

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });

        return rootView;
    }

    public void getPostList() {

        retrofitService.getQuestion(page, limit).enqueue(new Callback<CursorPageDTO>() {
            @Override
            public void onResponse(Call<CursorPageDTO> call, Response<CursorPageDTO> response) {
                postlist.addAll(response.body().getPostDTOS());
                page = postlist.get(postlist.size() - 1).getId();

                isNext = response.body().getHasNext();

            }

            @Override
            public void onFailure(Call<CursorPageDTO> call, Throwable t) {

            }
        });

    }

}