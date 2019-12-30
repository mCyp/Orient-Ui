package com.orient.me.data.table;

public abstract class AbstractCellItem implements ICellItem {


    // 默认行或列都占一个单位
    @Override
    public int getRowSpan() {
        return 1;
    }

    @Override
    public int getColSpan() {
        return 1;
    }
}
