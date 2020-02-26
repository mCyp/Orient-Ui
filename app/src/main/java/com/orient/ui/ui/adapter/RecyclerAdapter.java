package com.orient.ui.ui.adapter;

import android.view.View;


import com.orient.me.widget.rv.adapter.BaseAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础的RecyclerAdapter
 *
 * Author WangJie
 * Created on 2018/8/27.
 */
@SuppressWarnings("ALL")
public abstract class RecyclerAdapter<Data> extends BaseAdapter<Data> {

    public RecyclerAdapter() {
    }

    public RecyclerAdapter(AdapterListener<Data> adapterListener) {
        super(adapterListener);
    }

    public RecyclerAdapter(List<Data> mDataList, AdapterListener<Data> adapterListener) {
        super(mDataList, adapterListener);
    }

    @Override
    protected void doWithRoot(BaseAdapter.ViewHolder viewHolder, View root) {
        super.doWithRoot(viewHolder, root);

        ((RecyclerAdapter.ViewHolder)viewHolder).unbinder = ButterKnife.bind(viewHolder,root);
    }

    /**
     *  自定义的ViewHolder
     */
    public static abstract class ViewHolder<Data> extends BaseAdapter.ViewHolder<Data>{
        public Unbinder unbinder;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
