package com.orient.ui.ui.fragment.timeline.dtl;


import android.graphics.Color;
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
import com.orient.ui.widget.timeline.dtl.DateInfo;
import com.orient.ui.widget.timeline.dtl.DateInfoDTL;
import com.orient.ui.widget.timeline.dtl.WeekPlanDTL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 展示详细日期的时间轴
 * 两侧分布的时间轴 样式二
 */
public class DateInfoDTLFragment extends BaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<DateInfo> mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.common_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);


        mRecyclerView.setLayoutManager(new DoubleSideLayoutManager(DoubleSideLayoutManager.START_LEFT, UIUtils.dip2px(40)));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<DateInfo>() {
            @Override
            public ViewHolder<DateInfo> onCreateViewHolder(View root, int viewType) {
                return new DateInfoHolder(root);
            }

            @Override
            public int getItemLayout(DateInfo s, int position) {
                return R.layout.date_info_recycle_item;
            }
        });

        List<DateInfo> timeItems = initItems();
        mAdapter.addAllData(timeItems);

        TimeLine timeLine = provideTimeLine(timeItems);
        mRecyclerView.addItemDecoration(timeLine);
    }

    private List<DateInfo> initItems() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<DateInfo> items = new ArrayList<>();
        items.add(new DateInfo("喝茶", "第一天养养生吧~",calendar.getTime(), Color.parseColor("#f36c60")));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        items.add(new DateInfo("喝酒", "今天找老徐吃烧烤",calendar.getTime(), Color.parseColor("#ab47bc")));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        items.add(new DateInfo("画画", "去鼋头渚写生",calendar.getTime(), Color.parseColor("#aed581")));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        items.add(new DateInfo("高尔夫", "约个高尔夫",calendar.getTime(), Color.parseColor("#5FB29F")));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        items.add(new DateInfo("游泳", "今天来洗个澡", calendar.getTime(),Color.parseColor("#ec407a")));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        items.add(new DateInfo("温泉", "快上班了好好休息",calendar.getTime(), Color.parseColor("#0D47A1")));
        return items;
    }


    private TimeLine provideTimeLine(List<DateInfo> timeItems) {
        return new TimeLine.Builder(getContext(), timeItems)
                .setTitleStyle(TimeLine.FLAG_TITLE_POS_NONE, 0)
                .setLine(TimeLine.FLAG_LINE_BEGIN_TO_END, 60, Color.parseColor("#757575"), 2)
                .setDot(TimeLine.FLAG_DOT_DRAW)
                .build(DateInfoDTL.class);
    }

    class DateInfoHolder extends RecyclerAdapter.ViewHolder<DateInfo> {

        @BindView(R.id.tv_name)
        TextView mNameTv;

        @BindView(R.id.tv_detail)
        TextView mDetailTv;

        DateInfoHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(DateInfo timeItem) {
            mNameTv.setText(timeItem.getName());
            mDetailTv.setText(timeItem.getDetail());
        }

    }

}
