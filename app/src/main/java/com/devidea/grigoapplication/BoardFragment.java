package com.devidea.grigoapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BoardFragment extends Fragment {
    private View view;
    private Button btn_question, btn_free;

    public BoardFragment(){

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_board, container, false);

        btn_question = view.findViewById(R.id.btn_Question);
        btn_free = view.findViewById(R.id.btn_Free);

        btn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                QuestionFragment questionFragment = new QuestionFragment();
                transaction.replace(R.id.frame2, questionFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btn_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                FreeFragment freeFragment = new FreeFragment();
                transaction.replace(R.id.frame2, freeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}
