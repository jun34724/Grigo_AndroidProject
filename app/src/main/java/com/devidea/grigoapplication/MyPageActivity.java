package com.devidea.grigoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyPageActivity extends AppCompatActivity {

    TextView tv_email, tv_name, tv_student_id, tv_sex, tv_phone, tv_birth, tv_tag;

    Button btn_updateProfile, btn_updatePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.tv_name);
        tv_student_id = findViewById(R.id.tv_student_id);
        tv_sex = findViewById(R.id.tv_sex);
        tv_phone = findViewById(R.id.tv_phone);
        tv_birth = findViewById(R.id.tv_birth);
        tv_tag = findViewById(R.id.tv_tag);

        btn_updatePass = findViewById(R.id.btn_updatePass);
        btn_updateProfile = findViewById(R.id.btn_updateProfile);

        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, UpdateProfileActivity.class);
                MyPageActivity.this.startActivity(intent);
            }
        });

        btn_updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, UpdatePassActivity.class);
                MyPageActivity.this.startActivity(intent);
            }
        });

        getAccount();

    }

    public void getAccount(){

        tv_email.setText(PrefsHelper.read("email", ""));
        tv_name.setText(PrefsHelper.read("name", ""));
        tv_birth.setText(PrefsHelper.read("birth", ""));
        tv_student_id.setText(PrefsHelper.read("student_id", ""));
        tv_sex.setText(PrefsHelper.read("sex", ""));
        tv_phone.setText(PrefsHelper.read("phone", ""));
        //tv_tag.setText(PrefsHelper.read("tags", ""));
    }
}