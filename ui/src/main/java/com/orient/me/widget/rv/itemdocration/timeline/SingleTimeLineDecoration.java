package com.orient.me.widget.rv.itemdocration.timeline;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orient.me.data.ITimeItem;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class SingleTimeLineDecoration extends TimeLine {
    public SingleTimeLineDecoration(Config config) {
        super(config);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int pos = params.getViewAdapterPosition();
        ITimeItem timeItem = timeItems.get(pos);

        if ((mFlag & FLAG_SAME_TITLE_HIDE) != 0) {
            if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                if (pos == 0 || !timeItem.getTitle().equals(lastTitle)) {
                    outRect.set(mLineOffset + mLineWidth, mTopOffset, 0, 0);
                } else {
                    outRect.set(mLineOffset + mLineWidth, 0, 0, 0);
                }
                lastTitle = timeItem.getTitle();
            } else {
                outRect.set(mLineOffset + mLineWidth + mLeftOffset, 0, 0, 0);
            }
        } else {
            if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                outRect.set(mLineOffset + mLineWidth, mTopOffset, 0, 0);
            } else {
                outRect.set(mLineOffset + mLineWidth + mLeftOffset, 0, 0, 0);
            }
        }
    }


    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();
        if (childCount == 0)
            return;

        // 绘制处理
        // 1. 绘制标题
        drawTitle(c, parent);
        // 2. 绘制线
        drawVerticalLine(c, parent);
        // 3. 绘制点
        drawPoint(c, parent);
    }

    /**
     * 绘制标题
     */
    @Override
    protected void drawTitle(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        String mLastTitle = null;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getTop();
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int pos = params.getViewAdapterPosition();
            ITimeItem timeItem = timeItems.get(pos);
            int mLeft, mTop, mRight, mBottom;

            if ((mFlag & FLAG_SAME_TITLE_HIDE) != 0) {
                if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                    if (pos == 0 || !timeItem.getTitle().equals(mLastTitle)) {
                        mLeft = child.getLeft() - params.leftMargin - mLineOffset - mLineWidth;
                        mTop = child.getTop() - params.topMargin - mTopOffset;
                        mRight = child.getRight();
                        mBottom = child.getTop() - params.topMargin;
                        if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                            canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                        onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, pos);
                    }
                } else if ((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
                    if (pos == 0 || !timeItem.getTitle().equals(mLastTitle)) {
                        mLeft = child.getLeft() - params.leftMargin - (mLineOffset + mLineWidth) - mLeftOffset;
                        mTop = child.getTop();
                        mRight = child.getLeft() - params.leftMargin - (mLineOffset + mLineWidth);
                        mBottom = child.getBottom();
                        if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                            canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                        onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, pos);
                    }
                }
                mLastTitle = timeItem.getTitle();
            } else {
                if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                    // 绘制上面的标题
                    // TODO 验证RecyclerView子布局中的内外边距
                    mLeft = child.getLeft() - params.leftMargin - mLineOffset - mLineWidth;
                    mTop = child.getTop() - params.topMargin - mTopOffset;
                    mRight = child.getRight();
                    mBottom = child.getTop() - params.topMargin;
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, pos);
                } else if ((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
                    mLeft = child.getLeft() - params.leftMargin - (mLineOffset + mLineWidth) - mLeftOffset;
                    mTop = child.getTop();
                    mRight = child.getLeft() - params.leftMargin - (mLineOffset + mLineWidth);
                    mBottom = child.getBottom();
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, pos);
                }
            }
        }
    }

    /**
     * 绘制标题
     *
     * @param left   绘制文本区域范围
     * @param top    绘制文本区域范围
     * @param right  绘制文本区域范围
     * @param bottom 绘制文本区域范围
     * @param pos    使用数据的位置
     */
    protected abstract void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int pos);


    @Override
    protected void drawVerticalLine(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int left = parent.getPaddingLeft();
        int bottom;
        int childCount = parent.getChildCount();

        if ((mFlag & FLAG_LINE_DIVIDE) != 0 && (mFlag & FLAG_SAME_TITLE_HIDE) != 0) {
            // 这个模式位置写死了
            String mLastTitle = null;
            int beginY = 0, endY = 0;
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                ITimeItem item = timeItems.get(i);
                if (i == 0 || !item.getTitle().equals(mLastTitle) || i == childCount - 1) {
                    if (i == 0) {
                        beginY = (child.getTop() + child.getBottom()) / 2;
                    } else if (i == childCount - 1) {
                        endY = (child.getTop() + child.getBottom()) / 2;
                        c.drawLine(left + mLineOffset / 2, beginY, left + mLineOffset / 2 + mLineWidth, endY, mLinePaint);
                    } else {
                        View lastChild = parent.getChildAt(i - 1);
                        endY = (lastChild.getTop() + lastChild.getBottom()) / 2;
                        if (endY != beginY) {
                            c.drawLine(left + mLineOffset / 2, beginY, left + mLineOffset / 2 + mLineWidth, endY, mLinePaint);
                        }
                        beginY = (child.getTop() + child.getBottom()) / 2;
                    }
                }
                mLastTitle = item.getTitle();
            }
        } else {
            View lastChild = parent.getChildAt(childCount - 1);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) lastChild.getLayoutParams();
            if ((mFlag & FLAG_LINE_BEGIN_TO_END) != 0) {
                if (params.getViewAdapterPosition() == timeItems.size() - 1) {
                    bottom = (lastChild.getBottom() + lastChild.getTop()) / 2;
                } else {
                    bottom = lastChild.getBottom();
                }
            } else {
                bottom = lastChild.getBottom();
            }
            c.drawLine(left + mLineOffset / 2 + mLeftOffset, top, left + mLineOffset / 2 + mLineWidth + mLeftOffset, top + bottom, mLinePaint);
        }


    }

    @Override
    protected void drawPoint(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            // 圆心坐标
            int cx, cy;
            int top = child.getTop();
            int bottom = child.getBottom();
            cy = (bottom + top) / 2;
            int r = (top - bottom) / 2;
            r = Math.min((mLineOffset * 2 + mLineWidth) / 2, r);

            if ((mFlag & FLAG_TITLE_POS_NONE) != 0 || (mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                cx = (mLineOffset + (mLineWidth + 1)) / 2;
            } else {
                cx = mLeftOffset + (mLineOffset + (mLineWidth + 1)) / 2;
            }

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int pos = params.getViewAdapterPosition();
            if ((mFlag & FLAG_DOT_RES) != 0) {
                ITimeItem timeItem = timeItems.get(pos);
                if (timeItem != null) {
                    Drawable drawable = ContextCompat.getDrawable(mContext, timeItem.getResource());
                    onDrawDotResItem(c, cx, cy, r, drawable, pos);
                }
            } else
                onDrawDotItem(c, cx, cy, r, pos);
        }
    }

    /**
     * 绘制原点
     *
     * @param cx     圆心x
     * @param cy     圆心y
     * @param radius 最大半径
     * @param pos    位置
     */
    protected void onDrawDotItem(Canvas canvas, int cx, int cy, int radius, int pos) {

    }

    /**
     * @param cx       圆心X
     * @param cy       圆心Y
     * @param radius   最大半径
     * @param drawable 绘制的Drawable
     * @param pos      位置
     */
    protected void onDrawDotResItem(Canvas canvas, int cx, int cy, int radius, Drawable drawable, int pos) {

    }


}
