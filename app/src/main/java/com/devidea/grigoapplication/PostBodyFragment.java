package com.devidea.grigoapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class PostBodyFragment extends Fragment {

    private static PostDTO postBody = new PostDTO();
    private RecyclerView recyclerView;
    private CommentListViewer adapter;

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

    public static void deleteComment(Long postID) {
        Log.d("delete", String.valueOf(postID));

        retrofitService.deleteComment(postID).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
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
        EditText et_comm = rootView.findViewById(R.id.input_comment);


        title.setText(postBody.getTitle());
        content.setText(postBody.getContent());
        writer.setText(postBody.getWriter());
        time.setText(postBody.getTimeStamp());
        if (postBody.getTag() != null) {
            teg.setText(String.valueOf(postBody.getTag()));
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        if(!postBody.getComments().isEmpty()) {
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

        return rootView;

    }

    public void postComment(JsonObject jsonObject, Long postID) {
        Log.d("url", String.valueOf(jsonObject));
        Log.d("url", String.valueOf(postID));
        retrofitService.postComment(postID, jsonObject).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("url", String.valueOf(call.request()));



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getCause", String.valueOf(t.getCause()));
            }
        });


    }

}