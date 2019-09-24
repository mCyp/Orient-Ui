package com.orient.ui.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.ui.R;
import com.orient.ui.data.TimeItem;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.widget.TimeLineDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TimelineActivity extends BaseActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<TimeItem> mAdapter;

    public static void show(Context context){
        Intent intent = new Intent(context, TimelineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.timeline_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<TimeItem>() {
            @Override
            public ViewHolder<TimeItem> onCreateViewHolder(View root, int viewType) {
                return new TimeLineViewHolder(root);
            }

            @Override
            public int getItemLayout(TimeItem timeItem, int position) {
                return R.layout.timeline_recycle_item;
            }
        });
        List<TimeItem> timeItems = initItems();
        mAdapter.addAllData(timeItems);
        TimeLineDecoration decoration = new TimeLineDecoration(this,timeItems);
        mRecyclerView.addItemDecoration(decoration);
    }

    private List<TimeItem> initItems(){
        List<TimeItem> items = new ArrayList<>();
        items.add(new TimeItem("完善信息","实践探究","+30积分", Color.parseColor("#008577"),0));
        items.add(new TimeItem("了解基地","实践探究","+30积分", Color.parseColor("#008577"),0));
        items.add(new TimeItem("知识储备","实践探究","+30积分", Color.parseColor("#008577"),0));
        items.add(new TimeItem("安全教育主题馆","实践探究","+30积分", Color.parseColor("#008577"),0));
        items.add(new TimeItem("评价教师","总结拓展","+30积分", Color.parseColor("#008577"),0));
        items.add(new TimeItem("评价路线","总结拓展","+30积分", Color.parseColor("#008577"),0));
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
