package com.devidea.grigoapplication;

public class ScheduleDTO {
    private Integer id;
    private String date;
    private String content;
    private Integer month;
    private Integer year;

    public ScheduleDTO(int id, String date, String content, int month, int year){
        this.id = id;
        this.date = date;
        this.content = content;
        this.month = month;
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
