package com.orient.ui.data;

import com.orient.me.data.IGridItem;

public class GridItem implements IGridItem {

    public static final int TYPE_SMALL =1 ;
    public static final int TYPE_NORMAL =2 ;
    public static final int TYPE_SPECIAL =3  ;


    private String name;
    private String other;
    private int source;
    private String tag;
    private int spanSize;
    private int type;

    public GridItem(String name, String other, int source, String tag, int spanSize,int type) {
        this.name = name;
        this.other = other;
        this.source = source;
        this.tag = tag;
        this.spanSize = spanSize;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public boolean isShow() {
        return true;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
