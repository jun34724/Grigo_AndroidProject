package com.devidea.grigoapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NotificationViewer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<NotificationDTO> notificationDTOS;

    private static OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

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

        notificationViewHolder.title.setText(notificationDTOS.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return notificationDTOS.size();
    }


    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title;
    /*
    TextView writer;
    TextView time;
    Button btn_delete;

     */

        public NotificationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    //Log.d("position", String.valueOf(pos));

                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

            title = itemView.findViewById(R.id.noti_title);
        /*
        writer = itemView.findViewById(R.id.writer);
        time = itemView.findViewById(R.id.time);
        btn_delete = itemView.findViewById(R.id.delete);

         */


        }
    }

}