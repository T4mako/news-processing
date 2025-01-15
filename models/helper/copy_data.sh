#!/bin/bash

# 设置字符编码
export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8

# copy MAXCOUNT files from each directory
MAXCOUNT=6500

# 遍历 THUCNews 目录
# shellcheck disable=SC2045
for category in $(ls data/THUCNews); do
  echo "正在处理类别: $category"

  dir="data/THUCNews/$category"
  newdir="data/thucnews1/$category"

  # 检查源目录是否存在
  if [ ! -d "$dir" ]; then
    echo "错误: 源目录不存在 - $dir"
    continue
  fi

  # 清理并创建目标目录
  if [ -d "$newdir" ]; then
    rm -rf "$newdir"
  fi
  mkdir -p "$newdir"

  # 复制文件
  COUNTER=0
  for file in $(ls "$dir" | head -n $MAXCOUNT); do
    cp "$dir/$file" "$newdir"
    COUNTER=$((COUNTER + 1))
    echo "已复制文件: $dir/$file 到 $newdir"
  done

  echo "完成: 类别 $category, 已复制 $COUNTER 个文件"
done