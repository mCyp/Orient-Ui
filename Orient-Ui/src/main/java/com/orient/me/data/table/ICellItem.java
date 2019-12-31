package com.orient.me.data.table;

public interface ICellItem {
    // 行
    int getRow();
    // 列
    int getCol();
    // 行所占Span
    int getWidthSpan();
    // 列所占Span
    int getHeightSpan();
}
