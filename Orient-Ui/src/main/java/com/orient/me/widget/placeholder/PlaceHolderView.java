package com.orient.me.widget.placeholder;

import androidx.annotation.StringRes;

/**
 * 基础的占位布局接口定义
 *
 * 不是TeaOf写的，参考自qiujuer老师
 */
public interface PlaceHolderView {

    /**
     * 没有数据
     * 显示空布局，隐藏当前数据布局
     */
    void triggerEmpty();

    /**
     * 网络错误
     * 显示一个网络错误的图标
     */
    void triggerNetError();

    /**
     * 加载错误，并显示错误信息
     *
     * @param strRes 错误信息
     */
    void triggerError(@StringRes int strRes);

    /**
     * 显示正在加载的状态
     */
    void triggerLoading();

    /**
     * 数据加载成功，
     * 调用该方法时应该隐藏当前占位布局
     */
    void triggerOk();

    /**
     * 该方法如果传入的isOk为True则为成功状态，
     * 此时隐藏布局，反之显示空数据布局
     *
     * @param isOk 是否加载成功数据
     */
    void triggerOkOrEmpty(boolean isOk);
}
