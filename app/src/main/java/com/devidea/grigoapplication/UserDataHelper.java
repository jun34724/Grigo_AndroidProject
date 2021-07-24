package com.devidea.grigoapplication;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Response;

public class UserDataHelper {

    public void setUserdata(UserDataDTO userDataDTO){
        //Log.d("성공 : ", String.valueOf(response.body()));
        //userDataDTO = new Gson().fromJson(response.body(), UserDataDTO.class);
        //userDataDTO에 Mapping된 변수들을 prefs에 저장
        PrefsHelper.write("email", userDataDTO.getEmail());
        PrefsHelper.write("name", userDataDTO.getName());
        PrefsHelper.write("student_id", userDataDTO.getStudent_id());
        PrefsHelper.write("phone", userDataDTO.getPhone());
        PrefsHelper.write("birth", userDataDTO.getBirth());
        PrefsHelper.write("sex", userDataDTO.getSex());
        PrefsHelper.write("tags",  userDataDTO.getTags());

    }

    public void getUserdata(){
        //prefs에 저장된 정보들 받아오기
    }

}
