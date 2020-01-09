package com.orient.me.widget.rv.adapter;

import android.view.View;

import com.orient.me.data.table.ICellItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 表格适配器
 */
public abstract class TableAdapter<Data extends ICellItem> implements ITableAdapter<Data> {
    private List<Data> mDataList;
    private IAdapterProxy<Data> titleAdapter;
    private IAdapterProxy<Data> leftAdapter;
    private IAdapterProxy<Data> tableAdapter;


    public TableAdapter(List<Data> mDataList) {
        this.mDataList = mDataList;
    }

    void setTitleAdapter(IAdapterProxy<Data> titleAdapter, IAdapterProxy<Data> leftAdapter, IAdapterProxy<Data> tableAdapter) {
        this.titleAdapter = titleAdapter;
        this.leftAdapter = leftAdapter;
        this.tableAdapter = tableAdapter;

        init();
    }

    void init() {
        if (mDataList.size() == 0)
            return;

        List<Data> titles = new LinkedList<>();
        List<Data> lefts = new LinkedList<>();
        List<Data> contents = new LinkedList<>();

        for (Data data : mDataList) {
            int row = data.getRow();
            int col = data.getCol();

            if (row == 0) {
                titles.add(data);
            } else if (col == 0) {
                lefts.add(data);
            } else {
                contents.add(data);
            }
        }

        mDataList.clear();
    }

    public void addList(List<Data> data) {
        if (data == null || data.size() == 0)
            return;
        mDataList.addAll(data);
        init();
    }

    /**
     * 设置监听器
     */
    public void setAdapterListener(BaseAdapter.AdapterListener<Data> listener) {
        if (titleAdapter != null)
            titleAdapter.setAdapterListener(listener);

        if (leftAdapter != null) {
            leftAdapter.setAdapterListener(listener);
        }

        if (tableAdapter != null) {
            tableAdapter.setAdapterListener(listener);
        }
    }


}
