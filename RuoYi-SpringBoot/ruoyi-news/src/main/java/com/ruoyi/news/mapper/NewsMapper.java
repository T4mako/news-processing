package com.ruoyi.news.mapper;

import com.ruoyi.news.domain.News;
import com.ruoyi.news.domain.NewsTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository("newsDao")
public interface NewsMapper {
    public List<News> getAll();
    public int insert(News news);
    public int delete(int id);
    public News findById(int id);
    public List<NewsTable> findAll();
    public int update(News news);
}
