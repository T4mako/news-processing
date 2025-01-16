package com.ruoyi.news.controller;

import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.extra.tokenizer.engine.hanlp.HanLPEngine;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.news.common.R;
import com.ruoyi.news.service.INewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @date 2025/1/15 14:26
 */

@RestController
@RequestMapping("/news")
public class ProcessController {

    @Resource(name = "newsService")
    private INewsService newsService;



    // 读取停用词表
    private Set<String> loadStopwords() {
        Set<String> stopwords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("cn_stopwords.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopwords.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopwords;
    }

    // 分词统计接口
    @PostMapping("/tokenize")
    public R<Map<String, Integer>> tokenizeText(@RequestBody String text) {
        // 使用 Hutool 进行分词
        TokenizerEngine engine = new HanLPEngine();
        Result parseResult = engine.parse(text); // 获取分词结果

        // 加载停用词表
        Set<String> stopwords = loadStopwords();

        // 过滤标点符号、空格、数字和停用词，并统计词频
        Map<String, Integer> wordFrequency = new HashMap<>();
        Pattern punctuationPattern = Pattern.compile("\\p{P}"); // 匹配所有标点符号
        Pattern digitPattern = Pattern.compile("\\d+"); // 匹配数字

        for (Word word : parseResult) {
            String wordText = word.getText();
            // 过滤标点符号、空格、数字、停用词、空字符串和单个字符的词
            if (!punctuationPattern.matcher(wordText).matches() &&
                    !digitPattern.matcher(wordText).matches() &&
                    !wordText.trim().isEmpty() &&
                    wordText.length() > 1 &&  // 过滤掉单个字符的词
                    !stopwords.contains(wordText)) {
                wordFrequency.put(wordText, wordFrequency.getOrDefault(wordText, 0) + 1);
            }
        }

        // 按词频从高到低排序
        Map<String, Integer> sortedWordFrequency = wordFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return R.success(sortedWordFrequency);
    }


    // 单独暴露的分类接口
    @PostMapping("/classify")
    public R classifyNews(@RequestBody String text) {
        JSONObject classify = classify(text);
        if (classify != null){
            return R.success(classify);
        }else {
            return R.error(403,"参数异常");
        }
    }

    // 分类逻辑
    private JSONObject classify(String text) {
        // 构建请求体
        JSONObject json = new JSONObject();
        json.set("text", text);

        // 发送POST请求到分类服务
        HttpResponse response = HttpRequest.post("http://127.0.0.1:5000/predict")
                .body(json.toString())
                .execute();

        // 解析响应
        if (response.isOk()) {
            JSONObject responseJson = JSONUtil.parseObj(response.body());
            return responseJson; // 假设返回的JSON中有"prediction"字段
        } else {
            return null; // 请求失败时返回未知分类
        }
    }
}