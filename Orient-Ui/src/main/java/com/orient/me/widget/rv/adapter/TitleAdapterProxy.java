package com.orient.me.widget.rv.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.orient.me.data.table.ICellItem;

/**
 * 标题适配器
 */
public class TitleAdapterProxy<Data extends ICellItem> extends BaseAdapter<Data>{

    private ITableAdapter<Data> iAdapter;
    private int width;
    private int height;

    public TitleAdapterProxy(ITableAdapter<Data> iAdapter,int width,int height) {
        this.iAdapter = iAdapter;
        this.width = width;
        this.height = height;
    }


    @Override
    public ViewHolder<Data> onCreateViewHolder(View root, int viewType) {
        ViewHolder<Data> holder = iAdapter.onCreateViewHolder(root,viewType);
        View view = holder.itemView;
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
        return holder;
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

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }
}
