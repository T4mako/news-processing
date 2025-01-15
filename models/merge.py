import os

# 定义根目录
root_dir = r'E:\_毕设工厂\基于Hadoop的新闻数据处理与热点分析系统设计与实现\THU数据集\THUCNews'

# 定义输出文件路径
output_file = r'E:\_毕设工厂\基于Hadoop的新闻数据处理与热点分析系统设计与实现\THU数据集\THUCNews\all_news.txt'

# 打开输出文件，准备写入
with open(output_file, 'w', encoding='utf-8') as outfile:
    # 遍历根目录下的所有子文件夹
    for subdir, _, files in os.walk(root_dir):
        for file in files:
            # 只处理.txt文件
            if file.endswith('.txt'):
                file_path = os.path.join(subdir, file)
                # 打开每个.txt文件并读取内容
                with open(file_path, 'r', encoding='utf-8') as infile:
                    # 将文件内容写入输出文件
                    outfile.write(infile.read())
                    # 可选：在每个文件内容之间添加一个换行符
                    outfile.write('\n')

print(f"所有.txt文件已合并到 {output_file}")