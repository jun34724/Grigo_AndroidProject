package com.devidea.grigoapplication;

import android.os.Bundle;

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

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class PostListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private CustomRecyclerView adapter;

    private PostBodyFragment pb = new PostBodyFragment();


    public PostListFragment() {
    }


    public static PostListFragment newInstance(String param1, String param2) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_post_list, container, false);


        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        adapter = new CustomRecyclerView(list());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CustomRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ((MainActivity)getActivity()).replaceFragment(pb);
            }
        });

        return rootView;
    }


    public ArrayList list(){
        ArrayList<PostListItem> postListItem;
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PostListItem>>(){}.getType();

        postListItem = gson.fromJson(hi().getAsJsonArray("post"), type);

        return postListItem;

    }
    public ArrayList getPostList(){
        ArrayList<PostListItem> postListItem;
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PostListItem>>(){}.getType();
        postListItem = gson.fromJson(hi().getAsJsonArray("post"), type);

        return postListItem;

    }


    public JsonObject hi() {
        JsonObject obj = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        String []arr = {"1","2"};
        for (int i = 0; i < 100; i++) {
            JsonObject jsonpost = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            jsonpost.addProperty("id", i);
            jsonpost.addProperty("title", i + "번글");
            jsonpost.addProperty("writer", i+i + "쓴이");
            jsonpost.addProperty("content", i+i + "라는데 어떡하나요");
            jsonpost.addProperty("teg", i+i + "라는데 어떡하나요");
            jsonpost.addProperty("content", i+i + " 어떡하나요");
            jsonpost.addProperty("time", i+i + "qns");
            jsonArray.add(jsonpost);
        }
        obj.add("post", jsonArray);
        return obj;
    }
}