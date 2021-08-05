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
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

//글 List를 가져와 보여주는 프래그먼트
public class PostListFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostListViewer adapter;
    private static ArrayList<PostDTO> postDTOArrayList = new ArrayList<PostDTO>(); //가져온 게시글 리스트를 저장할 DTO array


    private static String boardTitle; // 생성할 게시판 유뮤
    private static String boardType;
    private boolean isNext = true; // 다음 페이지 유무
    private Long id = 100L;       // 현재 페이지
    private final int size = 10;    // 한 번에 가져올 아이템 수

    public PostListFragment() {
    }

    public static PostListFragment newInstance(String title) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        boardTitle = title;

        if (title.equals("질문게시판")) {
            boardType = "question";
        } else {
            boardType = "free";
        }
        postDTOArrayList.clear();
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
        tv_title.setText(boardTitle);

        getPostList();
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_post_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        //recyclerview의 스크롤 리스너. 스크롤 최하단 감지시 게시글 리스트 요청
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {
                    if (isNext) {
                        getPostList();
                        recyclerView.post(new Runnable() {
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                }

                //터치된 리스트의 포지션을 이용해 게시글의 id 확인, 해당 id의 게시글을 받아 postbody 프래그먼트로 전달
                adapter.setOnItemClickListener(new PostListViewer.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        getPostBody(postDTOArrayList.get(pos).getId());
                    }
                });

            }
        });

        //글 작성 activity로 이동하는 버튼
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postIntent = new Intent(getActivity(), PostActivity.class);
                postIntent.putExtra("id", 0L);
                startActivity(postIntent);
            }
        });

        return rootView;
    }

    public void getPostList() {
        retrofitService.getList(id, size, boardType).enqueue(new Callback<CursorPageDTO>() {
            @Override
            public void onResponse(Call<CursorPageDTO> call, Response<CursorPageDTO> response) {

                Log.d("url", String.valueOf(call.request()));

                try {
                    postDTOArrayList.addAll(response.body().getPostDTOS());
                    Log.d("postlist", String.valueOf(postDTOArrayList.get(0).getContent()));
                    id = postDTOArrayList.get(postDTOArrayList.size() - 1).getId();
                    isNext = response.body().getHasNext();

                    if (adapter == null) {
                        adapter = new PostListViewer(postDTOArrayList);
                        recyclerView.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<CursorPageDTO> call, Throwable t) {
                Log.d("fail", String.valueOf(t.getCause()));
            }
        });
    }


    public void getPostBody(Long postId) {

        retrofitService.getPostBody(postId).enqueue(new Callback<PostDTO>() {

            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                Log.d("body", String.valueOf(call.request()));
                if (response.body() != null) {
                    PostBodyFragment postBodyFragment = PostBodyFragment.newInstance(response.body());
                    id = 100L;
                    ((MainActivity) requireActivity()).replaceFragment(postBodyFragment);

                }
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {

            }
        });

    }

}