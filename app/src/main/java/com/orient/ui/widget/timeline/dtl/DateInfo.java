package com.orient.ui.widget.timeline.dtl;

import com.orient.me.data.ITimeItem;

import java.util.Date;

public class DateInfo implements ITimeItem {

    private String name;
    private String detail;
    private Date date;
    private int color;

    public DateInfo(String name, String detail, Date date, int color) {
        this.name = name;
        this.detail = detail;
        this.date = date;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    public void setColor(int color) {
        this.color = color;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getResource() {
        return 0;
    }
}
