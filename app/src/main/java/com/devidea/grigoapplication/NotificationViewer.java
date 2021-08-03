package com.devidea.grigoapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NotificationViewer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private final ArrayList<NotificationDTO> notificationDTOS;

    public NotificationViewer(ArrayList<NotificationDTO> notificationDTOS) {
        this.notificationDTOS = notificationDTOS;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_notification, parent, false);

        return new NotificationViewer.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;

        notificationViewHolder.tag.setText(notificationDTOS.get(position).getTag());

    }

    @Override
    public int getItemCount() {
        return notificationDTOS.size();
    }


    static class NotificationViewHolder extends RecyclerView.ViewHolder {
    TextView tag;
    /*
    TextView writer;
    TextView time;
    Button btn_delete;

     */

    public NotificationViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        tag = itemView.findViewById(R.id.tag);
        /*
        writer = itemView.findViewById(R.id.writer);
        time = itemView.findViewById(R.id.time);
        btn_delete = itemView.findViewById(R.id.delete);

         */


    }
}

}