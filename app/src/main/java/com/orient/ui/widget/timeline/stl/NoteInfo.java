package com.orient.ui.widget.timeline.stl;

import com.orient.ui.utils.DateUtils;
import com.orient.ui.widget.timeline.dtl.DateInfo;

import java.security.PublicKey;
import java.util.Date;

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
}


