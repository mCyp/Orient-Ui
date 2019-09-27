package com.orient.ui.ui.fragment.timeline.stl;


import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.ui.R;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.ui.fragment.BaseFragment;
import com.orient.ui.widget.timeline.dtl.DateInfo;
import com.orient.ui.widget.timeline.stl.NoteInfo;
import com.orient.ui.widget.timeline.stl.NoteInfoSTL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.orient.ui.widget.timeline.stl.NoteInfo.NOTE_IMG;
import static com.orient.ui.widget.timeline.stl.NoteInfo.NOTE_TEXT;

/**
 * 展示详细日期的时间轴
 * 线性分布的时间轴 样式二
 */
public class NoteInfoSTLFragment extends BaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<NoteInfo> mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_date_info_dtl;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<NoteInfo>() {
            @Override
            public ViewHolder<NoteInfo> onCreateViewHolder(View root, int viewType) {
                return new DateInfoHolder(root);
            }

            @Override
            public int getItemLayout(NoteInfo s, int position) {
                if (s.getType() == NOTE_IMG) {
                    return R.layout.note_info_img_recycle_item;
                }
                return R.layout.note_info_text_recycle_item;

            }
        });

        List<NoteInfo> timeItems = initItems();
        mAdapter.addAllData(timeItems);

        TimeLine timeLine = provideTimeLine(timeItems);
        mRecyclerView.addItemDecoration(timeLine);
    }

    private List<NoteInfo> initItems() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<NoteInfo> items = new ArrayList<>();
        items.add(new NoteInfo("喝茶", "第一天养养生吧~", calendar.getTime(), Color.parseColor("#f36c60"), NOTE_IMG));
        items.add(new NoteInfo("喝酒", "今天找老徐吃烧烤", calendar.getTime(), Color.parseColor("#ab47bc"), NOTE_TEXT));
        items.add(new NoteInfo("画画", "去鼋头渚写生", calendar.getTime(), Color.parseColor("#aed581"), NOTE_TEXT));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new NoteInfo("高尔夫", "约个高尔夫", calendar.getTime(), Color.parseColor("#5FB29F"),NOTE_IMG ));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        items.add(new NoteInfo("桑拿", "今天来洗个澡", calendar.getTime(), Color.parseColor("#ec407a"), NOTE_TEXT));
        items.add(new NoteInfo("足浴", "快上班了好好休息", calendar.getTime(), Color.parseColor("#0D47A1"), NOTE_TEXT));
        return items;
    }


    private TimeLine provideTimeLine(List<NoteInfo> timeItems) {
        return new TimeLine.Builder(getContext(), timeItems)
                .setTitle(Color.parseColor("#ffffff"), 20)
                .setTitleStyle(SingleTimeLineDecoration.FLAG_TITLE_TYPE_LEFT, 80)
                .setLine(SingleTimeLineDecoration.FLAG_LINE_CONSISTENT, 0, Color.parseColor("#00000000"))
                .setDot(SingleTimeLineDecoration.FLAG_DOT_DRAW)
                .build(NoteInfoSTL.class);
    }

    class DateInfoHolder extends RecyclerAdapter.ViewHolder<NoteInfo> {

        @BindView(R.id.tv_name)
        TextView mNameTv;

        @BindView(R.id.tv_detail)
        TextView mDetailTv;

        DateInfoHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(NoteInfo timeItem) {
            mNameTv.setText(timeItem.getName());
            mDetailTv.setText(timeItem.getDetail());

        }

    }

}
