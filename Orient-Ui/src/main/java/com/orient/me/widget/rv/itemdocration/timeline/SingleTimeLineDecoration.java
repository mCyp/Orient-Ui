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
                if (pos == 0 || !timeItem.getTitle().equals(timeItems.get(pos - 1).getTitle())) {
                    outRect.set(mLineOffset + mLineWidth, mTopOffset, 0, 0);
                } else {
                    outRect.set(mLineOffset + mLineWidth, 0, 0, 0);
                }
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

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            // 适配器中的实际位置
            int pos = params.getViewAdapterPosition();
            ITimeItem timeItem = timeItems.get(pos);
            int mLeft, mTop, mRight, mBottom;

            // 绘制上侧 FLAG_TITLE_TYPE_TOP 和左侧 FLAG_TITLE_TYPE_LEFT略有不同
            // 1. 上侧
            // 绘制区域：
            //          上下：高度 = mTopOffset, 在子视图之上，间隔着子视图的topMargin的长度
            //          左右：宽度 = 子视图宽度 + 左右的margin长度
            // 2. 左侧
            // 绘制区域
            //          上下：高度 = 子视图高度
            //          左右：宽度 = 时间线偏移量 + 时间线的宽度 + 标题偏移量， 间隔着左margin的长度
            if ((mFlag & FLAG_SAME_TITLE_HIDE) != 0) {
                if (i == 0 && pos != 0) {
                    if (timeItem.getTitle().equals(timeItems.get(pos - 1).getTitle())) {
                        continue;
                    }
                }

                if (pos != 0 && timeItem.getTitle().equals(timeItems.get(pos - 1).getTitle())) {
                    continue;
                }


                if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                    mLeft = parent.getPaddingLeft();
                    mTop = child.getTop() - params.topMargin - mTopOffset;
                    mRight = child.getRight() + params.rightMargin;
                    mBottom = child.getTop() - params.topMargin;
                    // 绘制背景
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, pos);
                } else if ((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
                    mLeft = parent.getPaddingLeft();
                    mTop = child.getTop();
                    mRight = child.getLeft() - params.leftMargin - (mLineOffset + mLineWidth);
                    mBottom = child.getBottom();
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, pos);
                }
            } else {
                if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                    // 绘制上面的标题
                    mLeft = parent.getPaddingLeft() + params.leftMargin;
                    mTop = child.getTop() - params.topMargin - mTopOffset;
                    mRight = child.getRight();
                    mBottom = child.getTop() - params.topMargin;
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, pos);
                } else if ((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
                    mLeft = parent.getPaddingLeft();
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
        final int left = parent.getPaddingLeft();
        int childCount = parent.getChildCount();

        // 绘制时间线的x坐标
        int beginX = left + mLineOffset / 2 + mLeftOffset;
        int endX = beginX + mLineWidth;
        int beginY = 0, endY = 0;

        if ((mFlag & FLAG_LINE_DIVIDE) != 0) {
            // 给相同的标题画 时间线
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int pos = params.getViewAdapterPosition();
                ITimeItem item = timeItems.get(pos);

                if (pos == 0 || !item.getTitle().equals(timeItems.get(pos - 1).getTitle()) || pos == timeItems.size() - 1
                    || i== childCount -1) {
                    if (pos == 0) {
                        beginY = (child.getTop() + child.getBottom()) / 2;
                    } else if (pos == timeItems.size() - 1) {
                        endY = (child.getTop() + child.getBottom()) / 2;
                        c.drawRect(beginX, beginY, endX, endY, mLinePaint);
                    } else if(!item.getTitle().equals(timeItems.get(pos - 1).getTitle())) {
                        //Log.e(SingleTimeLineDecoration.class.getSimpleName(),"i:"+i+",pos:"+pos);
                        View lastChild = parent.getChildAt(i - 1);
                        if(lastChild != null) {
                            endY = (lastChild.getTop() + lastChild.getBottom()) / 2;
                            if (endY != beginY) {
                                c.drawRect(beginX, beginY, endX, endY, mLinePaint);
                            }
                        }
                        beginY = (child.getTop() + child.getBottom()) / 2;
                    }else {
                        if(childCount == 1)
                            continue;

                        endY = child.getBottom();
                        c.drawRect(beginX, beginY, endX, endY, mLinePaint);
                    }
                }
            }
        } else if ((mFlag & FLAG_LINE_BEGIN_TO_END) != 0) {
            View lastChild = parent.getChildAt(childCount - 1);
            RecyclerView.LayoutParams lastParams = (RecyclerView.LayoutParams) lastChild.getLayoutParams();
            View firstChild = parent.getChildAt(0);
            RecyclerView.LayoutParams firstParams = (RecyclerView.LayoutParams) firstChild.getLayoutParams();

            if (firstParams.getViewAdapterPosition() == 0) {
                beginY = (firstChild.getTop() + firstChild.getBottom()) / 2;
            } else {
                beginY = firstChild.getTop();
            }

            if (lastParams.getViewAdapterPosition() == timeItems.size() - 1) {
                endY = (lastChild.getBottom() + lastChild.getTop()) / 2;
            } else {
                endY = lastChild.getBottom();
            }

            c.drawRect(beginX, beginY, endX, endY, mLinePaint);
        } else {
            View lastChild = parent.getChildAt(childCount - 1);
            beginY = parent.getTop();
            endY = lastChild.getBottom();
            c.drawRect(beginX, beginY, endX, endY, mLinePaint);
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
            int r = (bottom - top) / 2;
            r = Math.min((mLineOffset + mLineWidth) / 2, r);

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
