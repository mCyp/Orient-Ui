package com.orient.ui.ui.activity.table;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orient.me.widget.rv.adapter.BaseAdapter;
import com.orient.me.widget.rv.adapter.TableAdapter;
import com.orient.me.widget.rv.adapter.TableView;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;
import com.orient.ui.R;
import com.orient.ui.ui.activity.BaseActivity;
import com.orient.ui.ui.adapter.CommonGridAdapter;
import com.orient.ui.ui.adapter.TBAdapter;
import com.orient.ui.ui.adapter.TimeLineAdapter;
import com.orient.ui.widget.viewPager.NoScrollViewPager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class TableActivity extends BaseActivity {


    public static final String[] titles = {"Course", "Table", "Page"};

    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.mViewPager)
    NoScrollViewPager mViewPager;

    private TBAdapter mAdapter;

    public static void show(Context context) {
        Intent intent = new Intent(context, TableActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.table_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mTabLayout.setupWithViewPager(mViewPager, true);
        mAdapter = new TBAdapter(getSupportFragmentManager(), titles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setNoScroll(true);

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



}
