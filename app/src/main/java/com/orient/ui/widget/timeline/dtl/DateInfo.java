package com.orient.ui.widget.timeline.dtl;

import android.graphics.Color;

import com.orient.me.data.ITimeItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static List<DateInfo> initDateInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<DateInfo> items = new ArrayList<>();
        items.add(new DateInfo("喝茶", "第一天养养生吧~", calendar.getTime(), Color.parseColor("#f36c60")));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new DateInfo("喝酒", "今天找老徐吃烧烤", calendar.getTime(), Color.parseColor("#ab47bc")));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new DateInfo("画画", "去鼋头渚写生", calendar.getTime(), Color.parseColor("#aed581")));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new DateInfo("高尔夫", "约个高尔夫", calendar.getTime(), Color.parseColor("#5FB29F")));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new DateInfo("游泳", "今天来洗个澡", calendar.getTime(), Color.parseColor("#ec407a")));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new DateInfo("温泉", "快上班了好好休息", calendar.getTime(), Color.parseColor("#ffd54f")));
        return items;
    }
}
