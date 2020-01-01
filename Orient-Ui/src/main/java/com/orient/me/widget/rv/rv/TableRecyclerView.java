package com.orient.me.widget.rv.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.orient.me.widget.rv.adapter.TableAdapter;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;

public class TableRecyclerView extends RecyclerView implements ScrollerCallback {

    private boolean isCanScrollHor = true;
    private boolean isCanScrollVer = true;

    private float mLastX;
    private float mLastY;
    private float mCurrentX;
    private float mCurrentY;

    public TableRecyclerView(@NonNull Context context) {
        super(context);
    }

    public TableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);

        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null
                && layoutManager instanceof TableLayoutManager
                && getAdapter() instanceof TableAdapter) {
            ((TableLayoutManager) layoutManager).setCoordinateCallback((TableAdapter) adapter);
        }
    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout instanceof TableLayoutManager) {
            ((TableLayoutManager) layout).setScrollerCallback(this);
        }

        if (getAdapter() != null
                && getAdapter() instanceof TableAdapter
                && layout instanceof TableLayoutManager) {
            ((TableLayoutManager) layout).setCoordinateCallback((TableAdapter) getAdapter());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getX();
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isCanScrollVer || !isCanScrollHor)
                    return super.dispatchTouchEvent(ev);

                mCurrentX = ev.getX();
                mCurrentY = ev.getY();
                if (Math.abs(mCurrentX - mLastX) > Math.abs(mCurrentY - mLastY)) {
                    isCanScrollVer = true;
                    isCanScrollHor = false;
                } else {
                    isCanScrollHor = true;
                    isCanScrollVer = false;
                }
                mLastX = mCurrentX;
                mLastY = mCurrentY;
                break;
            case MotionEvent.ACTION_UP:
                isCanScrollHor = true;
                isCanScrollVer = true;
                resetCoordinate();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void resetCoordinate() {
        mCurrentX = 0;
        mCurrentY = 0;
        mLastX = 0;
        mLastY = 0;
    }

    @Override
    public boolean canScrollVertical() {
        return isCanScrollVer;
    }

    @Override
    public boolean canScrollHorizontal() {
        return isCanScrollHor;
    }
}
