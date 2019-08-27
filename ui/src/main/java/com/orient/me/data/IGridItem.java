package com.orient.me.data;

/**
 * 网格的接口
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
     * 占宽权重
     */
    int getSpanSize();
}
