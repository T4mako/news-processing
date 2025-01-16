import tensorflow as tf
from tensorflow.keras import layers

class TRNNConfig(object):
    """RNN配置参数"""

    # 模型参数
    embedding_dim = 64      # 词向量维度
    seq_length = 600        # 序列长度
    num_classes = 14        # 类别数
    vocab_size = 5000       # 词汇表大小

    num_layers = 2          # 隐藏层层数
    hidden_dim = 128        # 隐藏层神经元
    rnn = 'gru'             # lstm 或 gru

    dropout_keep_prob = 0.8 # dropout保留比例
    learning_rate = 1e-3    # 学习率

    batch_size = 128        # 每批训练大小
    num_epochs = 8         # 总迭代轮次

    print_per_batch = 100   # 每多少轮输出一次结果
    save_per_batch = 10     # 每多少轮存入tensorboard

class TextRNN(tf.keras.Model):
    """文本分类，RNN模型"""
    def __init__(self, config, **kwargs):
        super(TextRNN, self).__init__(**kwargs)  # 处理 Keras 默认参数
        self.config = config

        # 词向量映射
        self.embedding = layers.Embedding(
            input_dim=self.config.vocab_size,
            output_dim=self.config.embedding_dim,
            input_length=self.config.seq_length
        )

        # RNN 层
        if self.config.rnn == 'lstm':
            self.rnn_cells = [
                layers.LSTM(self.config.hidden_dim, return_sequences=True, return_state=True)
                for _ in range(self.config.num_layers)
            ]
        else:  # gru
            self.rnn_cells = [
                layers.GRU(self.config.hidden_dim, return_sequences=True, return_state=True)
                for _ in range(self.config.num_layers)
            ]

        # Dropout 层
        self.dropout = layers.Dropout(self.config.dropout_keep_prob)

        # 全连接层
        self.fc1 = layers.Dense(self.config.hidden_dim, activation='relu')
        self.fc2 = layers.Dense(self.config.num_classes, activation='softmax')

    def call(self, inputs, training=False):
        # 词向量映射
        x = self.embedding(inputs)

        # RNN 层
        for rnn_cell in self.rnn_cells:
            x, *state = rnn_cell(x)

        # 取最后一个时序输出作为结果
        x = x[:, -1, :]

        # Dropout
        x = self.dropout(x, training=training)

        # 全连接层
        x = self.fc1(x)
        logits = self.fc2(x)

        return logits

    def get_config(self):
        """返回模型的配置，用于保存和加载模型"""
        config = super(TextRNN, self).get_config()
        config.update({
            'config': self.config  # 保存自定义配置
        })
        return config

    @classmethod
    def from_config(cls, config):
        """从配置中恢复模型实例"""
        custom_config = config.pop('config')  # 提取自定义配置
        return cls(custom_config, **config)  # 创建模型实例