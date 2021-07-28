package com.devidea.grigoapplication;

import android.accounts.AbstractAccountAuthenticator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommentListViewer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CommentDTO> commentDTO = new ArrayList<>();

    public CommentListViewer(ArrayList<CommentDTO> comments) {
        this.commentDTO = comments;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_comment, parent, false);

        return new PostListViewer.PostListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        CommentListViewHolder commentHolder = (CommentListViewHolder) holder;

        commentHolder.comment.setText(commentDTO.get(position).getContent());
        //commentHolder.writer.setText(commentDTO.get(position).getId());
        //commentHolder.time.setText(commentDTO.get(position).getTimeStamp());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class CommentListViewHolder extends RecyclerView.ViewHolder {
            TextView comment;
            TextView writer;
            TextView time;

        public CommentListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.comment);
            writer = itemView.findViewById(R.id.writer);
            time = itemView.findViewById(R.id.time);

        }
    }

}

