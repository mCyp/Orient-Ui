package com.orient.me.widget.rv.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.orient.me.R;
import com.orient.me.data.table.ICellItem;
import com.orient.me.widget.rv.adapter.GridAdapter;
import com.orient.me.widget.rv.adapter.GridAdapterProxy;
import com.orient.me.widget.rv.adapter.LeftAdapterProxy;
import com.orient.me.widget.rv.adapter.TableAdapter;
import com.orient.me.widget.rv.adapter.TitleAdapterProxy;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;
import com.orient.me.widget.rv.rv.TableRecyclerView;

import java.util.List;

public class TableView<Data extends ICellItem> extends FrameLayout {

    private RecyclerView mTitleRv;
    private FrameLayout mTitleHeadFl;
    private RecyclerView mLeftRv;
    private TableRecyclerView mTableRv;

    // 适配器
    private TableAdapter<Data> mTableAdapter;
    private GridAdapterProxy<Data> mGridAdapter;
    private LeftAdapterProxy<Data> mLeftAdapter;
    private TitleAdapterProxy<Data> mTitleAdapter;

    private boolean isLeftOpen = true;
    private boolean isTopOpen = true;

    private int mMode = TableLayoutManager.MODE_A;
    private int w = 4;
    private int h = 8;

    public TableView(@NonNull Context context) {
        super(context);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置模型和长度
     *
     * @param mode 模型
     * @param w    长度 Or WidthSpan
     * @param h    高度 or heightSpan
     */
    public void setModeAndValue(int mode, int w, int h) {
        this.mMode = mode;
        this.w = w;
        this.h = h;
    }

    /**
     * 设置是否使用标题和左边的栏
     *
     * @param isLeftOpen 使用坐标的第一栏
     * @param isTopOpen  使用标题栏
     */
    public void setTitle(boolean isLeftOpen, boolean isTopOpen) {
        this.isLeftOpen = isLeftOpen;
        this.isTopOpen = isTopOpen;

        mLeftRv.setVisibility(isLeftOpen ? VISIBLE : GONE);
        mTitleRv.setVisibility(isTopOpen ? VISIBLE : GONE);
        mTitleHeadFl.setVisibility(isTopOpen ? VISIBLE : GONE);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.table_view, this, true);

        mTitleRv = findViewById(R.id.rv_head);
        mTitleHeadFl = findViewById(R.id.fl_head);
        mLeftRv = findViewById(R.id.rv_left);
        mTableRv = findViewById(R.id.rv_table);

        mTitleRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mLeftRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void setAdapter(TableAdapter<Data> adapter) {
        this.mTableAdapter = adapter;
        if (mTableAdapter != null) {
            mGridAdapter = new GridAdapterProxy<>(mTableAdapter);
            mLeftAdapter = new LeftAdapterProxy<>(mTableAdapter);
            mTitleAdapter = new TitleAdapterProxy<>(mTableAdapter);
            mTableAdapter.setTitleAdapter(mTitleAdapter, mLeftAdapter, mGridAdapter);

            mTableRv.setLayoutManager(new TableLayoutManager(mMode, w, h));
            mTableRv.setAdapter(mGridAdapter);
            mLeftRv.setAdapter(mLeftAdapter);
            mTitleRv.setAdapter(mTitleAdapter);

            if (mTitleAdapter.getItemCount() > 0) {

            }


        }
    }

    void setHeadFirstItem() {
        if (mTitleAdapter == null || mTitleAdapter.getItemCount() == 0)
            return;

        if(mTitleHeadFl.getChildCount() > 0)
            return;


    }


}
