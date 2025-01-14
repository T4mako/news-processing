package com.ruoyi.news.domain;

import com.alibaba.fastjson2.annotation.JSONField;

public class News {
    @JSONField(ordinal = 0)
    private int id;
    @JSONField(ordinal = 1)
    private String title;
    @JSONField(ordinal = 2)
    private String content;
    @JSONField(ordinal = 3)
    private String time;
    @JSONField(ordinal = 4)
    private String update_time;
    @JSONField(ordinal = 5)
    private String category;
    @JSONField(ordinal = 6)
    private String push_name;
    @JSONField(ordinal = 7)
    private int click_num;

    public News(int id, String title, String content, String time, String update_time, String category, String push_name, int click_num) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.update_time = update_time;
        this.category = category;
        this.push_name = push_name;
        this.click_num = click_num;
    }

    public News() {
    }

    public News(String title, String content, String time, String category, String push_name) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.category = category;
        this.push_name = push_name;
    }

    public News(String title, String content, String time, String push_name) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.push_name = push_name;
    }

    public News(int id, String title, String content, String time, String update_time, String category, String push_name) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.update_time = update_time;
        this.category = category;
        this.push_name = push_name;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPush_name() {
        return push_name;
    }

    public void setPush_name(String push_name) {
        this.push_name = push_name;
    }

    public int getClick_num() {
        return click_num;
    }

    public void setClick_num(int click_num) {
        this.click_num = click_num;
    }


//    public String toString2() {
//        return "{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                ", time='" + time + '\'' +
//                ", update_time='" + update_time + '\'' +
//                ", category='" + category + '\'' +
//                ", push_name='" + push_name + '\'' +
//                ", click_num=" + click_num +
//                '}';
//    }
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", category='" + category + '\'' +
                ", push_name='" + push_name + '\'' +
                ", click_num=" + click_num +
                '}';
    }


}
