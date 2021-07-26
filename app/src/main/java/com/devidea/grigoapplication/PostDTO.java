package com.devidea.grigoapplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDTO {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private String boardType;
    private List<String> tag = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();
    private String timeStamp;

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

    public List<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentDTO> comments) {
        this.comments = comments;
    }

    //교체 필요
    public String getTimeStamp() {
        return String.valueOf(timeStamp);
    }

    /*
    public String getTimeStamp() {
        return String.valueOf(timeStamp);
    }

     */


    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}