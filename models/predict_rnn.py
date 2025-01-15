# coding: utf-8

from __future__ import print_function

import os
import tensorflow as tf
from cnn_model import TCNNConfig, TextCNN
from data.cnews_loader import read_category, read_vocab

base_dir = 'D:\\code\\text-classification-cnn-rnn-master\\helper\\data\\cnews'
vocab_dir = os.path.join(base_dir, 'cnews.vocab.txt')

save_dir = 'checkpoints/textrnn'
save_path = os.path.join(save_dir, 'final_model.keras')  # 最佳验证结果保存路径


class CnnModel:
    def __init__(self):
        self.config = TCNNConfig()
        self.categories, self.cat_to_id = read_category()
        self.words, self.word_to_id = read_vocab(vocab_dir)
        self.config.vocab_size = len(self.words)

        # 加载模型
        self.model = tf.keras.models.load_model(save_path)
        print("模型加载成功")

    def predict(self, message):
        # 将输入文本转换为词 ID 序列
        content = str(message)  # 确保输入为字符串
        data = [self.word_to_id[x] for x in content if x in self.word_to_id]

        # 填充序列
        padded_data = tf.keras.preprocessing.sequence.pad_sequences([data], maxlen=self.config.seq_length)

        # 使用模型进行预测
        predictions = self.model.predict(padded_data)
        y_pred_cls = tf.argmax(predictions, axis=1).numpy()[0]  # 获取预测类别

        return self.categories[y_pred_cls]


if __name__ == '__main__':
    cnn_model = CnnModel()
    test_demo = [
        '三星ST550以全新的拍摄方式超越了以往任何一款数码相机',
        '热火vs骑士前瞻：皇帝回乡二番战 东部次席唾手可得新浪体育讯北京时间3月30日7:00'
    ]
    for i in test_demo:
        print(cnn_model.predict(i))