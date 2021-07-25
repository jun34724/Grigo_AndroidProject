package com.devidea.grigoapplication;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PostDTO {
    private int id;
    private String title;
    private String writer;
    private String content;
    private String boardType;
    private ArrayList<String> tag = new ArrayList<>();
    private ArrayList<CommentDTO> comments = new ArrayList<>();
    private LocalDateTime timeStamp;

    public PostDTO(int id, String title, String writer, String content, String boardType, ArrayList<String> tag, ArrayList<CommentDTO> comments, LocalDateTime timeStamp) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.boardType = boardType;
        this.tag = tag;
        this.comments = comments;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
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

    /*
    public String getTimeStamp() {
        return String.valueOf(timeStamp);
    }

     */


    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}