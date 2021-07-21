package com.devidea.grigoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;


public class TagInputActivity extends AppCompatActivity {
    TextInputLayout inputLayout;
    EditText et_tagInput;
    Button btn_tagSend;
    TextView tv_tagViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_input);

        inputLayout = findViewById(R.id.input_layout);
        et_tagInput = inputLayout.getEditText();
        btn_tagSend = findViewById(R.id.button);
        tv_tagViewer = findViewById(R.id.tagviewer);


        assert et_tagInput != null;
        et_tagInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().contains(" ")) {
                    inputLayout.setError("공백없이 입력해주세요");
                } else {
                    inputLayout.setError(null); // null은 에러 메시지를 지워주는 기능
                }
            }
        });


        btn_tagSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tag = et_tagInput.getText().toString().split("#");
                tagSend(tag);
                showTag();
            }

        });

    }

    private void tagSend(String[] tags) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i = 1; i < tags.length; i++) {
            jsonArray.add(tags[i]);
        }
        jsonObject.add("tags", jsonArray);
        Log.d("tags", String.valueOf(jsonObject));

        retrofitService.tagPost(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                showTag();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    public void showTag() {
        retrofitService.tagGet().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                tv_tagViewer.setText(String.valueOf(response.body()));
                PrefsHelper.write("tags", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

}