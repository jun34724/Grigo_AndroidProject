package com.devidea.grigoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class PostActivity extends AppCompatActivity {

    UserDataHelper userDataHelper;

    EditText et_title, et_content;
    Spinner sp_board;
    Button btn_save;
    ListView list_item;

    private static PostDTO postBody = new PostDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        btn_save = findViewById(R.id.btn_save);
        sp_board = findViewById(R.id.sp_board);
        list_item = findViewById(R.id.list_item);

        System.out.println("postBody :" + postBody.getId());

        //저장된 tag의 내용을 불러와서 sp_tag에 연결
        userDataHelper = new UserDataHelper();
        List<String> tagItem = userDataHelper.getTagdata();
        ArrayAdapter adapter = new ArrayAdapter(PostActivity.this, android.R.layout.simple_list_item_multiple_choice, tagItem);
        list_item.setAdapter(adapter);

        //게시판 선택 (질문게시판 -> 태그 보이게, 자유게시판 -> 태그 보이지 않게 설정)
        sp_board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(sp_board.getSelectedItem().equals("질문게시판")){
                    list_item.setVisibility(View.VISIBLE);
                }
                else {
                    list_item.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //게시글 수정
        Intent postIntent = getIntent();
        Long id = postIntent.getExtras().getLong("id");
        if(id != 0){
            et_title.setText(postIntent.getExtras().getString("email"));
            et_content.setText(postIntent.getExtras().getString("content"));
            if(postIntent.getExtras().getString("boardtype").equals("question")){
                sp_board.setSelection(1);
            }
            else if(postIntent.getExtras().getString("boardtype").equals("free")){
                sp_board.setSelection(2);
            }
            for(int i = 0; i < tagItem.size(); i++){
                for(int j = 0; j < postIntent.getExtras().getStringArrayList("tags").size(); j++){
                    if(tagItem.get(i).equals(postIntent.getExtras().getStringArrayList("tags").get(j))){
                        list_item.setItemChecked(i, true);
                    }
                }
            }
        }

        //게시글 등록버톤
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boardType = sp_board.getSelectedItem().toString();
                String title = et_title.getText().toString();
                //Edittext에 줄바꿈으로 데이터 입력시 변환
                String content = et_content.getText().toString().replace("\n","  ");
                String writer = PrefsHelper.read("name", "");
                ArrayList<String> tagList = new ArrayList<>();

                System.out.println("아이디 값 : " + id);

                SparseBooleanArray checkedItems = list_item.getCheckedItemPositions();
                //0번부터 리스트의 개수만큼
                for(int i = 0; i < adapter.getCount(); i++) {
                    //선택되어진 리스트 추가
                    if (checkedItems.get(i)) {
                        tagList.add(tagItem.get(i));
                    }
                }

                if(id == 0){
                    if(sp_board.getSelectedItem().equals("질문게시판")){
                        if(checkedItems.size() == 0){
                            Toast.makeText(PostActivity.this, "태그를 선택하여 주세요",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            writeQuestion(title, "question", content, writer, tagList);
                        }
                    }
                    else if(sp_board.getSelectedItem().equals("자유게시판")){
                        writeFree(title, "free", content, writer);
                    }
                }
                else {
                    if(sp_board.getSelectedItem().equals("질문게시판")){
                        updateQuestionPost(id, title, "question", content, writer, tagList);
                    }
                    else{
                        updateFreePost(id, title, "free", content, writer);
                    }
                }
            }
        });
    }
    //질문게시판
    public void writeQuestion(String title, String boardType, String content, String writer, List<String> tagList){

        JsonObject jsonObject = new JsonObject();
        JsonArray tagJsonArray = new JsonArray();
        for (int i = 0; i < tagList.size(); i++){
            tagJsonArray.add(tagList.get(i));
        }

        jsonObject.addProperty("title", title);
        jsonObject.addProperty("boardType", boardType);
        jsonObject.addProperty("writer", writer);
        jsonObject.addProperty("content", content);
        jsonObject.add("tags", tagJsonArray);

        retrofitService.writePost(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });


    }
    //자유게시판
    public void writeFree(String title, String boardType, String content, String writer){

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("title", title);
        jsonObject.addProperty("boardType", boardType);
        jsonObject.addProperty("writer", writer);
        jsonObject.addProperty("content", content);

        retrofitService.writePost(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void updateQuestionPost(Long postID, String title, String boardType, String content, String writer, List<String> tagList) {

        ArrayList<String> postTag = getIntent().getExtras().getStringArrayList("tags");

        JsonObject jsonObject = new JsonObject();

        //중복된 태그를 찾는 리스트
        List<String> tag = new ArrayList<>();

        for (int i = 0; i < postTag.size(); i++) {
            for (int j = 0; j < tagList.size(); j++) {
                if (tagList.get(j).equals(postTag.get(i))) {
                    tag.add(tagList.get(j));
                }
            }
        }
        tagList.removeAll(tag);
        postTag.removeAll(tag);

        List<String> addTag = tagList;
        List<String> delTag = postTag;

        JsonArray addtagArray = new JsonArray();
        for (int i = 0; i < addTag.size(); i++){
            addtagArray.add(addTag.get(i));
        }

        JsonArray deltagArray = new JsonArray();
        for (int i = 0; i < delTag.size(); i++){
            deltagArray.add(delTag.get(i));
        }

        jsonObject.addProperty("title", title);
        jsonObject.addProperty("boardType", boardType);
        jsonObject.addProperty("writer", writer);
        jsonObject.addProperty("content", content);
        jsonObject.add("addTags", addtagArray);
        jsonObject.add("deleteTags", deltagArray);

        System.out.println("제이슨 출력 : " + jsonObject);

        retrofitService.updatePost(postID, jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void updateFreePost(Long postID, String title, String boardType, String content, String writer) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("title", title);
        jsonObject.addProperty("boardType", boardType);
        jsonObject.addProperty("writer", writer);
        jsonObject.addProperty("content", content);

        retrofitService.updatePost(postID, jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

}