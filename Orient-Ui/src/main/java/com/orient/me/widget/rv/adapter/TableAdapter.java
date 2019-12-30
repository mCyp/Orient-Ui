package com.orient.me.widget.rv.adapter;

import com.orient.me.data.table.ICellItem;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class TableAdapter<Data extends ICellItem> extends BaseAdapter<Data>
        implements TableLayoutManager.CoordinateCallback {

    private HashMap<String, Integer> coordinateCache = new HashMap<>();

    @Override
    public void add(Data data) {
        if (checkData(data)) {
            int pos = getItemCount();

            int rowSpan = data.getRowSpan();
            int colSpan = data.getColSpan();
            if (rowSpan > 1 || colSpan > 1) {
                for (int i = 0; i < rowSpan; i++) {
                    for (int j = 0; j < colSpan; j++) {
                        String key = (data.getRow() + i) + "-" + (data.getCol() + j);
                        coordinateCache.put(key, pos);
                    }
                }
            } else {
                String key = data.getRow() + "-" + data.getCol();
                coordinateCache.put(key, pos);
            }
        } else
            return;
        super.add(data);
    }

    @Override
    public void addAllData(Data... datas) {
        List<Data> dataList = new LinkedList<>();
        int pos = getItemCount();
        for (int i = 0; i < datas.length; i++) {
            Data data = datas[i];
            if (checkData(data)) {
                int rowSpan = data.getRowSpan();
                int colSpan = data.getColSpan();
                if (rowSpan > 1 || colSpan > 1) {
                    for (int x = 0; x < rowSpan; x++) {
                        for (int y = 0; y < colSpan; y++) {
                            String key = (data.getRow() + x) + "-" + (data.getCol() + y);
                            coordinateCache.put(key, pos);
                        }
                    }
                } else {
                    String key = data.getRow() + "-" + data.getCol();
                    coordinateCache.put(key, pos);
                }
                dataList.add(data);
                pos++;
            }
        }
        super.addAllData(dataList);
    }

    @Override
    public void addAllData(Collection<Data> datas) {
        List<Data> dataList = new LinkedList<>();
        int pos = getItemCount();
        for (Data data : datas) {
            if (checkData(data)) {
                int rowSpan = data.getRowSpan();
                int colSpan = data.getColSpan();
                if (rowSpan > 1 || colSpan > 1) {
                    for (int x = 0; x < rowSpan; x++) {
                        for (int y = 0; y < colSpan; y++) {
                            String key = (data.getRow() + x) + "-" + (data.getCol() + y);
                            coordinateCache.put(key, pos);
                        }
                    }
                } else {
                    String key = data.getRow() + "-" + data.getCol();
                    coordinateCache.put(key, pos);
                }
                dataList.add(data);
                pos++;
            }
        }
        super.addAllData(dataList);
    }

    @Override
    public void remove() {
        coordinateCache.clear();
        super.remove();
    }

    public void remove(Data data) {
        if (checkData(data)) {
            String key = data.getRow() + "-" + data.getCol();
            coordinateCache.remove(key);
        }
    }

    @Override
    public void replace(Collection<Data> datas) {
        coordinateCache.clear();
        for (Data data : datas) {
            add(data);
        }
        super.replace(datas);
    }

    @Override
    public int covertToPosition(int row, int col) {
        String key = row + "-" + col;
        Integer num = coordinateCache.get(key);
        if (num == null)
            return -1;
        return num;
    }

    @Override
    public int[] coordinate(int pos) {
        Data data = mDataList.get(pos);
        if (data == null)
            return null;
        return new int[]{data.getRow(), data.getCol()};
    }

    @Override
    public int[] getSpanArray(int pos) {
        Data data = mDataList.get(pos);
        if (data != null) {
            int rowSpan = data.getRowSpan() <= 0 ? 1 : data.getRowSpan();
            int colSpan = data.getColSpan() <= 0 ? 1 : data.getColSpan();
            return new int[]{rowSpan, colSpan};
        } else
            return null;
    }

    private boolean checkData(Data data) {
        int row = data.getRow();
        int col = data.getCol();
        return row >= 0 && col >= 0;
    }
}
