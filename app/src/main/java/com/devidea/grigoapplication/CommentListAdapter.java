package com.devidea.grigoapplication;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<CommentDTO> commentDTO;
    private final PostBodyModel postBodyModel = new PostBodyModel();

    public CommentListAdapter(ArrayList<CommentDTO> comments) {
        this.commentDTO = comments;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_comment, parent, false);

        return new CommentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        CommentListViewHolder commentHolder = (CommentListViewHolder) holder;
        commentHolder.comment.setText(commentDTO.get(position).getContent());
        commentHolder.writer.setText(String.valueOf(commentDTO.get(position).getWriter()));
        commentHolder.time.setText(commentDTO.get(position).getTimeStamp());

        //본인이 작성한 글이 아닐경우 수정 버튼 노출 안함.
        if(commentDTO.get(position).isUserCheck()){
            commentHolder.btn_delete.setVisibility(View.VISIBLE);
        }
        else {
            commentHolder.btn_delete.setVisibility(View.INVISIBLE);
        }

        commentHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);//v는 클릭된 뷰를 의미

                popup.getMenuInflater().inflate(R.menu.board_inner_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.revise:
                                CommentUpdateDialog dialog = new CommentUpdateDialog(v.getContext(), commentDTO.get(position).getId(), commentDTO.get(position).getContent());
                                dialog.show();
                                break;

                            case R.id.delete:
                                Toast.makeText(v.getContext(), "삭제", Toast.LENGTH_SHORT).show();
                                postBodyModel.deleteComment(commentDTO.get(position).getId());
                                break;
                        }
                        return false;
                    }

                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentDTO.size();
    }

    static class CommentListViewHolder extends RecyclerView.ViewHolder {
        TextView comment;
        TextView writer;
        TextView time;
        Button btn_delete;

        public CommentListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.comment);
            writer = itemView.findViewById(R.id.writer);
            time = itemView.findViewById(R.id.time);
            btn_delete = itemView.findViewById(R.id.delete);


        }
    }

}

