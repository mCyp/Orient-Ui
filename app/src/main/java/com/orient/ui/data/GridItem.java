package com.orient.ui.data;

import com.orient.ui.widget.ISuspensionInterface;

public class GridItem implements ISuspensionInterface {

    private String name;
    private String tag;
    private int spanSize;

    public GridItem(String name, String tag, int spanSize) {
        this.name = name;
        this.tag = tag;
        this.spanSize = spanSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }

    @Override
    public String getSuspensionTag() {
        return tag;
    }
}
