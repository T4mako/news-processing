package com.ruoyi.news.hadoop;

import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.extra.tokenizer.engine.hanlp.HanLPEngine;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.List;

public class WordCount2 {

    /**
     * Mapper 类：使用 Hutool 的 HanLPEngine 进行分词
     */
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            // 使用 Hutool 的 HanLPEngine 进行分词
            TokenizerEngine engine = new HanLPEngine();
            Result parseResult = engine.parse(value.toString());

            // 遍历分词结果
            for (Word w : parseResult) {
                String wordText = w.getText();
                word.set(w.toString()); // 获取分词结果中的单词
                context.write(word, one); // 输出 <word, 1>
            }
        }
    }

    /**
     * Reducer 类：统计词频
     */
    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    /**
     * 封装 MapReduce 作业为可调用的方法
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     * @throws Exception 异常
     */
    public static void runWordCount(String inputPath, String outputPath) throws Exception {
        // 创建 Configuration 对象并设置 HDFS 为默认文件系统
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000"); // 设置 HDFS 地址

        // 获取 HDFS 文件系统对象
        FileSystem fs = FileSystem.get(conf);

        // 检查输出路径是否存在，如果存在则删除
        Path output = new Path(outputPath);
        if (fs.exists(output)) {
            fs.delete(output, true); // true 表示递归删除
        }

        // 创建 Hadoop 作业
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 设置输入路径
        FileInputFormat.addInputPath(job, new Path(inputPath));

        // 设置输出路径
        FileOutputFormat.setOutputPath(job, output);

        // 提交作业并等待完成
        job.waitForCompletion(true);
    }
}