package com.devidea.grigoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class PostActivity extends AppCompatActivity {

    UserDataHelper userDataHelper;

    EditText et_title, et_content;
    Spinner sp_tag, sp_board;
    Button btn_save;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        btn_save = findViewById(R.id.btn_save);
        sp_board = findViewById(R.id.sp_board);
        sp_tag = findViewById(R.id.sp_tag);
        linearLayout = findViewById(R.id.layout_tag);

        //저장된 tag의 내용을 불러와서 sp_tag에 연결
        userDataHelper = new UserDataHelper();
        List<String> tagItem = userDataHelper.getTagdata();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PostActivity.this, R.layout.support_simple_spinner_dropdown_item, tagItem);
        sp_tag.setAdapter(adapter);

        //게시판 선택 (질문게시판 -> 태그 보이게, 자유게시판 -> 태그 보이지 않게 설정)
        sp_board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(sp_board.getSelectedItem().equals("질문게시판")){
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //게시글 등록버톤
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boardType = sp_board.getSelectedItem().toString();
                String title = et_title.getText().toString();
                //Edittext에 줄바꿈으로 데이터 입력시 변환
                String content = et_content.getText().toString().replace("\n","  ");
                String writer = PrefsHelper.read("name", "");

                if(sp_board.getSelectedItem().equals("질문게시판")){
                    writeQuestion(title, boardType, content, writer);
                }
                else if(sp_board.getSelectedItem().equals("자유게시판")){
                    writeFree(title, boardType, content, writer);
                }
            }
        });
    }
    //질문게시판
    public void writeQuestion(String title, String boardType, String content, String writer){

        String[] tags = sp_tag.getSelectedItem().toString().split(" ");

        JsonObject jsonObject = new JsonObject();
        JsonArray tagJsonArray = new JsonArray();
        for (int i = 0; i < tags.length; i++) {
            tagJsonArray.add(tags[i]);
        }

        jsonObject.addProperty("title", title);
        jsonObject.addProperty("boardType", boardType);
        jsonObject.addProperty("writer", writer);
        jsonObject.addProperty("content", content);
        jsonObject.add("tags", tagJsonArray);

        retrofitService.writePost(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                startActivity(new Intent(PostActivity.this, BoardFragment.class));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
    //자유게시판 tag[]
    public void writeFree(String title, String boardType, String content, String writer){

        JsonObject jsonObject = new JsonObject();
        JsonArray tagJsonArray = new JsonArray();

        jsonObject.addProperty("title", title);
        jsonObject.addProperty("boardType", boardType);
        jsonObject.addProperty("writer", writer);
        jsonObject.addProperty("content", content);
        jsonObject.add("tags", tagJsonArray);

        retrofitService.writePost(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                startActivity(new Intent(PostActivity.this, BoardFragment.class));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}