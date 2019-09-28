package com.orient.ui.ui.fragment.timeline.stl;


import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orient.me.utils.UIUtils;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.me.widget.rv.layoutmanager.DoubleSideLayoutManager;
import com.orient.ui.R;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.ui.fragment.BaseFragment;
import com.orient.ui.widget.timeline.dtl.DateInfo;
import com.orient.ui.widget.timeline.dtl.DateInfoDTL;
import com.orient.ui.widget.timeline.stl.NoteInfo;
import com.orient.ui.widget.timeline.stl.SocialMediaInfo;
import com.orient.ui.widget.timeline.stl.SocialMediaSTL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 展示详细日期的时间轴
 * 两侧分布的时间轴 样式二
 */
public class SocialMediaSTLFragment extends BaseFragment {


    @BindView(R.id.lay_bg)
    View view;
    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<SocialMediaInfo> mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.common_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        view.setBackgroundResource(R.color.teal_50);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<SocialMediaInfo>() {
            @Override
            public ViewHolder<SocialMediaInfo> onCreateViewHolder(View root, int viewType) {
                return new DateInfoHolder(root);
            }

            @Override
            public int getItemLayout(SocialMediaInfo s, int position) {
                return R.layout.social_meida_recycle_item;
            }
        });

        List<SocialMediaInfo> timeItems = initItems();
        mAdapter.addAllData(timeItems);

        TimeLine timeLine = provideTimeLine(timeItems);
        mRecyclerView.addItemDecoration(timeLine);
    }

    private List<SocialMediaInfo> initItems() {
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


    private TimeLine provideTimeLine(List<SocialMediaInfo> timeItems) {
        return new TimeLine.Builder(getContext(), timeItems)
                .setTitleStyle(TimeLine.FLAG_TITLE_TYPE_TOP, 52)
                .setTitle(Color.parseColor("#000000"), 22)
                .setLine(TimeLine.FLAG_LINE_DIVIDE, 80, Color.parseColor("#757575"), 1)
                .setDot(TimeLine.FLAG_DOT_DRAW)
                .setSameTitleHide()
                .build(SocialMediaSTL.class);
    }

    class DateInfoHolder extends RecyclerAdapter.ViewHolder<SocialMediaInfo> {

        @BindView(R.id.tv_name)
        TextView mNameTv;

        @BindView(R.id.tv_detail)
        TextView mDetailTv;

        @BindView(R.id.iv_content)
        ImageView mContentIv;

        DateInfoHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(SocialMediaInfo timeItem) {
            mNameTv.setText(timeItem.getName());
            mDetailTv.setText(timeItem.getDetail());
            mContentIv.setImageResource(timeItem.getRes());
        }

    }

}
