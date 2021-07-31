package com.devidea.grigoapplication;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_join;
    private EditText et_id, et_pw;

    TokenManager tokenManager;
    UserDataHelper userDataHelper;
    ServiceGenerator serviceGenerator;
    static RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefsHelper.init(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenManager = new TokenManager();
        serviceGenerator = new ServiceGenerator();
        retrofitService = ServiceGenerator.createService(RetrofitService.class);
        userDataHelper = new UserDataHelper();

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);

        ActivityResultLauncher<Intent> joinResult =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent joinIntent = result.getData();
                            et_id.setText(joinIntent.getStringExtra("id"));
                            et_pw.setText(joinIntent.getStringExtra("password"));
                        }
                    }
                });

        //회원가입 버튼
        btn_join = findViewById(R.id.btn_join);
        btn_join.setOnClickListener(view -> {
            joinResult.launch(new Intent(LoginActivity.this, JoinActivity.class));
        });

        //로그인 시도
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginActivity.this.login(et_id.getText().toString(), et_pw.getText().toString());

            }

        });

    }

    //서버로 로그인 정보 전송.
    public void login(String user_id, String pw) {
        JsonObject jsonObjectLogin = new JsonObject();

        jsonObjectLogin.addProperty("email", user_id);
        jsonObjectLogin.addProperty("password", pw);

        retrofitService.login(jsonObjectLogin).enqueue(new Callback<UserDataDTO>() {
            @Override
            public void onResponse(@NotNull Call<UserDataDTO> call, @NotNull Response <UserDataDTO> response) {

                if (response.code() == 213 || response.code() == 214) {

                    String token = response.headers().get("Authorization");
                    tokenManager.set(token);
                    Log.d("token", tokenManager.get());

                    //로그인 성공하면 JWT TOKEN 있는 서비스 생성
                    retrofitService = ServiceGenerator.createService(RetrofitService.class, token);

                    if (response.code() == 213) {
                        userDataHelper.setUserdata(response.body());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        ActivityCompat.finishAffinity(LoginActivity.this);

                    } else {
                        userDataHelper.setUserdata(response.body());
                        startActivity(new Intent(LoginActivity.this, TagInputActivity.class));
                        ActivityCompat.finishAffinity(LoginActivity.this);
                    }

                }
                //todo 개발종료후 삭제해야 합니다.
                else if (user_id.equals("admin")|| pw.equals("admin")){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    ActivityCompat.finishAffinity(LoginActivity.this);
                }

                else {
                    Toast.makeText(getApplicationContext(), "알수없는 오류", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UserDataDTO> call, Throwable t) {

                //todo 개발종료후 삭제해야 합니다.
                if (user_id.equals("admin")|| pw.equals("admin")){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    ActivityCompat.finishAffinity(LoginActivity.this);
                }

                Toast.makeText(getApplicationContext(), "통신 오류", Toast.LENGTH_LONG).show();

            }
        });
    }
}