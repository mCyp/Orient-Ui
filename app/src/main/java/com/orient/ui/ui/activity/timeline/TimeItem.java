package com.orient.ui.ui.activity.timeline;

import com.orient.me.data.ITimeItem;

public class TimeItem implements ITimeItem {

    private String name;
    private String title;
    private String detail;
    private int color;
    private int res;

    public TimeItem(String name, String title, String detail, int color, int res) {
        this.name = name;
        this.title = title;
        this.detail = detail;
        this.color = color;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getResource() {
        return res;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
