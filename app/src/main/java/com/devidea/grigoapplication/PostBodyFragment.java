package com.devidea.grigoapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PostBodyFragment extends Fragment {

    private static PostDTO postBody = new PostDTO();

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
        TextView comment = rootView.findViewById(R.id.comment);


        title.setText(postBody.getTitle());
        content.setText(postBody.getContent());
        teg.setText((CharSequence) postBody.getTag());
        writer.setText(postBody.getWriter());
        time.setText(postBody.getTimeStamp());
        comment.setText((CharSequence) postBody.getComments());


        return rootView;

    }

}