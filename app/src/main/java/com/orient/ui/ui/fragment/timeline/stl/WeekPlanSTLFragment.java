package com.orient.ui.ui.fragment.timeline.stl;


import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.utils.UIUtils;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.me.widget.rv.layoutmanager.DoubleSideLayoutManager;
import com.orient.ui.R;
import com.orient.ui.ui.activity.timeline.TimeItem;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.ui.fragment.BaseFragment;
import com.orient.ui.widget.timeline.dtl.WeekPlanDTL;
import com.orient.ui.widget.timeline.stl.WeekPlanSTL;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 一周计划的Fragment
 * 两侧分布的时间轴 样式一
 */
public class WeekPlanSTLFragment extends BaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<TimeItem> mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.common_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<TimeItem>() {
            @Override
            public ViewHolder<TimeItem> onCreateViewHolder(View root, int viewType) {
                return new WeekPlanViewHolder(root);
            }

            @Override
            public int getItemLayout(TimeItem s, int position) {
                return R.layout.week_plan_stl_recycle_item;
            }
        });

        List<TimeItem> timeItems = initItems();
        mAdapter.addAllData(timeItems);

        TimeLine timeLine = provideTimeLine(timeItems);
        mRecyclerView.addItemDecoration(timeLine);
    }

    private List<TimeItem> initItems() {
        List<TimeItem> items = new ArrayList<>();
        items.add(new TimeItem("喝茶", "10-01，周二", "第一天养养生吧~", Color.parseColor("#f36c60"), R.drawable.timeline_ic_tea));
        items.add(new TimeItem("喝酒", "06-12，周三", "今天找老徐吃烧烤", Color.parseColor("#ab47bc"), R.drawable.timeline_ic_drink));
        items.add(new TimeItem("画画", "07-07，周四", "去鼋头渚写生", Color.parseColor("#aed581"), R.drawable.timeline_ic_draw));
        items.add(new TimeItem("高尔夫", "08-20，周五", "约个高尔夫", Color.parseColor("#5FB29F"), R.drawable.timeline_ic_golf));
        items.add(new TimeItem("桑拿", "09-16，周六", "今天来洗个澡", Color.parseColor("#ec407a"), R.drawable.timeline_ic_bath));
        items.add(new TimeItem("足浴", "10-01，周日", "快上班了好好休息", Color.parseColor("#0D47A1"), R.drawable.timeline_ic_footer));
        return items;
    }


    private TimeLine provideTimeLine(List<TimeItem> timeItems) {
        return new TimeLine.Builder(getContext(), timeItems)
                .setTitle(Color.parseColor("#8d9ca9"), 14)
                .setTitleStyle(TimeLine.FLAG_TITLE_TYPE_LEFT, 100)
                .setLine(TimeLine.FLAG_LINE_BEGIN_TO_END, 40, Color.parseColor("#757575"),3)
                .setDot(TimeLine.FLAG_DOT_RES)
                .build(WeekPlanSTL.class);
    }

    class WeekPlanViewHolder extends RecyclerAdapter.ViewHolder<TimeItem> {

        @BindView(R.id.tv_name)
        TextView mNameTv;

        @BindView(R.id.tv_detail)
        TextView mDetailTv;

        WeekPlanViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(TimeItem timeItem) {
            mNameTv.setText(timeItem.getName());
            mDetailTv.setText(timeItem.getDetail());
        }

    }


}
