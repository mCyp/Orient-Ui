package com.orient.me.widget.rv.adapter;

import android.view.View;

import com.orient.me.data.table.ICellItem;

public interface ITableAdapter<Data extends ICellItem> {
    /**
     * 确定具体的布局
     * @param data 数据
     * @param pos 位置
     * @return int
     */
    int getItemLayout(Data data,int pos);

    /**
     * BaseAdapter.ViewHolder
     */
    BaseAdapter.ViewHolder<Data> onCreateViewHolder(View root,int itemType);
}
