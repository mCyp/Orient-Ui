package com.orient.me.widget.rv.adapter;

import android.view.View;

import com.orient.me.data.table.ICellItem;

public class GridAdapterProxy<Data extends ICellItem> extends GridAdapter<Data> {

    private ITableAdapter<Data> iAdapter;

    public GridAdapterProxy(ITableAdapter<Data> iAdapter) {
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
