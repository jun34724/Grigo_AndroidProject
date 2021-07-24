package com.devidea.grigoapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.ViewHolder> {

    private final ArrayList<PostListItem> arrayList;
    private static OnItemClickListener mListener = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public CustomRecyclerView(ArrayList<PostListItem> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_post_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerView.ViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.content.setText(arrayList.get(position).getContent());
        holder.teg.setText(arrayList.get(position).getTag());
        holder.writer.setText(arrayList.get(position).getWriter());
        holder.time.setText(arrayList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView content;
        private final TextView teg;
        private final TextView writer;
        private final TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    //Log.d("position", String.valueOf(pos));

                    if (pos != RecyclerView.NO_POSITION){
                        if (mListener != null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
            title = (TextView)itemView.findViewById(R.id.post_title);
            content = (TextView)itemView.findViewById(R.id.content);
            teg = (TextView)itemView.findViewById(R.id.teg);
            writer = (TextView)itemView.findViewById(R.id.writer);
            time = (TextView)itemView.findViewById(R.id.time);
        }
    }


}
