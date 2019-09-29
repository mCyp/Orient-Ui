package com.orient.me.data;

/**
 * 时间轴数据需要实现的接口
 */
public interface ITimeItem {
    /**
     * 构建绘制的标题
     * @return 标题
     */
    String getTitle();

    /**
     * 用户绘制原点的颜色
     * @return 颜色
     */
    int getColor();

    /**
     * 图片的资源文件
     * @return drawable的资源地址
     */
    int getResource();

}
