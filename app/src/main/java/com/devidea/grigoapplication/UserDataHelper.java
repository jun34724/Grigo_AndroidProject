package com.devidea.grigoapplication;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

public class UserDataHelper {

    UserDataDTO userDataDTO = new UserDataDTO();


    public void setUserdata(Response<JsonObject> response){
        Log.d("성공 : ", String.valueOf(response.body()));
        userDataDTO = new Gson().fromJson(response.body(), UserDataDTO.class);
        //userDataDTO에 Mapping된 변수들을 prefs에 저장
        PrefsHelper.write("email", userDataDTO.getEmail());
        PrefsHelper.write("name", userDataDTO.getName());
        PrefsHelper.write("student_id", userDataDTO.getStudent_id());
        PrefsHelper.write("phone", userDataDTO.getPhone());
        PrefsHelper.write("birth", userDataDTO.getBirth());
        PrefsHelper.write("sex", userDataDTO.getSex());
        PrefsHelper.write("tags",  userDataDTO.getTags());
    }

    public void getTagdata(){

        //이게 맞는건가??
        //prefs에 저장된 tag 정보 받아오기
        String tags = PrefsHelper.read("tags", "");
        //맨 처음과 끝 문자열 제거
        String tags2 = tags.substring(1, tags.length()-1);
        //string으로 저장된 태그들을 list로 변환 ,로 구분해서 저장
        List<String> taglist = Arrays.asList(tags2.split(","));

        /*제대로 나오는지 출력
        for (int i = 0; i < taglist.size(); i++){
            System.out.println(taglist.get(i));
        }*/
    }

}
