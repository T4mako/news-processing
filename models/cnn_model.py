import tensorflow as tf
from tensorflow.keras import layers


class TCNNConfig(object):
    """CNN配置参数"""

    embedding_dim = 64  # 词向量维度
    seq_length = 600  # 序列长度
    num_classes = 14  # 类别数
    num_filters = 256  # 卷积核数目
    kernel_size = 5  # 卷积核尺寸
    vocab_size = 5000  # 词汇表达小

    hidden_dim = 128  # 全连接层神经元

    dropout_keep_prob = 0.5  # dropout保留比例
    learning_rate = 1e-3  # 学习率

    batch_size = 128  # 每批训练大小
    num_epochs = 10  # 总迭代轮次

    print_per_batch = 100  # 每多少轮输出一次结果
    save_per_batch = 10  # 每多少轮存入tensorboard


class TextCNN(tf.keras.Model):
    """文本分类，CNN模型"""
    def __init__(self, config, **kwargs):
        super(TextCNN, self).__init__(**kwargs)  # 处理 Keras 默认参数
        self.config = config

        # 词向量映射
        self.embedding = layers.Embedding(
            input_dim=self.config.vocab_size,
            output_dim=self.config.embedding_dim,
            input_length=self.config.seq_length
        )

        # CNN 层
        self.conv = layers.Conv1D(
            filters=self.config.num_filters,
            kernel_size=self.config.kernel_size,
            activation='relu',
            padding='valid'  # 不填充，输出长度会减少
        )

        # 全局最大池化层
        self.gmp = layers.GlobalMaxPooling1D()

        # Dropout 层
        self.dropout = layers.Dropout(self.config.dropout_keep_prob)

        # 全连接层
        self.fc1 = layers.Dense(self.config.hidden_dim, activation='relu')
        self.fc2 = layers.Dense(self.config.num_classes, activation='softmax')

    def call(self, inputs, training=False):
        # 词向量映射
        x = self.embedding(inputs)  # 输入形状: (batch_size, seq_length)

        # CNN 层
        x = self.conv(x)  # 输出形状: (batch_size, seq_length - kernel_size + 1, num_filters)

        # 全局最大池化层
        x = self.gmp(x)  # 输出形状: (batch_size, num_filters)

        # Dropout
        x = self.dropout(x, training=training)

        # 全连接层
        x = self.fc1(x)  # 输出形状: (batch_size, hidden_dim)
        logits = self.fc2(x)  # 输出形状: (batch_size, num_classes)

        return logits

    def get_config(self):
        """返回模型的配置，用于保存和加载模型"""
        config = super(TextCNN, self).get_config()
        config.update({
            'config': {
                'vocab_size': self.config.vocab_size,
                'embedding_dim': self.config.embedding_dim,
                'seq_length': self.config.seq_length,
                'num_classes': self.config.num_classes,
                'num_filters': self.config.num_filters,
                'kernel_size': self.config.kernel_size,
                'hidden_dim': self.config.hidden_dim,
                'dropout_keep_prob': self.config.dropout_keep_prob,
                'learning_rate': self.config.learning_rate,
                'batch_size': self.config.batch_size,
                'num_epochs': self.config.num_epochs,
                'print_per_batch': self.config.print_per_batch,
                'save_per_batch': self.config.save_per_batch,
            }
        })
        return config

    @classmethod
    def from_config(cls, config):
        """从配置中恢复模型实例"""
        custom_config = config.pop('config')  # 提取自定义配置
        return cls(TCNNConfig(), **config)  # 创建模型实例