package com.orient.ui.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orient.ui.ui.fragment.timeline.dtl.DateInfoDTLFragment;
import com.orient.ui.ui.fragment.timeline.dtl.WeekPlanDTLFragment;
import com.orient.ui.ui.fragment.timeline.stl.NoteInfoSTLFragment;
import com.orient.ui.ui.fragment.timeline.stl.SocialMediaSTLFragment;
import com.orient.ui.ui.fragment.timeline.stl.StepSTLFragment;
import com.orient.ui.ui.fragment.timeline.stl.WeekPlanSTLFragment;

/**
 * 时间轴的展示的适配器
 */
public class TimeLineAdapter extends FragmentPagerAdapter {

    private String[] titles;

    public TimeLineAdapter(FragmentManager fm,String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new DateInfoDTLFragment();
            case 1:
                return new WeekPlanDTLFragment();
            case 2:
                return new NoteInfoSTLFragment();
            case 3:
                return new WeekPlanSTLFragment();
            case 4:
                return new StepSTLFragment();
            default:
                return new SocialMediaSTLFragment();
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    /*@Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }*/

}
