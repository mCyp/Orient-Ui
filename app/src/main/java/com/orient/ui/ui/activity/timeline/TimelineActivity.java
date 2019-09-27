package com.orient.ui.ui.activity.timeline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.ui.R;
import com.orient.ui.ui.activity.BaseActivity;
import com.orient.ui.ui.adapter.RecyclerAdapter;
import com.orient.ui.widget.timeline.stl.StepSTL;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TimelineActivity extends BaseActivity {

    public static void show(Context context){
        Intent intent = new Intent(context, TimelineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.timeline_activity;
    }
}
