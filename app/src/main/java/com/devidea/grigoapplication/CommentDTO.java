package com.devidea.grigoapplication;

public class CommentDTO {
    private Long id;
    private String content;
    private String timeStamp;
    private boolean userCheck = false;

    public boolean isUserCheck() {
        return userCheck;
    }

    public void setUserCheck(boolean userCheck) {
        this.userCheck = userCheck;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }



}
