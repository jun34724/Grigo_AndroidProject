package com.devidea.grigoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.Headers;
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

        //회원가입 버튼
        btn_join = findViewById(R.id.btn_join);
        btn_join.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(intent);
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

        retrofitService.login(jsonObjectLogin).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code() == 213 || response.code() == 214) {

                    String token = response.headers().get("Authorization");
                    tokenManager.set(token);
                    Log.d("token", tokenManager.get());

                    //로그인 성공하면 JWT TOKEN 있는 서비스 생성
                    retrofitService = ServiceGenerator.createService(RetrofitService.class, token);

                    if (response.code() == 213) {
                        userDataHelper.setUserdata(response);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        userDataHelper.setUserdata(response);
                        startActivity(new Intent(LoginActivity.this, TagInputActivity.class));
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "알수없는 오류", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "통신 오류", Toast.LENGTH_LONG).show();

            }
        });
    }
}