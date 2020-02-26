package com.orient.ui.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.orient.ui.ui.fragment.table.CourseFragment;
import com.orient.ui.ui.fragment.table.PageFragment;
import com.orient.ui.ui.fragment.table.TableFragment;

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
