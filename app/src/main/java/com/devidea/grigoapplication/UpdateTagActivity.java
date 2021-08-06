package com.devidea.grigoapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class UpdateTagActivity extends AppCompatActivity {

    UserDataHelper userDataHelper;

    Button btn_plusTag, btn_upTag;
    EditText et_plusTag;
    Context context;

    LinearLayout linear_ownTag, linear_delTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tag);

        btn_plusTag = findViewById(R.id.btn_plusTag);
        btn_upTag = findViewById(R.id.btn_upTag);
        et_plusTag = findViewById(R.id.et_plusTag);
        linear_ownTag = findViewById(R.id.layout_ownTag);
        linear_delTag = findViewById(R.id.layout_delTag);

        userDataHelper = new UserDataHelper();

        //추가 태그 list
        List<String> plusTag = new ArrayList<>();

        //보유 태그 list
        List<String> ownTag = userDataHelper.getTagdata();
        context = this;

        //삭제한 태그 list
        List<String> delTag = new ArrayList<>();

        getTag(ownTag, delTag);

        //태그 추가
        btn_plusTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //추가하려는 태그가 이미 가지고 있으면 x
                if(ownTag.contains(et_plusTag.getText().toString())){
                    Toast.makeText(getApplicationContext(), "이미 존재하는 태그입니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    plusTag.add(et_plusTag.getText().toString());
                    System.out.println(plusTag);
                    addTag(et_plusTag.getText().toString());
                    Toast.makeText(getApplicationContext(), "추가되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //태그 최종 수정
        btn_upTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delTag.isEmpty() && plusTag.isEmpty()){
                    Toast.makeText(getApplicationContext(), "변경사항이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    updateTag(plusTag, delTag);
                }
            }
        });


    }

    public void deleteTag(List<String> delTag){
        linear_delTag.removeAllViews();
        //삭제한 태그
        for(int i = 0; i < delTag.size(); i++){
            Button button = new Button(context);
            button.setText(delTag.get(i));
            linear_delTag.addView(button);
        }
    }

    //태그 추가
    public void addTag(String plusTag){
        Button button = new Button(context);
        button.setAllCaps(false);
        button.setText(plusTag);
        linear_ownTag.addView(button);
    }

    //보유한 태그 버튼으로 보여주기 -> 생성된 버튼 삭제 가능.
    public void getTag(List<String> ownTag, List<String> delTag){
        //보유한 태그로 버튼 생성 -> 태그 클릭시 삭제가 가능한 리스너 실행

        for(int i = 0; i < ownTag.size(); i++){
            Button button = new Button(context);
            //버튼 대소문자 구별
            button.setAllCaps(false);
            button.setText(ownTag.get(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTagActivity.this);
                    builder.setTitle("태그 삭제").setMessage("태그를 삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_SHORT).show();
                            delTag.add((String) button.getText());
                            deleteTag(delTag);
                            linear_ownTag.removeView(button);
                            //System.out.println("삭제 태그 : " + delTag);
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            linear_ownTag.addView(button);
        }
    }

    public void updateTag(List<String> plusTag, List<String> delTag){
        JsonObject jsonObject = new JsonObject();
        JsonArray plusJsonArray = new JsonArray();
        JsonArray delJsonArray = new JsonArray();
        for (int i = 0; i < plusTag.size(); i++){
            plusJsonArray.add(plusTag.get(i));
        }

        for (int i = 0; i < delTag.size(); i++){
            delJsonArray.add(delTag.get(i));
        }
        jsonObject.add("addTags", plusJsonArray);
        jsonObject.add("deleteTags", delJsonArray);

        retrofitService.updateProfile(jsonObject).enqueue(new Callback<JsonObject>() {
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

        System.out.println("출력 : " + jsonObject);

    }

}