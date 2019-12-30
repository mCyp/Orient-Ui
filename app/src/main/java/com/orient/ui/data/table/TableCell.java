package com.orient.ui.data.table;

import com.orient.me.data.table.ICellItem;

public class TableCell implements ICellItem {

    private String name;
    private String value;
    private int type;
    private int row;
    private int col;
    private int rowSpan;
    private int colSpan;

    public TableCell(String name, String value, int type, int row, int col, int rowSpan, int colSpan) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.row = row;
        this.col = col;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    @Override
    public int getColSpan() {
        return colSpan;
    }
}
