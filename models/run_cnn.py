# coding: utf-8

from __future__ import print_function

import os
import sys
import time
from datetime import timedelta

import numpy as np
import tensorflow as tf
from sklearn import metrics

from cnn_model import TCNNConfig, TextCNN  # 修改为 CNN 模型
from data.cnews_loader import read_vocab, read_category, batch_iter, process_file, build_vocab

base_dir = './helper/data/cnews'
train_dir = os.path.join(base_dir, 'cnews.train.txt')
test_dir = os.path.join(base_dir, 'cnews.test.txt')
val_dir = os.path.join(base_dir, 'cnews.val.txt')
vocab_dir = os.path.join(base_dir, 'cnews.vocab.txt')

save_dir = 'checkpoints/textcnn'  # 修改保存路径
save_path = os.path.join(save_dir, 'best_validation.weights.h5')  # 最佳验证结果保存路径


def get_time_dif(start_time):
    """获取已使用时间"""
    end_time = time.time()
    time_dif = end_time - start_time
    return timedelta(seconds=int(round(time_dif)))


def evaluate(model, x_, y_):
    """评估在某一数据上的准确率和损失"""
    loss, acc = model.evaluate(x_, y_, batch_size=128, verbose=0)
    return loss, acc


def train():
    print("Configuring TensorBoard and Saver...")
    # 配置 Tensorboard，重新训练时，请将tensorboard文件夹删除，不然图会覆盖
    tensorboard_dir = 'tensorboard/textcnn'  # 修改为 CNN
    if not os.path.exists(tensorboard_dir):
        os.makedirs(tensorboard_dir)

    # 配置 Saver
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)

    print("Loading training and validation data...")
    # 载入训练集与验证集
    start_time = time.time()
    x_train, y_train = process_file(train_dir, word_to_id, cat_to_id, config.seq_length)
    x_val, y_val = process_file(val_dir, word_to_id, cat_to_id, config.seq_length)
    time_dif = get_time_dif(start_time)
    print("Time usage:", time_dif)

    # 创建模型
    model = TextCNN(config)  # 使用 TextCNN 模型
    model.build(input_shape=(None, config.seq_length))

    # 编译模型
    model.compile(
        optimizer=tf.keras.optimizers.Adam(learning_rate=config.learning_rate),
        loss='categorical_crossentropy',
        metrics=['accuracy']
    )

    # 配置 TensorBoard 回调
    tensorboard_callback = tf.keras.callbacks.TensorBoard(log_dir=tensorboard_dir)

    # 配置 ModelCheckpoint 回调
    checkpoint_callback = tf.keras.callbacks.ModelCheckpoint(
        filepath=save_path,
        monitor='val_accuracy',
        save_best_only=True,
        save_weights_only=True,
        verbose=1
    )

    print('Training and evaluating...')
    start_time = time.time()
    best_acc_val = 0.0  # 最佳验证集准确率
    require_improvement = 1000  # 如果超过1000轮未提升，提前结束训练

    for epoch in range(config.num_epochs):
        print('Epoch:', epoch + 1)
        history = model.fit(
            x_train, y_train,
            batch_size=config.batch_size,
            epochs=1,
            validation_data=(x_val, y_val),
            callbacks=[tensorboard_callback, checkpoint_callback]
        )

        # 获取当前验证集准确率
        acc_val = history.history['val_accuracy'][0]

        # 如果验证集准确率提升，保存模型
        if acc_val > best_acc_val:
            best_acc_val = acc_val
            model.save_weights(save_path)
            improved_str = '*'
        else:
            improved_str = ''

        time_dif = get_time_dif(start_time)
        msg = 'Epoch: {0:>3}, Train Loss: {1:>6.2}, Train Acc: {2:>7.2%},' \
              + ' Val Loss: {3:>6.2}, Val Acc: {4:>7.2%}, Time: {5} {6}'
        print(msg.format(
            epoch + 1,
            history.history['loss'][0],
            history.history['accuracy'][0],
            history.history['val_loss'][0],
            history.history['val_accuracy'][0],
            time_dif,
            improved_str
        ))

        # 如果超过 require_improvement 轮未提升，提前结束训练
        if epoch > require_improvement and acc_val < best_acc_val:
            print("No optimization for a long time, auto-stopping...")
            break

    # 训练结束后手动保存最终模型
    model.save_weights('./checkpoints/textcnn/final_model.weights.h5')  # 保存权重
    model.save('./checkpoints/textcnn/final_model.keras')  # 保存整个模型


def test():
    print("Loading test data...")
    start_time = time.time()
    x_test, y_test = process_file(test_dir, word_to_id, cat_to_id, config.seq_length)

    # 加载模型
    model = tf.keras.models.load_model(
        './checkpoints/textcnn/final_model.keras',
        custom_objects={'TextCNN': TextCNN}  # 确保 TextCNN 类被正确加载
    )

    print('Testing...')
    loss_test, acc_test = evaluate(model, x_test, y_test)
    msg = 'Test Loss: {0:>6.2}, Test Acc: {1:>7.2%}'
    print(msg.format(loss_test, acc_test))

    # 预测
    y_pred = model.predict(x_test, batch_size=128)
    y_test_cls = np.argmax(y_test, axis=1)
    y_pred_cls = np.argmax(y_pred, axis=1)

    # 评估
    print("Precision, Recall and F1-Score...")
    print(metrics.classification_report(y_test_cls, y_pred_cls, target_names=categories))

    # 混淆矩阵
    print("Confusion Matrix...")
    cm = metrics.confusion_matrix(y_test_cls, y_pred_cls)
    print(cm)

    time_dif = get_time_dif(start_time)
    print("Time usage:", time_dif)


if __name__ == '__main__':
    if len(sys.argv) != 2 or sys.argv[1] not in ['train', 'test']:
        raise ValueError("""usage: python run_cnn.py [train / test]""")  # 修改为 run_cnn.py

    print('Configuring CNN model...')  # 修改为 CNN
    config = TCNNConfig()  # 使用 TCNNConfig
    if not os.path.exists(vocab_dir):  # 如果不存在词汇表，重建
        build_vocab(train_dir, vocab_dir, config.vocab_size)
    categories, cat_to_id = read_category()
    words, word_to_id = read_vocab(vocab_dir)
    config.vocab_size = len(words)

    if sys.argv[1] == 'train':
        train()
    else:
        test()