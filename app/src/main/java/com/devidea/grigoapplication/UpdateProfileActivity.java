package com.devidea.grigoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText et_updateBirth, et_updatePhone;
    Button btn_upProfile;

    UserDataDTO userDataDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        et_updateBirth = findViewById(R.id.et_updateBirth);
        et_updatePhone = findViewById(R.id.et_updatePhone);

        btn_upProfile = findViewById(R.id.btn_upProfile);
        btn_upProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfileActivity.this.upDateData(et_updateBirth.getText().toString(), et_updatePhone.getText().toString());
            }
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
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}