package com.ruoyi.news.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce. Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class WordCount {

    /**
     * *实现Mapper，文件的每一行数据会执行一次map运算逻辑
     * * 因为输入是文件，会将处理数据的行数作为Key，这里应为LongWritable，设置为0bject也可以: Value类型为
     * Text:每一行的文件内容
     * * Mapper处理逻辑是将文件中的每一行切分为单词后，将单词作为Key，而Value则设置为1，<Word,1》
     * *因此输出类型为Text，IntWritable
     */
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
        // 事先定义好Value的值，它是IntWritable，值为1
        private final static IntWritable one = new IntWritable(1);

        // 事先定义好Text对象word，用于存储提取出来的每个单词
        private Text word = new Text() ;

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            // 将文件内容的每一行数据按照空格拆分为单词
            StringTokenizer itr = new StringTokenizer(value. toString());

            // 遍历单词，处理为<word,1>的Key-Value形式，并输出(这里会调用上下文输出到buffer缓冲区)
            while (itr.hasMoreTokens()){
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }


    /**
     * *实现Reducer
     * *接收Mapper的输出，所以Key类型为Text，Value类型为IntWritable
     * * Reducer的运算逻辑是Key相同的单词，对Value进行累加
     * *因此输出类型为Text，IntWritable，只不过IntWritable不再是1，而是最终累加结果
     */
    public static class IntSumReducer extends Reducer<Text, IntWritable,Text,IntWritable> {

        // 预先定义IntWritable对象result用于存储词频结果
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;

            // 遍历key相同单词的value值，进行累加
            for (IntWritable val : values)
                sum += val.get();
            result.set(sum);

            // 将结果输出
            context.write(key, result);
        }
    }

    // 实现Main方法
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 数据输入路径
        FileInputFormat.addInputPath(job, new Path("hdfs://localhost:9000/news/all_news.txt"));
        // 数据输出路径
        Path outputPath = new Path("file:/E:/news");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);  // true 表示递归删除
        }
        FileOutputFormat.setOutputPath(job, new Path("file:///E:/news"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}