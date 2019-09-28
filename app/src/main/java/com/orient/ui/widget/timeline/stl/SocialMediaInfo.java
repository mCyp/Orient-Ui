package com.orient.ui.widget.timeline.stl;

import com.orient.ui.widget.timeline.dtl.DateInfo;

import java.util.Calendar;
import java.util.Date;

public class SocialMediaInfo extends DateInfo {

    private int res;

    public SocialMediaInfo(String name, String detail, Date date, int color,int res) {
        super(name, detail, date, color);
        this.res = res;
    }

    @Override
    public String getTitle() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        return Integer.toString(calendar.get(Calendar.YEAR));
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
