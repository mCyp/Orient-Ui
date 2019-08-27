package com.orient.ui.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orient.ui.R;
import com.orient.ui.data.GridItem;
import com.orient.ui.ui.adapter.GridAdapter;
import com.orient.ui.widget.SuspensionDecoration;

import java.util.ArrayList;
import java.util.List;

public class SpecialGridActivity extends AppCompatActivity {

    private List<GridItem> values;
    private RecyclerView mRecyclerView;
    private SuspensionDecoration suspensionDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_grid);

        initWidget();
    }

    private void initWidget(){
        mRecyclerView = findViewById(R.id.rv_content);

        GridLayoutManager gll = new GridLayoutManager(this,6);
        gll.setSpanSizeLookup(new SpecialSpanSizeLookup());
        mRecyclerView.setLayoutManager(gll);
        initData();
        GridAdapter mAdapter = new GridAdapter(values,this);
        mRecyclerView.setAdapter(mAdapter);
        suspensionDecoration = new SuspensionDecoration(this,values);
        mRecyclerView.addItemDecoration(suspensionDecoration);
    }

    private void initData(){
        values = new ArrayList<>();
        values.add(new GridItem("6","最火",6));
        values.add(new GridItem("3","热门",3));
        values.add(new GridItem("3","热门",3));
        values.add(new GridItem("2","一周",2));
        values.add(new GridItem("2","一周",2));
        values.add(new GridItem("2","一周",2));
        values.add(new GridItem("2","一周",2));
        values.add(new GridItem("2","一周",2));
        values.add(new GridItem("2","一周",2));

    }

    class SpecialSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{

        @Override
        public int getSpanSize(int i) {
            // TODO 做自己处理的逻辑
            GridItem gridItem = values.get(i);
            return gridItem.getSpanSize();
        }
    }


}
