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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_join;
    private EditText et_id, et_pw;
    private CheckBox AutoLogin;

    static TokenManager tokenManager;
    UserDataHelper userDataHelper;
    public static RetrofitService retrofitService;
    public static final RetrofitService nonTokenService = ServiceGenerator.createService(RetrofitService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefsHelper.init(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenManager = new TokenManager();
        userDataHelper = new UserDataHelper();

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        AutoLogin = findViewById(R.id.check_AutoLogin);

        ActivityResultLauncher<Intent> joinResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
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

        //token을 보유하고 있고 자동로그인 check가 되어있으면 get token후 통신 서비스 생성
        if(tokenManager.get() != null && PrefsHelper.read("AutoLogin", AutoLogin.isChecked())){
            String token = tokenManager.get();
            retrofitService = ServiceGenerator.createService(RetrofitService.class, token);
            System.out.println("token : " + tokenManager.get());
            System.out.println("token : " + PrefsHelper.read("AutoLogin", AutoLogin.isChecked()));

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            ActivityCompat.finishAffinity(LoginActivity.this);
        }

        //로그인 시도
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AutoLogin.isChecked()){
                    LoginActivity.this.login(et_id.getText().toString(), et_pw.getText().toString());
                    PrefsHelper.write("AutoLogin", AutoLogin.isChecked());
                }
                else{
                    LoginActivity.this.login(et_id.getText().toString(), et_pw.getText().toString());
                    PrefsHelper.write("AutoLogin", AutoLogin.isChecked());
                }

            }

        });

    }

    //서버로 로그인 정보 전송.
    public void login(String user_id, String pw) {
        JsonObject jsonObjectLogin = new JsonObject();

        jsonObjectLogin.addProperty("email", user_id);
        jsonObjectLogin.addProperty("password", pw);

        nonTokenService.login(jsonObjectLogin).enqueue(new Callback<UserDataDTO>() {
            @Override
            public void onResponse(@NotNull Call<UserDataDTO> call, @NotNull Response<UserDataDTO> response) {

                if (response.code() == 213 || response.code() == 214) {

                    String token = response.headers().get("Authorization");
                    tokenManager.set(token);

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

                } else {
                    Toast.makeText(getApplicationContext(), "계정을 확인해주세요", Toast.LENGTH_LONG).show();
                    Log.d("실패 : ", String.valueOf(response));
                }

            }

            @Override
            public void onFailure(Call<UserDataDTO> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "통신 오류", Toast.LENGTH_LONG).show();

            }
        });

    }
}