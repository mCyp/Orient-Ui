package com.orient.ui.ui.activity.timeline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.ui.R;
import com.orient.ui.ui.activity.BaseActivity;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.ui.adapter.TimeLineAdapter;
import com.orient.ui.widget.timeline.stl.StepSTL;

import java.util.ArrayList;
import java.util.List;

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

       /* for (int i = 0; i < titles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setIcon(res[i]));
        }*/

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
