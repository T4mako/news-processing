import os
import tensorflow as tf
from cnn_model import TCNNConfig, TextCNN
from data.cnews_loader import read_category, read_vocab
from flask import Flask, request, jsonify

app = Flask(__name__)

vocab = 'models/vocab.txt'
save_path = 'models/final_model.keras'  # 确保路径正确


class CnnModel:
    def __init__(self):
        self.config = TCNNConfig()
        self.categories, self.cat_to_id = read_category()
        self.words, self.word_to_id = read_vocab(vocab)
        self.config.vocab_size = len(self.words)

        # 加载模型
        self.model = tf.keras.models.load_model(
            save_path,
            custom_objects={'TextCNN': TextCNN}  # 确保 TextCNN 类被正确加载
        )
        print("模型加载成功")

    def predict(self, message):
        # 将输入文本转换为词 ID 序列
        content = str(message)  # 确保输入为字符串
        data = [self.word_to_id[x] for x in content if x in self.word_to_id]

        # 填充序列
        padded_data = tf.keras.preprocessing.sequence.pad_sequences(
            [data], maxlen=self.config.seq_length, padding='post', truncating='post'
        )

        # 使用模型进行预测
        logits = self.model.predict(padded_data)[0]  # 获取 logits
        probabilities = tf.nn.softmax(logits).numpy()  # 将 logits 转换为概率

        # 将概率分布转换为字典
        prob_dict = {
            self.categories[i]: float(probabilities[i])  # 将概率值转换为 Python float
            for i in range(len(self.categories))
        }

        return {
            "prediction": self.categories[tf.argmax(probabilities).numpy()],  # 预测的类别
            "probabilities": prob_dict  # 每个类别的概率
        }


# 初始化模型
cnn_model = CnnModel()


@app.route('/predict', methods=['POST'])
def predict():
    # 获取请求中的 JSON 数据
    data = request.get_json()
    text = data.get('text', '')

    if not text:
        return jsonify({'error': 'No text provided'}), 400

    # 使用模型进行预测
    result = cnn_model.predict(text)

    # 返回预测结果
    return jsonify(result)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)