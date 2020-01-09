package com.orient.me.widget.rv.adapter;

import android.view.View;

import com.orient.me.data.table.ICellItem;

/**
 * 标题适配器
 */
public class LeftAdapterProxy<Data extends ICellItem> extends BaseAdapter<Data>{

    private ITableAdapter<Data> iAdapter;

    public LeftAdapterProxy(ITableAdapter<Data> iAdapter) {
        this.iAdapter = iAdapter;
    }

    @Override
    public ViewHolder<Data> onCreateViewHolder(View root, int viewType) {
        return iAdapter.onCreateViewHolder(root,viewType);
    }

    @Override
    public int getItemLayout(Data data, int position) {
        return iAdapter.getItemLayout(data,position);
    }



}
