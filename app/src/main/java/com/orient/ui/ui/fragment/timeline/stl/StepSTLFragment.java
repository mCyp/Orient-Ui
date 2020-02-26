package com.orient.ui.ui.fragment.timeline.stl;


import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.ui.R;
import com.orient.ui.ui.activity.timeline.TimeItem;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.ui.fragment.BaseFragment;
import com.orient.ui.widget.timeline.stl.StepSTL;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepSTLFragment extends BaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<TimeItem> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_stl;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<TimeItem>() {
            @Override
            public ViewHolder<TimeItem> onCreateViewHolder(View root, int viewType) {
                return new TimeLineViewHolder(root);
            }

            @Override
            public int getItemLayout(TimeItem timeItem, int position) {
                return R.layout.step_recycle_item;
            }
        });

        List<TimeItem> timeItems = TimeItem.initStepInfo();
        mAdapter.addAllData(timeItems);
        TimeLine decoration = new SingleTimeLineDecoration.Builder(getContext(), timeItems)
                .setTitle(Color.parseColor("#ffffff"), 20)
                .setTitleStyle(SingleTimeLineDecoration.FLAG_TITLE_TYPE_TOP, 40)
                .setLine(SingleTimeLineDecoration.FLAG_LINE_DIVIDE, 50, Color.parseColor("#8d9ca9"))
                .setDot(SingleTimeLineDecoration.FLAG_DOT_DRAW)
                .setSameTitleHide()
                .build(StepSTL.class);
        mRecyclerView.addItemDecoration(decoration);
    }


    class TimeLineViewHolder extends RecyclerAdapter.ViewHolder<TimeItem> {

        @BindView(R.id.tv_title)
        TextView mTitleTv;
        @BindView(R.id.tv_content)
        TextView mContentTv;

        public TimeLineViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(TimeItem timeItem) {
            mTitleTv.setText(timeItem.getName());
            mContentTv.setText(timeItem.getDetail());
        }
    }

}
