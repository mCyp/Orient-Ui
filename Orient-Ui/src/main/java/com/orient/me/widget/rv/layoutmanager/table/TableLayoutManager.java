package com.orient.me.widget.rv.layoutmanager.table;

import android.support.v7.widget.RecyclerView;

/**
 * 二维表格LayoutManager
 *
 * 目标：
 * 1. 表格内容使用固定权重或者宽高
 * 2. 可横向或者纵向滑动
 * 3. 左侧和上侧 悬浮
 */
public class TableLayoutManager extends RecyclerView.LayoutManager {


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }
}
