package com.orient.ui.rv;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class TwoSideLayoutManager extends RecyclerView.LayoutManager {
    // TODO 按照LinearLayoutManager编写

    // 开始的一边
    public static final int START_LEFT = 1;
    public static final int START_RIGHT = 2;

    private static int DEFAULT_SIDE = START_LEFT;

    private int mStartSide;

    public TwoSideLayoutManager(Context context,int startSide) {
        this.mStartSide = startSide;
    }

    public TwoSideLayoutManager(Context context) {
        this(context,DEFAULT_SIDE);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private static final String TAG = "TwoSideLayoutManager";

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }


    public static class LayoutParams extends RecyclerView.LayoutParams{
        // 无效的边
        public static final int INVALID_SIDE_ID = -1;
        public static final int SIDE_LEFT = 1;
        public static final int SIDE_RIGHT = 2;
        int side = INVALID_SIDE_ID;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

        public int getSide() {
            return side;
        }
    }
}
