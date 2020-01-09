package com.orient.me.widget.rv.adapter;

import android.view.View;

import com.orient.me.data.table.ICellItem;

/**
 * 标题适配器
 */
public class TitleAdapterProxy<Data extends ICellItem> extends BaseAdapter<Data>{

    private ITableAdapter<Data> iAdapter;

    public TitleAdapterProxy(ITableAdapter<Data> iAdapter) {
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

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        super.onBindViewHolder(holder, position+1);
    }

    @Override
    public int getItemCount() {
        return mDataList.size()-1;
    }
}
