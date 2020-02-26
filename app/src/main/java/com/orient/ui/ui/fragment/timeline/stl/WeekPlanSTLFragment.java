package com.orient.ui.ui.fragment.timeline.stl;


import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.ui.R;
import com.orient.ui.ui.activity.timeline.TimeItem;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.ui.fragment.BaseFragment;
import com.orient.ui.widget.timeline.stl.WeekPlanSTL;

import java.util.List;

import butterknife.BindView;

/**
 * 一周计划的Fragment
 * 两侧分布的时间轴 样式一
 */
public class WeekPlanSTLFragment extends BaseFragment {

    @BindView(R.id.lay_bg)
    View view;
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

        view.setBackgroundResource(R.color.purple_50);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<TimeItem>() {
            @Override
            public ViewHolder<TimeItem> onCreateViewHolder(View root, int viewType) {
                return new WeekPlanViewHolder(root);
            }

            @Override
            public int getItemLayout(TimeItem s, int position) {
                return R.layout.two_side_left_recycle_item;
            }
        });

        List<TimeItem> timeItems = TimeItem.initTimeInfo();
        mAdapter.addAllData(timeItems);

        TimeLine timeLine = provideTimeLine(timeItems);
        mRecyclerView.addItemDecoration(timeLine);
    }


    private TimeLine provideTimeLine(List<TimeItem> timeItems) {
        return new TimeLine.Builder(getContext(), timeItems)
                .setTitle(Color.parseColor("#8d9ca9"), 14)
                .setTitleStyle(TimeLine.FLAG_TITLE_TYPE_LEFT, 100)
                .setLine(TimeLine.FLAG_LINE_BEGIN_TO_END, 40, Color.parseColor("#757575"),1)
                .setDot(TimeLine.FLAG_DOT_RES)
                .build(WeekPlanSTL.class);
    }

    class WeekPlanViewHolder extends RecyclerAdapter.ViewHolder<TimeItem> {

        @BindView(R.id.tv_name)
        TextView mNameTv;

        @BindView(R.id.tv_detail)
        TextView mDetailTv;

        @BindView(R.id.btn_go)
        TextView mGoBtn;

        @BindView(R.id.btn_write)
        TextView mWriteBtn;

        WeekPlanViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(TimeItem timeItem) {
            mNameTv.setText(timeItem.getName());
            mDetailTv.setText(timeItem.getDetail());

            setColor(timeItem.getColor());
        }

        private void setColor(int color){
            mGoBtn.setBackgroundColor(color);
            mWriteBtn.setBackgroundColor(color);
        }

    }


}
