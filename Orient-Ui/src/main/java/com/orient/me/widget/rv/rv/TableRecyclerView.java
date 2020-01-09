package com.orient.me.widget.rv.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.orient.me.widget.rv.adapter.GridAdapter;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;

public class TableRecyclerView extends RecyclerView implements ScrollerCallback {
    private static final String TAG = "TableRecyclerView";

    // 滑动方向
    private int scrollFlag = -1;
    private boolean isCanScroll = true;

    private float mLastX, mLastY;
    private float mCurX, mCurY;


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
                && getAdapter() instanceof GridAdapter) {
            ((TableLayoutManager) layoutManager).setCoordinateCallback((GridAdapter) adapter);
        }
    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout instanceof TableLayoutManager) {
            ((TableLayoutManager) layout).setScrollerCallback(this);
        }

        if (getAdapter() != null
                && getAdapter() instanceof GridAdapter
                && layout instanceof TableLayoutManager) {
            ((TableLayoutManager) layout).setCoordinateCallback((GridAdapter) getAdapter());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = e.getX();
                mLastY = e.getY();
                scrollFlag = -1;
                isCanScroll = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(scrollFlag != -1)
                    break;
                mCurX = e.getX();
                mCurY = e.getY();
                float deltaX = Math.abs(mCurX - mLastX);
                float deltaY = Math.abs(mCurY - mLastY);
                mLastX = mCurX;
                mLastY = mCurY;
                int delta = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if(deltaX < delta && deltaY < delta){
                    break;
                }
                Log.e(TAG,"dx:"+deltaX+",dy:"+deltaY+",d:"+delta);
                if(Math.abs(deltaX) <= Math.abs(deltaY)) {
                    scrollFlag = RecyclerView.VERTICAL;
                    //Log.e(TAG,"lastX:"+mLastX+",lastY:"+mLastY+",curX:"+mCurX+",curY:"+mCurY+",deltaX:"+deltaX+",deltaY:"+deltaY);
                    //Log.e(TAG,"orientation:------Vertical");
                }else {
                    scrollFlag = RecyclerView.HORIZONTAL;
                    //Log.e(TAG,"lastX:"+mLastX+",lastY:"+mLastY+",curX:"+mCurX+",curY:"+mCurY+",deltaX:"+deltaX+",deltaY:"+deltaY);
                    //Log.e(TAG,"orientation:------Horizontal");
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurX = -1;
                mCurY = -1;
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean canScrollVertical() {
        return scrollFlag != RecyclerView.HORIZONTAL;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == SCROLL_STATE_IDLE && !isCanScroll) {
            isCanScroll = true;
            if (getLayoutManager() instanceof TableLayoutManager) {
                ((TableLayoutManager) getLayoutManager()).clearScrollFlag();
            }
        }
    }
}
