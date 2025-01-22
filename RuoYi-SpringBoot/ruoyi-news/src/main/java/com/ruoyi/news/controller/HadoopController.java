package com.ruoyi.news.controller;

import com.ruoyi.news.hadoop.WordCount2;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.web.bind.annotation.*;
import org.apache.hadoop.conf.Configuration;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class HadoopController {


    @PostMapping("/wordcount")
    public Map<String, Integer> wordCount(@RequestBody String text) throws Exception {
        // 过滤文本的符号与数字
        String filteredText = filterText(text);
        // 将传入的文本写入 HDFS
        String inputPath = "hdfs://localhost:9000/news/input.txt";
        String outputPath = "hdfs://localhost:9000/news/output.txt";

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        FileSystem fs = null;
        fs = FileSystem.get(conf);

        // 写入输入文件
        Path inputFile = new Path(inputPath);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs.create(inputFile, true)));
        writer.write(filteredText);
        writer.close();

        // 调用 MapReduce 作业
        WordCount2.runWordCount(inputPath, outputPath);

        // 正则表达式：匹配字母、标点或数字
        Pattern pattern = Pattern.compile("[0-9\\p{Punct}]");

        // 读取输出结果
        Map<String, Integer> result = new HashMap<>();
        Path outputFile = new Path(outputPath + "/part-r-00000");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fs.open(outputFile)));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            if (parts.length >= 2) {
                String word = parts[0]; // 单词
                int count = Integer.parseInt(parts[1]); // 词频

                // 过滤条件：长度 >= 2 且不包含字母、标点或数字
                if (word.length() >= 2 && !pattern.matcher(word).find()) {
                    result.put(word, count);
                }
            }
        }
        reader.close();
        // 按词频排序
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(result.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue())); // 降序排序

        // 将排序后的结果存入 LinkedHashMap
        Map<String, Integer> sortedResult = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            sortedResult.put(entry.getKey(), entry.getValue());
        }
        return sortedResult;
    }

    /**
     * 过滤文本的符号与数字
     *
     * @param text 原始文本
     * @return 过滤后的文本
     */
    private String filterText(String text) {
        // 正则表达式：保留中文字符和常见标点符号
        String regex = "[^\u4e00-\u9fa5，。！？；：、]";
        return text.replaceAll(regex, "");
    }
}