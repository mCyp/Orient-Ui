package com.orient.ui.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.TwoSideLayoutManager;
import com.orient.ui.R;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.utils.UIUtils;
import com.orient.ui.widget.DotItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TimeLineActivity extends BaseActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<String> mAdapter;
    private List<String> values = new ArrayList<>();

    public static void show(Context context){
        Intent intent = new Intent(context, TimeLineActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.timeline_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();


        mRecyclerView.setLayoutManager(new TwoSideLayoutManager(TwoSideLayoutManager.START_LEFT, UIUtils.dip2px(40)));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<String>() {
            @Override
            public ViewHolder<String> onCreateViewHolder(View root, int viewType) {
                return new TimeLineActivity.ViewHolder(root);
            }

            @Override
            public int getItemLayout(String s, int position) {
                if(position%2 == 1)
                    return R.layout.timeline_right_recycle_item;
                else
                    return R.layout.timeline_left_recycle_item;
            }
        });

        DotItemDecoration dotItemDecoration = providesDotItemDecoration();
        mRecyclerView.addItemDecoration(dotItemDecoration);
    }

    @Override
    protected void initData() {
        super.initData();

        values.add("Java");
        values.add("Android");
        values.add("Kotlin");
        values.add("Python");
        values.add("Vue");
        values.add("Flutter");

        mAdapter.addAllData(values);
    }

    private DotItemDecoration providesDotItemDecoration(){
        return new DotItemDecoration.Builder(this)
                .setOrientation(DotItemDecoration.VERTICAL)//if you want a horizontal item decoration,remember to set horizontal orientation to your LayoutManager
                .setItemStyle(DotItemDecoration.STYLE_DRAW)// choose to draw or use resource
                .setTopDistance(20)//dp
                .setItemInterVal(130)//dp
                .setItemPaddingLeft(10)//default value equals to item interval value
                .setItemPaddingRight(10)//default value equals to item interval value
                .setDotColor(Color.parseColor("#673AB7"))
                .setDotRadius(8)//dp
                .setDotPaddingTop(2)
                .setDotInItemOrientationCenter(true)// t true if you want the dot align center
                .setLineWidth(2)//dp
                .setEndText("结束")
                .setTextSize(14)
                .setTextColor(Color.parseColor("#673AB7"))
                //.setBottomRes(R.drawable.ic_ma_1)
                .setDotPaddingText(2)//dp.The distance between the last dot and the end text
                .setBottomDistance(20)//you can add a distance to make bottom line longer
                .create();
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<String>{

        @BindView(R.id.tv_content)
        TextView mContentTv;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(String s) {
            mContentTv.setText(s);
        }
    }


}
