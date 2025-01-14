package com.ruoyi.news.service.impl;

import com.ruoyi.news.domain.News;
import com.ruoyi.news.domain.NewsTable;
import com.ruoyi.news.mapper.NewsMapper;
import com.ruoyi.news.service.INewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

@Service("newsService")
public class NewsServiceImpl implements INewsService {
    @Resource(name = "newsDao")
    private NewsMapper newsMapper;

    public List<News> getAll(){
        return newsMapper.getAll();
    }

    @Override
    public int insert(News news) {
        return newsMapper.insert(news);
    }

    @Override
    public int delete(int id) {
        return newsMapper.delete(id);
    }

    @Override
    public News findById(int id) {
        return newsMapper.findById(id);
    }

    @Override
    public List<NewsTable> findAll() {
        return newsMapper.findAll();
    }

    @Override
    public int update(News news) {
        return newsMapper.update(news);
    }
}
