package com.devidea.grigoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    ServiceGenerator serviceGenerator;
    RetrofitService retrofitService;
    TokenManager tokenManager;

    EditText et_updateBirth, et_updatePhone;
    Button btn_upProfile;

    UserDataDTO userDataDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        serviceGenerator = new ServiceGenerator();
        tokenManager = new TokenManager();
        retrofitService = serviceGenerator.createService(RetrofitService.class, tokenManager.get());

        et_updateBirth = findViewById(R.id.et_updateBirth);
        et_updatePhone = findViewById(R.id.et_updatePhone);

        btn_upProfile = findViewById(R.id.btn_upProfile);
        btn_upProfile.setOnClickListener(view -> {
            upDateData(et_updateBirth.getText().toString(), et_updatePhone.getText().toString());
        });
    }

    public void upDateData(String upBirth, String upPhone){

        //변경할 생년월일이 빈 값
        if(upBirth.equals("")){
            upBirth = PrefsHelper.read("birth", "");
        }
        //변경할 폰번호가 빈 값
        else if(upPhone.equals("")){
            upPhone = PrefsHelper.read("phone", "");
        }

        //변경할 생년월일, 폰번호가 모두 빈 값
        else if(upBirth.equals("") && upPhone.equals("")){
            upBirth = PrefsHelper.read("birth", "");
            upPhone = PrefsHelper.read("phone", "");
        }

        /*테스트
        PrefsHelper.write("birth",  upBirth);
        PrefsHelper.write("phone",  upPhone);
        Intent intent = new Intent(UpdateProfileActivity.this, MyPageActivity.class);
        startActivity(intent);*/

        JsonObject jsonObjectUpProfile = new JsonObject();

        jsonObjectUpProfile.addProperty("birth", upBirth);
        jsonObjectUpProfile.addProperty("phone", upPhone);

        retrofitService.updateProfile(jsonObjectUpProfile).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                userDataDTO = new Gson().fromJson(response.body(), UserDataDTO.class);
                PrefsHelper.write("birth",  userDataDTO.getBirth());
                PrefsHelper.write("phone",  userDataDTO.getPhone());

                Intent intent = new Intent(UpdateProfileActivity.this, MyPageActivity.class);
                startActivity(intent);
                //Log.d("변경 : ", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}