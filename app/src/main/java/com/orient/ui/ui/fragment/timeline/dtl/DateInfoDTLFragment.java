package com.orient.ui.ui.fragment.timeline.dtl;


import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.utils.UIUtils;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.me.widget.rv.layoutmanager.doubleside.DoubleSideLayoutManager;
import com.orient.ui.R;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.ui.fragment.BaseFragment;
import com.orient.ui.widget.timeline.dtl.DateInfo;
import com.orient.ui.widget.timeline.dtl.DateInfoDTL;

import java.util.List;

import butterknife.BindView;

/**
 * 展示详细日期的时间轴
 * 两侧分布的时间轴 样式二
 */
public class DateInfoDTLFragment extends BaseFragment {

    @BindView(R.id.lay_bg)
    View view;
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


        view.setBackgroundResource(R.color.light_green_50);
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

        List<DateInfo> timeItems = DateInfo.initDateInfo();
        mAdapter.addAllData(timeItems);

        TimeLine timeLine = provideTimeLine(timeItems);
        mRecyclerView.addItemDecoration(timeLine);
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
