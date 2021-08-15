package com.devidea.grigoapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    private TextView num_noti;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();

    private final NotificationModel notificationModel = new NotificationModel();

    public NotificationFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notification_list, container, false);

        notificationDTOS = MainActivity.notificationModel.getNotificationDTOS();

        num_noti = rootView.findViewById(R.id.num_alart);
        num_noti.setText(MainActivity.notificationModel.getNotificationDTOS().size() + " 개의 알림이 있습니다.");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new NotificationAdapter(notificationDTOS);
        recyclerView.setAdapter(adapter);

        //터치된 리스트의 포지션을 이용해 게시글의 id 확인, 해당 id의 게시글을 받아 postbody 프래그먼트로 전달
        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                num_noti.setText(notificationDTOS.size() + " 개의 알림이 있습니다.");
                notificationModel.getPostBody(notificationDTOS.get(pos).getPostId());

                notificationDTOS.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });


        return rootView;
    }


}
