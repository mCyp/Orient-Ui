package com.orient.ui.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.orient.me.data.IGridItem;
import com.orient.me.widget.rv.adapter.BaseAdapter;
import com.orient.ui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
