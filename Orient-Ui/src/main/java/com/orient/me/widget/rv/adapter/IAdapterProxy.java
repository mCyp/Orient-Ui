package com.orient.me.widget.rv.adapter;

import com.orient.me.data.table.ICellItem;

import java.util.Collection;
import java.util.List;

public interface IAdapterProxy<Data>{
    void addAllData(Collection<Data> dataList);
    void setAdapterListener(BaseAdapter.AdapterListener<Data> listener);
}
