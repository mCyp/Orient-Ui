package com.orient.ui.widget.timeline.stl;

import android.graphics.Color;

import com.orient.ui.utils.DateUtils;
import com.orient.ui.widget.timeline.dtl.DateInfo;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteInfo extends DateInfo {

    public static final int NOTE_IMG = 1;
    public static final int NOTE_TEXT = 2;


    private int type;

    public NoteInfo(String name, String detail, Date date, int color,int type) {
        super(name, detail, date, color);

        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getTitle() {
        return DateUtils.date2SDayFormat(getDate());
    }

    public static List<NoteInfo> initNoteInfo(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<NoteInfo> items = new ArrayList<>();
        items.add(new NoteInfo("喝茶", "第一天养养生吧~", calendar.getTime(), Color.parseColor("#f36c60"), NOTE_IMG));
        items.add(new NoteInfo("喝酒", "今天找老徐吃烧烤", calendar.getTime(), Color.parseColor("#ab47bc"), NOTE_TEXT));
        items.add(new NoteInfo("画画", "去鼋头渚写生", calendar.getTime(), Color.parseColor("#aed581"), NOTE_TEXT));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new NoteInfo("高尔夫", "约个高尔夫", calendar.getTime(), Color.parseColor("#5FB29F"),NOTE_IMG ));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new NoteInfo("游泳", "今天来洗个澡", calendar.getTime(), Color.parseColor("#ec407a"), NOTE_TEXT));
        items.add(new NoteInfo("温泉", "快上班了好好休息", calendar.getTime(), Color.parseColor("#0D47A1"), NOTE_TEXT));
        return items;
    }
}


