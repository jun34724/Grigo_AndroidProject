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

 
    UserDataDTO userDataDTO;

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

                Headers headers = response.headers();
                String token = headers.get("Authorization");
                tokenManager.set(token);
                Log.d("token", tokenManager.get());

                //로그인 성공하면 JWT TOKEN 있는 서비스 생성
                retrofitService = ServiceGenerator.createService(RetrofitService.class, token);

                /*
                userDataDTO = new Gson().fromJson(response.body(), UserDataDTO.class);
                Log.d("info", userDataDTO.getStudent_id());
                 */

                //DTO 작동하면 아래 코드로 저장
                //PrefsHelper.write("getStudent_id",  userDataDTO.getStudent_id());

                switch (response.code()) {
                    case 213: //태그 있을경우
                        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        startActivity(new Intent(LoginActivity.this, TagInputActivity.class));
                        Toast.makeText(getApplicationContext(),"로그인 성공", Toast.LENGTH_LONG).show();
                        break;

                    case 214: // 태그 없는경우
                        startActivity(new Intent(LoginActivity.this, TagInputActivity.class));
                        break;

                    default:
                        Toast.makeText(getApplicationContext(),"알수없는 오류", Toast.LENGTH_LONG).show();
                }


                Log.d("성공 : ", String.valueOf(response.body()));
                userDataDTO = new Gson().fromJson(response.body(), UserDataDTO.class);
                //userDataDTO에 Mapping된 변수들을 prefs에 저장
                PrefsHelper.write("email",  userDataDTO.getEmail());
                PrefsHelper.write("name",  userDataDTO.getName());
                PrefsHelper.write("student_id",  userDataDTO.getStudent_id());
                PrefsHelper.write("phone",  userDataDTO.getPhone());
                PrefsHelper.write("birth",  userDataDTO.getBirth());
                PrefsHelper.write("sex",  userDataDTO.getSex());
                //PrefsHelper.write("tags",  userDataDTO.getEmail());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"통신 오류", Toast.LENGTH_LONG).show();

            }
        });


    }

}