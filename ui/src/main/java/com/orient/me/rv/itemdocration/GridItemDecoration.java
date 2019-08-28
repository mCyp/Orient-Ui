package com.orient.me.rv.itemdocration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.orient.me.data.IGridItem;
import com.orient.me.utils.UIUtils;

import java.util.List;

/**
 * 适用于GridLayoutManager的分割线
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    // 显示数据
    private List<? extends IGridItem> gridItems;
    // 画笔
    private Paint mTitlePaint;
    // 存放文字
    private Rect mRect;
    // 颜色
    private int mTitleBgColor;
    private int mTitleColor;
    private int mTitleHeight;
    private int mTitleFontSize;
    private boolean isDrawTitleBg = false;

    // 总的SpanSize
    private int totalSpanSize;
    private int mCurrentSpanSize;

    private GridItemDecoration(Config config) {
        init(config);

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setDither(true);

        mRect = new Rect();
    }

    private void init(Config config) {
        this.gridItems = config.gridItems;
        this.totalSpanSize = config.totalSpanSize;
        this.mTitleBgColor = config.titleBgColor;
        this.mTitleColor = config.titleTextColor;
        this.mTitleHeight = UIUtils.dip2px(config.titleHeight);;
        this.mTitleFontSize = config.titleFontSize;
        this.isDrawTitleBg = config.isDrawTitleBg;
    }

    public void updateGridItems(List<IGridItem> gridItems){
        this.gridItems = gridItems;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Context context = parent.getContext();
        mTitleFontSize = UIUtils.sp2px(context, mTitleFontSize);

        final int paddingLeft = parent.getPaddingLeft();
        final int paddingRight = parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            IGridItem item = gridItems.get(i);
            if (item == null || !item.isShow())
                continue;

            View child = parent.getChildAt(i);
            if (i == 0) {
                drawTitle(c, paddingLeft, paddingRight, child
                        , (RecyclerView.LayoutParams) child.getLayoutParams(), i);
            } else {
                IGridItem lastItem = gridItems.get(i - 1);
                if (lastItem != null && !item.getTag().equals(lastItem.getTag())) {
                    drawTitle(c, paddingLeft, paddingRight, child,
                            (RecyclerView.LayoutParams) child.getLayoutParams(), i);
                }
            }
        }

    }

    /**
     * 绘制标题
     *
     * @param canvas 画布
     * @param pl     左边距
     * @param pr     右边距
     * @param child  子View
     * @param params RecyclerView.LayoutParams
     * @param pos    位置
     */
    private void drawTitle(Canvas canvas, int pl, int pr, View child, RecyclerView.LayoutParams params, int pos) {
        if (isDrawTitleBg) {
            mTitlePaint.setColor(mTitleBgColor);
            canvas.drawRect(pl, child.getTop() - params.topMargin - mTitleHeight, pl
                    , child.getTop() - params.topMargin, mTitlePaint);
        }

        IGridItem item = gridItems.get(pos);
        String content = item.getTag();
        if (TextUtils.isEmpty(content))
            return;

        mTitlePaint.setColor(mTitleColor);
        mTitlePaint.setTextSize(mTitleFontSize);
        mTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTitlePaint.getTextBounds(content, 0, content.length(), mRect);
        float x = UIUtils.dip2px(20f);
        float y = child.getTop() - params.topMargin - (mTitleHeight - mRect.height()) / 2;
        canvas.drawText(content, x, y, mTitlePaint);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        IGridItem item = gridItems.get(position);
        if (item == null || !item.isShow())
            return;
        if (position == 0) {
            outRect.set(0, mTitleHeight, 0, 0);
            mCurrentSpanSize = item.getSpanSize();
        } else {
            if (!TextUtils.isEmpty(item.getTag()) && !item.getTag().equals(gridItems.get(position - 1).getTag())) {
                mCurrentSpanSize = item.getSpanSize();
            } else
                mCurrentSpanSize += item.getSpanSize();

            if (mCurrentSpanSize <= totalSpanSize) {
                outRect.set(0, mTitleHeight, 0, 0);
            }
        }
    }

    static class Config {
        // 数据
        List<? extends IGridItem> gridItems;
        // 总的SpanSize 来自GridLayoutManager
        int totalSpanSize;
        // 颜色 默认颜色
        boolean isDrawTitleBg = false;
        int titleBgColor;
        int titleTextColor = Color.parseColor("#4e5864");
        // 高度 40dp
        int titleHeight = 40;
        // 文本大小 24sp
        int titleFontSize = 40;
    }

    public static class Builder {
        private Config config;

        public Builder(List<? extends IGridItem> gridItems, int totalSpanSize) {
            config = new Config();
            config.gridItems = gridItems;
            config.totalSpanSize = totalSpanSize;
        }

        public Builder setTitleBgColor(int titleBgColor) {
            config.titleBgColor = titleBgColor;
            config.isDrawTitleBg = true;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor) {
            config.titleTextColor = titleTextColor;
            return this;
        }

        /**
         * 设置高度
         *
         * @param titleHeight 高度 单位为Dp
         */
        public Builder setTitleHeight(int titleHeight) {
            config.titleHeight = titleHeight;
            return this;
        }

        /**
         * 设置文本大小
         *
         * @param fontSize 文本带下 单位为Sp
         */
        public Builder setTitleFontSize(int fontSize) {
            config.titleFontSize = fontSize;
            return this;
        }

        public GridItemDecoration build() {
            return new GridItemDecoration(config);
        }
    }


}
