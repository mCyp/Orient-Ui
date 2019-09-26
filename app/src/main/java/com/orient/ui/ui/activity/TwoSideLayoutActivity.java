package com.orient.ui.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.me.widget.rv.layoutmanager.DoubleSideLayoutManager;
import com.orient.ui.R;
import com.orient.ui.data.TimeItem;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.utils.UIUtils;
import com.orient.ui.widget.DoubleSideTimeLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 两侧布局的最佳实践
 */
public class TwoSideLayoutActivity extends BaseActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<TimeItem> mAdapter;
    private List<String> values = new ArrayList<>();

    public static void show(Context context) {
        Intent intent = new Intent(context, TwoSideLayoutActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.two_side_layout_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();


        mRecyclerView.setLayoutManager(new DoubleSideLayoutManager(DoubleSideLayoutManager.START_LEFT, UIUtils.dip2px(40)));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<TimeItem>() {
            @Override
            public ViewHolder<TimeItem> onCreateViewHolder(View root, int viewType) {
                return new TwoSideLayoutActivity.ViewHolder(root);
            }

            @Override
            public int getItemLayout(TimeItem s, int position) {
                return R.layout.two_side_left_recycle_item;
                /*if (position % 2 == 1)
                    return R.layout.two_side_right_recycle_item;
                else
                    return R.layout.two_side_left_recycle_item;*/
            }
        });

        List<TimeItem> timeItems = initItems();
        mAdapter.addAllData(timeItems);

        TimeLine timeLine = provideTimeLine(timeItems);
        mRecyclerView.addItemDecoration(timeLine);
    }

    @Override
    protected void initData() {
        super.initData();

        /*values.add("Java");
        values.add("Android");
        values.add("Kotlin");
        values.add("Python");
        values.add("Vue");
        values.add("Flutter");

        mAdapter.addAllData(values);*/
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
        return new SingleTimeLineDecoration.Builder(this, timeItems)
                .setTitle(Color.parseColor("#8d9ca9"), 14)
                .setTitleStyle(TimeLine.FLAG_TITLE_TYPE_LEFT, 0)
                .setLine(TimeLine.FLAG_LINE_BEGIN_TO_END, 30, Color.parseColor("#757575"),3)
                .setDot(TimeLine.FLAG_DOT_DRAW)
                .build(DoubleSideTimeLine.class);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<TimeItem> {

        @BindView(R.id.tv_name)
        TextView mNameTv;

        @BindView(R.id.tv_detail)
        TextView mDetailTv;

        @BindView(R.id.btn_go)
        TextView mGoBtn;

        @BindView(R.id.btn_write)
        TextView mWriteBtn;

        ViewHolder(View itemView) {
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
