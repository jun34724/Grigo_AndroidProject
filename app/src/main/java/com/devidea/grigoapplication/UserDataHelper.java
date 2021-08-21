package com.devidea.grigoapplication;

import java.util.Arrays;
import java.util.List;

public class UserDataHelper {

    public void setUserdata(UserDataDTO userDataDTO){
        //userDataDTO에 Mapping된 변수들을 prefs에 저장
        PrefsHelper.write("email", userDataDTO.getEmail());
        PrefsHelper.write("name", userDataDTO.getName());
        PrefsHelper.write("student_id", userDataDTO.getStudent_id()+"");
        PrefsHelper.write("phone", userDataDTO.getPhone());
        PrefsHelper.write("birth", userDataDTO.getBirth());
        PrefsHelper.write("sex", userDataDTO.getSex());
        PrefsHelper.write("tags",  userDataDTO.getTags());

    }

    public List<String> getTagdata(){

        //prefs에 저장된 tag 정보 받아오기
        String tags = PrefsHelper.read("tags", "");

        //문자열 제거
        String tags2 = tags.substring(1, tags.length()-1).replace(" ","");

        //string으로 저장된 태그들을 list로 변환 ,로 구분해서 저장
        List<String> taglist = Arrays.asList(tags2.split(","));

        return taglist;
    }

}
