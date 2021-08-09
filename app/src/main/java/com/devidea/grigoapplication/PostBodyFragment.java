package com.devidea.grigoapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class PostBodyFragment extends Fragment {

    private static PostDTO postBody = new PostDTO();
    private static RecyclerView recyclerView;
    private static CommentListViewer adapter;

    private Context context = this.getContext();

    public PostBodyFragment() {
        // Required empty public constructor
    }

    public static PostBodyFragment newInstance(PostDTO postBodyInstance) {
        PostBodyFragment fragment = new PostBodyFragment();
        Bundle args = new Bundle();
        postBody = postBodyInstance;
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post_body, container, false);

        TextView title = rootView.findViewById(R.id.post_title);
        TextView content = rootView.findViewById(R.id.content);
        TextView teg = rootView.findViewById(R.id.teg);
        TextView writer = rootView.findViewById(R.id.writer);
        TextView time = rootView.findViewById(R.id.time);

        Button send = rootView.findViewById(R.id.send);
        Button option = rootView.findViewById(R.id.post_potion);

        Log.d("post_isUserCheck : ", String.valueOf(postBody.isUserCheck()));


        if (postBody.isUserCheck()) {
            option.setVisibility(View.VISIBLE);
        } else {
            option.setVisibility(View.INVISIBLE);
        }

        EditText et_comm = rootView.findViewById(R.id.input_comment);

        title.setText(postBody.getTitle());
        content.setText(postBody.getContent());
        writer.setText(postBody.getWriter());
        time.setText(postBody.getTimeStamp());
        if (postBody.getTags() != null) {
            teg.setText(String.valueOf(postBody.getTags()));
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        if (!postBody.getComments().isEmpty()) {
            adapter = new CommentListViewer(postBody.getComments());
            recyclerView.setAdapter(adapter);
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Comment = et_comm.getText().toString();
                JsonObject json = new JsonObject();
                json.addProperty("content", Comment);
                postComment(json, postBody.getId());

            }
        });

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
                                Toast.makeText(getContext(), "수정", Toast.LENGTH_LONG).show();

                                Intent postIntent = new Intent(getActivity(), PostActivity.class);
                                postIntent.putExtra("id", postBody.getId());
                                postIntent.putExtra("email", postBody.getTitle());
                                postIntent.putExtra("content", postBody.getContent());
                                postIntent.putExtra("boardtype", postBody.getBoardType());
                                postIntent.putExtra("tags", postBody.getTags());
                                System.out.println("태그 :" + postBody.getTags());
                                startActivity(postIntent);

                                break;

                            case R.id.delete:
                                Toast.makeText(getContext(), "삭제", Toast.LENGTH_LONG).show();
                                deletePost(postBody.getId());
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

                updateCommentList(postBody.getId());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //댓글 삭제
    public static void deleteComment(Long postID) {
        Log.d("delete", String.valueOf(postID));

        retrofitService.deleteComment(postID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("comment_m", (response.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("resion", String.valueOf(t.getCause()));

            }
        });

    }

    //글 삭제
    public void deletePost(Long PostID) {
        retrofitService.deletePost(PostID).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("url", String.valueOf(call.request()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("resion", String.valueOf(t.getCause()));
            }
        });
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(PostBodyFragment.this).commit();
        fragmentManager.popBackStack();
    }

    //댓글 리스트 새로고침
    public static void updateCommentList(Long postID) {
        retrofitService.getPostBody(postID).enqueue(new Callback<PostDTO>() {

            @Override
            public void onResponse(Call<PostDTO> call, Response<PostDTO> response) {
                adapter = null;
                adapter = new CommentListViewer(response.body().getComments());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {

            }
        });

    }

}