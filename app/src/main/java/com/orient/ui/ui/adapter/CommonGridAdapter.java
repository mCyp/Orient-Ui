package com.orient.ui.ui.adapter;

import android.view.View;

import com.orient.me.data.table.ICellItem;
import com.orient.me.widget.rv.adapter.BaseAdapter;
import com.orient.me.widget.rv.adapter.GridAdapter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class CommonGridAdapter<Data extends ICellItem> extends GridAdapter<Data> {

    @Override
    protected void doWithRoot(BaseAdapter.ViewHolder viewHolder, View root) {
        super.doWithRoot(viewHolder, root);

        ((CommonGridAdapter.ViewHolder)viewHolder).unbinder = ButterKnife.bind(viewHolder,root);
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
