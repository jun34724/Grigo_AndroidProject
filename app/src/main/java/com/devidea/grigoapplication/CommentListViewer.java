package com.devidea.grigoapplication;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentListViewer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<CommentDTO> commentDTO;

    public CommentListViewer(ArrayList<CommentDTO> comments) {
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

        Log.d("isUserCheck : ", String.valueOf(commentDTO.get(position).isUserCheck()));

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
                                CustomDialog dialog = new CustomDialog(v.getContext(), commentDTO.get(position).getId(), commentDTO.get(position).getContent());


                                //화면 사이즈 구하기
                                DisplayMetrics dm = v.getContext().getApplicationContext().getResources().getDisplayMetrics();
                                int width = dm.widthPixels; int height = dm.heightPixels;

                                //다이얼로그 사이즈 세팅
                                WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
                                wm.copyFrom(dialog.getWindow().getAttributes());
                                wm.width = width;
                                //wm.height = height/2;

                                //다이얼로그 Listener 세팅
                                //dialog.setDialogListener(this);

                                dialog.show();

                                break;

                            case R.id.delete:
                                Toast.makeText(v.getContext(), "삭제", Toast.LENGTH_SHORT).show();
                                PostBodyFragment.deleteComment(commentDTO.get(position).getId());
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

