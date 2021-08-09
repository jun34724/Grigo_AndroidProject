package com.devidea.grigoapplication;

import java.util.ArrayList;

public class PostDTO {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private String boardType;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<CommentDTO> comments = new ArrayList<>();
    private ArrayList<String> addTags = new ArrayList<>();
    private ArrayList<String> deleteTags = new ArrayList<>();
    private String timeStamp;
    private boolean userCheck = true;

    public PostDTO() {
    }

    public PostDTO(String title) {
        this.title = title;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentDTO> comments) {
        this.comments = comments;
    }

    //교체 필요
    public String getTimeStamp() {
        return String.valueOf(timeStamp);
    }

    public ArrayList<String> getAddTags() { return addTags; }

    public void setAddTags(ArrayList<String> addTags) { this.addTags = addTags; }

    public ArrayList<String> getDeleteTags() { return deleteTags; }

    public void setDeleteTags(ArrayList<String> deleteTags) { this.deleteTags = deleteTags;}

    /*
    public String getTimeStamp() {
        return String.valueOf(timeStamp);
    }

     */


    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}