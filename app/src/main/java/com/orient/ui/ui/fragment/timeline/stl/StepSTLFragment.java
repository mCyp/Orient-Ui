package com.orient.ui.ui.fragment.timeline.stl;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.ui.R;
import com.orient.ui.ui.activity.timeline.TimeItem;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.ui.fragment.BaseFragment;
import com.orient.ui.widget.timeline.stl.StepSTL;

import java.util.ArrayList;
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

        List<TimeItem> timeItems = initItems();
        mAdapter.addAllData(timeItems);
        TimeLine decoration = new SingleTimeLineDecoration.Builder(getContext(),timeItems)
                .setTitle(Color.parseColor("#ffffff"),20)
                .setTitleStyle(SingleTimeLineDecoration.FLAG_TITLE_TYPE_TOP,40)
                .setLine(SingleTimeLineDecoration.FLAG_LINE_DIVIDE,30,Color.parseColor("#8d9ca9"))
                .setDot(SingleTimeLineDecoration.FLAG_DOT_DRAW)
                .setSameTitleHide()
                .build(StepSTL.class);
        mRecyclerView.addItemDecoration(decoration);
    }

    private List<TimeItem> initItems(){
        List<TimeItem> items = new ArrayList<>();
        for(int i = 0;i<3;i++) {
            items.add(new TimeItem("完善信息", "实践探究"+i, "+30积分", Color.parseColor("#F57F17"), 0));
            items.add(new TimeItem("了解基地", "实践探究"+i, "+30积分", Color.parseColor("#F57F17"), 0));
            items.add(new TimeItem("知识储备", "实践探究"+i, "+30积分", Color.parseColor("#F57F17"), 0));
            items.add(new TimeItem("安全教育主题馆", "实践探究"+i, "+30积分", Color.parseColor("#F57F17"), 0));
            items.add(new TimeItem("评价教师", "总结拓展"+i, "+30积分", Color.parseColor("#0D47A1"), 0));
            items.add(new TimeItem("评价路线", "总结拓展"+i, "+30积分", Color.parseColor("#0D47A1"), 0));
        }
        return items;
    }


    class TimeLineViewHolder extends RecyclerAdapter.ViewHolder<TimeItem>{

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
