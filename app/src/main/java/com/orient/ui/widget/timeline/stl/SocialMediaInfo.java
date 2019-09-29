package com.orient.ui.widget.timeline.stl;

import android.graphics.Color;

import com.orient.ui.R;
import com.orient.ui.widget.timeline.dtl.DateInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static List<SocialMediaInfo> initSocialMediaInfo(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<SocialMediaInfo> items = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            items.add(new SocialMediaInfo("傍晚的太湖", "", calendar.getTime(), Color.parseColor("#f36c60"), R.drawable.social_5));

            calendar.add(Calendar.DAY_OF_MONTH, -1);
            items.add(new SocialMediaInfo("最近真的是种了简书和掘金的毒，偷偷学习了它们的风格", "共2张"
                    , calendar.getTime(), Color.parseColor("#ab47bc"), R.drawable.social_6));
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            items.add(new SocialMediaInfo("出差啦~", "共3张", calendar.getTime(), Color.parseColor("#aed581"), R.drawable.social_1));

            calendar.add(Calendar.YEAR, -1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            items.add(new SocialMediaInfo("Hey，故宫", "", calendar.getTime(), Color.parseColor("#5FB29F"), R.drawable.social_2));
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            items.add(new SocialMediaInfo("吃个布丁", "", calendar.getTime(), Color.parseColor("#ec407a"), R.drawable.social_3));
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            items.add(new SocialMediaInfo("夏天到啦啦啦，2333333", "共2张", calendar.getTime(), Color.parseColor("#0D47A1"), R.drawable.social_4));
        }
        return items;
    }
}
