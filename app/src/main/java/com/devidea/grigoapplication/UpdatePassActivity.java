package com.devidea.grigoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePassActivity extends AppCompatActivity {

    ServiceGenerator serviceGenerator;
    RetrofitService retrofitService;
    TokenManager tokenManager;

    EditText et_currentPass, et_updatePass, et_updateRepass;
    Button btn_upPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass);

        serviceGenerator = new ServiceGenerator();
        tokenManager = new TokenManager();
        retrofitService = serviceGenerator.createService(RetrofitService.class,tokenManager.get());

        et_currentPass = findViewById(R.id.et_currentPass);
        et_updatePass = findViewById(R.id.et_updatePass);
        et_updateRepass = findViewById(R.id.et_updateRepass);

        btn_upPass = findViewById(R.id.btn_upPass);

        btn_upPass.setOnClickListener(view -> {
            //변경할 비밀번호가 8자리 이하인 경우
            if(et_updatePass.getText().toString().length() < 8 || et_updatePass.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "비밀번호가 8자리 이상이어야 합니다.",Toast.LENGTH_SHORT).show();
            }
            else {
                upDatePass(et_currentPass.getText().toString(),et_updatePass.getText().toString(),et_updateRepass.getText().toString());
            }
        });
    }

    public void upDatePass(String currentPass, String newPass, String newPassConfirm){
        JsonObject jsonObjectPass = new JsonObject();

        jsonObjectPass.addProperty("currentPassword", currentPass);
        jsonObjectPass.addProperty("newPassword", newPass);
        jsonObjectPass.addProperty("newPasswordConfirm", newPassConfirm);

        retrofitService.updatePass(jsonObjectPass).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Log.d("성공 : ", String.valueOf(response.body()));
                }
                else{
                    Log.d("실패 : ", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("실패 : ", t.getMessage());
            }
        });
    }

}