package com.orient.me.widget.rv.itemdocration.timeline;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.orient.me.data.ITimeItem;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class DoubleTimeLineDecoration extends TimeLine {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private int mStartSide;

    public DoubleTimeLineDecoration(Config config) {
        super(config);
        mStartSide = LEFT;
    }

    /**
     * 设置起始边
     * @param startSide Left RIGHT
     */
    public void setStartSide(int startSide) {
        this.mStartSide = startSide;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // 这里应该不需要偏移了
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int pos = params.getViewAdapterPosition();
        ITimeItem timeItem = timeItems.get(pos);

        int side = pos % 2;
        if ((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
            if (side == mStartSide)
                outRect.set(0, 0, (mLineOffset+mLineWidth)/2 + mLeftOffset, 0);
            else
                outRect.set((mLineOffset+mLineWidth)/2 + mLeftOffset, 0, 0, 0);
        } else {
            if (side == mStartSide)
                outRect.set(0, 0, (mLineOffset+mLineWidth)/2, 0);
            else
                outRect.set((mLineOffset+mLineWidth)/2, 0, 0, 0);
        }
    }


    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        // 兼容4.0硬件加速无效
        parent.setLayerType(View.LAYER_TYPE_SOFTWARE, mDotPaint);

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
        // 注意：隐藏相同标题对两侧布局不重要
        int centerX = parent.getMeasuredWidth() / 2;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getTop();
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int pos = params.getViewAdapterPosition();
            ITimeItem timeItem = timeItems.get(pos);
            int mLeft, mTop, mRight, mBottom;

            if ((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
                if (child.getLeft() >= parent.getMeasuredWidth() / 2) {
                    mLeft = parent.getPaddingLeft();
                    mTop = child.getTop();
                    mRight = child.getLeft() - params.leftMargin;
                    mBottom = child.getBottom();
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, centerX, pos, false);
                } else {
                    mLeft = child.getRight() + params.rightMargin;
                    mTop = child.getTop();
                    mRight = parent.getMeasuredWidth() - parent.getPaddingRight();
                    mBottom = child.getBottom();
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, centerX, pos, true);
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
    protected abstract void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int centerX, int pos, boolean isLeft);


    @Override
    protected void drawVerticalLine(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        final int left = parent.getPaddingLeft();
        int bottom;
        int childCount = parent.getChildCount();

        if ((mFlag & FLAG_LINE_CONSISTENT) != 0) {
            View lastChild = parent.getChildAt(childCount - 1);
            bottom = lastChild.getBottom();
            c.drawLine(parent.getMeasuredWidth() / 2, top, parent.getMeasuredWidth() / 2, bottom, mLinePaint);
        } else {
            View firstChild = parent.getChildAt(0);
            top = (firstChild.getTop() + firstChild.getBottom()) / 2;
            View lastChild = parent.getChildAt(childCount - 1);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) lastChild.getLayoutParams();
            if(params.getViewAdapterPosition() == timeItems.size() - 1) {
                bottom = (lastChild.getBottom() + lastChild.getTop()) / 2;
            }else {
                bottom = lastChild.getBottom();
            }
            c.drawLine(parent.getMeasuredWidth() / 2, top, parent.getMeasuredWidth() / 2, bottom, mLinePaint);
        }
    }

    @Override
    protected void drawPoint(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int pos = params.getViewAdapterPosition();
            // 圆心坐标
            int cx, cy;
            int top = child.getTop();
            int bottom = child.getBottom();
            cx = parent.getMeasuredWidth() / 2;
            cy = (bottom + top) / 2;
            int r = (top - bottom) / 2;
            r = Math.min((mLineOffset + mLineWidth) / 2, r);

            if ((mFlag & FLAG_DOT_RES) != 0) {
                ITimeItem timeItem = timeItems.get(pos);
                if(timeItem != null) {
                    Drawable drawable = ContextCompat.getDrawable(mContext, timeItem.getResource());
                    onDrawDotResItem(c,cx,cy,r,drawable,pos);
                }
            } else
                onDrawDotItem(c, cx, cy, r, pos);

        }
    }

    /**
     * 绘制原点
     *
     * @param cx     圆心x
     * @param cy     原因y
     * @param radius 最大半径
     * @param pos    位置
     */
    protected void onDrawDotItem(Canvas canvas, int cx, int cy, int radius, int pos) {

    }

    /**
     *
     * @param cx 圆心X
     * @param cy 圆心Y
     * @param radius 最大半径
     * @param drawable 绘制的Drawable
     * @param pos 位置
     */
    protected void onDrawDotResItem(Canvas canvas, int cx, int cy, int radius, Drawable drawable,int pos){

    }


}
