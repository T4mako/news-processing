package com.ruoyi.news.service;

import com.ruoyi.news.domain.News;
import com.ruoyi.news.domain.NewsTable;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface INewsService {
    public List<News> getAll();
    public int insert(News news);
    public int delete(int id);
    public News findById(int id);
    public List<NewsTable> findAll();
    public int update(News news);
    public int updateClickNum(int id, int click_num);
}
