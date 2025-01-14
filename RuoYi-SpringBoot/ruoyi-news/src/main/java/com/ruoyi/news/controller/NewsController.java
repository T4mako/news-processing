package com.ruoyi.news.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.news.domain.News;
import com.ruoyi.news.domain.NewsTable;
import com.ruoyi.news.service.INewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class NewsController {
    @Resource(name = "newsService")
    private INewsService newsService;

    @ResponseBody
    @RequestMapping("/news/getAll")
    public String getAll() {
        List<NewsTable> news = newsService.findAll();
        String json = JSON.toJSONString(news);
        return json;
    }

    @ResponseBody
    @RequestMapping("/news/insertNews")
    public String insert(@RequestBody Map<String,String> map) {
        String title= map.get("title");
        String content= map.get("content");
        String time= map.get("time");
        String push=map.get("push_name");
        News news=new News(title,content,time,push);
        int n=newsService.insert(news);
        System.out.println(n);
        if(n==1){
            return "success";
        }else {
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping("/news/deleteNews")
    public String delete(@RequestBody Map<String,String> map) {
        String sid=map.get("id");
        int id=Integer.parseInt(sid);
        int n=newsService.delete(id);
        if(n==1){
            return "success";
        }else {
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping("/news/getNewsById")
    public String getNewsById(@RequestBody Map<String,String> map) {
        String sid=map.get("id");
        int id=Integer.parseInt(sid);
        News news=newsService.findById(id);
        String json = JSON.toJSONString(news);
        return json;
    }

    @ResponseBody
    @RequestMapping("/news/updateNews")
    public String updateNews(@RequestBody Map<String,String> map) {
        String sid=map.get("id");
        int id=Integer.parseInt(sid);
        String title= map.get("title");
        String content= map.get("content");
        String time= map.get("time");
        String push=map.get("push_name");
        String category=map.get("category");
        String update_time=getTime();
        News news=new News(id,title,content,time,update_time,category,push);
        int n=newsService.update(news);
        if(n==1){
            return "success";
        }else {
            return "fail";
        }

    }

    public String getTime() {
        // 获取当前的年月日
        LocalDate currentDate = LocalDate.now();
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将日期转换为字符串
        String dateString = currentDate.format(formatter);
        return dateString;
    }

}
