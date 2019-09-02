package com.orient.me.data;

/**
 * 数据约束
 */
public interface IGridItem {
    /**
     * 是否启用分割线
     * @return true
     */
    boolean isShow();

    /**
     * 分类标签
     */
    String getTag();

    /**
     * 权重
     */
    int getSpanSize();
}
