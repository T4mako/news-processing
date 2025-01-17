package com.ruoyi.news.domain;

import com.alibaba.fastjson2.annotation.JSONField;

public class NewsTable {
    @JSONField(ordinal = 0)
    private int id;
    @JSONField(ordinal = 1)
    private String title;
    @JSONField(ordinal = 2)
    private String time;
    @JSONField(ordinal = 3)
    private String category;
    @JSONField(ordinal = 4)
    private String push_name;
    @JSONField(ordinal = 5)
    private int click_num;

    public NewsTable(String title, String time, String category, String push_name) {
        this.title = title;
        this.time = time;
        this.category = category;
        this.push_name = push_name;
    }

    public NewsTable(int id, String title, String time, String category, String push_name, int click_num) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.category = category;
        this.push_name = push_name;
        this.click_num = click_num;
    }

    public int getClick_num() {
        return click_num;
    }

    public void setClick_num(int click_num) {
        this.click_num = click_num;
    }

    public NewsTable() {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "NewsTable{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", category='" + category + '\'' +
                ", push_name='" + push_name + '\'' +
                ", click_num=" + click_num +
                '}';
    }
}
