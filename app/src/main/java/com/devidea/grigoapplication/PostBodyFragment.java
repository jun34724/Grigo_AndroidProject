package com.devidea.grigoapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class PostBodyFragment extends Fragment {

    private static PostDTO postDTO = new PostDTO();
    private static RecyclerView recyclerView;
    private static CommentListAdapter adapter;
    private static final PostBodyModel postBodyModel = new PostBodyModel();

    public PostBodyFragment() {
    }

    public static PostBodyFragment newInstance(PostDTO postBodyInstance) {
        PostBodyFragment fragment = new PostBodyFragment();
        Bundle args = new Bundle();
        postDTO = postBodyInstance;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG ", "onCreate(), MainActivity");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post_body, container, false);

        TextView title = rootView.findViewById(R.id.post_title);
        TextView content = rootView.findViewById(R.id.content);
        TextView teg = rootView.findViewById(R.id.teg);
        TextView writer = rootView.findViewById(R.id.writer);
        TextView time = rootView.findViewById(R.id.time);

        Button send = rootView.findViewById(R.id.send);
        Button option = rootView.findViewById(R.id.post_potion);

        Log.d("post_isUserCheck : ", String.valueOf(postDTO.isUserCheck()));


        if (postDTO.isUserCheck()) {
            option.setVisibility(View.VISIBLE);
        } else {
            option.setVisibility(View.INVISIBLE);
        }

        EditText et_comm = rootView.findViewById(R.id.input_comment);

        title.setText(postDTO.getTitle());
        content.setText(postDTO.getContent());
        writer.setText(postDTO.getWriter());
        time.setText(postDTO.getTimeStamp());
        if (!String.valueOf(postDTO.getTags()).equals("[]")) {
            teg.setText(String.valueOf(postDTO.getTags()));
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        if (!postDTO.getComments().isEmpty()) {
            adapter = new CommentListAdapter(postDTO.getComments());
            recyclerView.setAdapter(adapter);
        }


        //댓글 작성버튼
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Comment = et_comm.getText().toString();
                JsonObject json = new JsonObject();
                json.addProperty("content", Comment);
                postBodyModel.postComment(json, postDTO.getId());

                et_comm.setText("");

            }
        });

        //게시물 관련 옵션
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);//v는 클릭된 뷰를 의미

                popup.getMenuInflater().inflate(R.menu.board_inner_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.revise:

                                Intent postIntent = new Intent(getActivity(), PostActivity.class);
                                postIntent.putExtra("id", postDTO.getId());
                                postIntent.putExtra("email", postDTO.getTitle());
                                postIntent.putExtra("content", postDTO.getContent());
                                postIntent.putExtra("boardtype", postDTO.getBoardType());
                                postIntent.putExtra("tags", postDTO.getTags());
                                System.out.println("태그 :" + postDTO.getTags());
                                startActivity(postIntent);

                                break;

                            case R.id.delete:
                                postBodyModel.deletePost(postDTO.getId());
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().remove(PostBodyFragment.this).commit();
                                fragmentManager.popBackStack();
                                break;
                        }
                        return false;
                    }

                });
                popup.show();
            }

        });
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        getList();
        Log.e("TAG ", "onResume(), MainActivity");
    }

    public void refreshFragment() {
        TextView content = getView().findViewById(R.id.content);
        TextView title = getView().findViewById(R.id.post_title);
        TextView teg = getView().findViewById(R.id.teg);
        TextView writer = getView().findViewById(R.id.writer);
        TextView time = getView().findViewById(R.id.time);

        title.setText(postDTO.getTitle());
        content.setText(postDTO.getContent());
        writer.setText(postDTO.getWriter());
        time.setText(postDTO.getTimeStamp());
        if (!String.valueOf(postDTO.getTags()).equals("[]")) {
            teg.setText(String.valueOf(postDTO.getTags()));
        }
    }


    //댓글 리스트 새로고침
    public static void getCommentList() {
        retrofitService.getPostBody(postDTO.getId()).enqueue(new Callback<PostDTO>() {

            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {

                adapter = new CommentListAdapter(response.body().getComments());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {

            }
        });

    }

    //본문내용 새로고침
    public void getList() {
        retrofitService.getPostBody(postDTO.getId()).enqueue(new Callback<PostDTO>() {

            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                postDTO = response.body();
                refreshFragment();
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {

            }
        });

    }

}