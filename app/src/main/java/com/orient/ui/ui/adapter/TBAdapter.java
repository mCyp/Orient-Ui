package com.orient.ui.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orient.ui.ui.fragment.table.CourseFragment;
import com.orient.ui.ui.fragment.table.PageFragment;
import com.orient.ui.ui.fragment.table.TableFragment;
import com.orient.ui.ui.fragment.timeline.dtl.DateInfoDTLFragment;
import com.orient.ui.ui.fragment.timeline.dtl.WeekPlanDTLFragment;
import com.orient.ui.ui.fragment.timeline.stl.NoteInfoSTLFragment;
import com.orient.ui.ui.fragment.timeline.stl.SocialMediaSTLFragment;
import com.orient.ui.ui.fragment.timeline.stl.StepSTLFragment;
import com.orient.ui.ui.fragment.timeline.stl.WeekPlanSTLFragment;

/**
 * 时间轴的展示的适配器
 */
public class TBAdapter extends FragmentPagerAdapter {

    private String[] titles;

    public TBAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new CourseFragment();
            case 1:
                return new TableFragment();
            default:
                return new PageFragment();
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
