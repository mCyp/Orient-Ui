package com.orient.ui.ui.activity.timeline;

import android.content.Context;
import android.content.Intent;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import com.orient.ui.R;
import com.orient.ui.ui.activity.BaseActivity;
import com.orient.ui.ui.adapter.TimeLineAdapter;

import butterknife.BindView;

public class TimelineActivity extends BaseActivity {

    public static final String[] titles = {"DateInfoDTL", "WeekPlanDTL", "NoteInfoSTL","WeekPlanSTL","StepSTL","SocialMediaSTL"};

    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private TimeLineAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.timeline_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mTabLayout.setupWithViewPager(mViewPager, true);
        mAdapter = new TimeLineAdapter(getSupportFragmentManager(), titles);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });

        // 重新添加
        mTabLayout.removeAllTabs();
        for (int i = 0; i < titles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles[i]));
        }
    }

    public static void show(Context context){
        Intent intent = new Intent(context, TimelineActivity.class);
        context.startActivity(intent);
    }



}
