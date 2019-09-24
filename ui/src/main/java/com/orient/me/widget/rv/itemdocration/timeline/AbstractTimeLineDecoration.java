package com.orient.me.widget.rv.itemdocration.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orient.me.data.ITimeItem;
import com.orient.me.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTimeLineDecoration extends RecyclerView.ItemDecoration {

    public static final int FLAG_TITLE_POS_NONE = 0X0001;
    public static final int FLAG_TITLE_TYPE_TOP = 0x0002;
    public static final int FLAG_TITLE_TYPE_LEFT = 0x0004;
    public static final int FLAG_LINE_DIVIDE = 0x0010;
    public static final int FLAG_LINE_FULL = 0x0020;


    // 目标
    // 支持类型
    // 1. 支持绘制右边
    // 2. 支持绘制上边的标题
    // 3. 支持中间绘制原点或者使用Drawable
    // 4. 支持自定义绘制

    private Context mContext;
    protected List<? extends ITimeItem> timeItems;
    // 是否启用同样的 标记 隐藏
    private boolean isSameTitleHide = true;
    // 标题放置的类型
    private int mFlag = FLAG_TITLE_TYPE_TOP|FLAG_LINE_DIVIDE;
    // 上次的标题
    private String lastTitle = null;


    // 标题分两种，
    // 1. 上方
    // 2. 左侧
    private int mTitleColor;
    private int mTopOffset;
    private int mLeftOffset;
    private Paint mTextPaint;
    private int mTitleFontSize;
    private int mBgColor;
    private Paint mBgPaint;

    // 线
    private int mLineColor;
    private Paint mLinePaint;
    private int mLineOffset;
    private int mLineWidth;

    // 点
    private int mDotColor;
    private int mDotRes;
    private Paint mDotPaint;

    public AbstractTimeLineDecoration(Context context) {
        this(context, new ArrayList());
    }

    public AbstractTimeLineDecoration(Context context, List<? extends ITimeItem> timeItems) {
        this.mContext = context;
        this.timeItems = timeItems;
        init();
    }

    private void init() {
        mLineColor = Color.parseColor("#8d9ca9");
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mLinePaint.setColor(mLineColor);
        mLineOffset = UIUtils.dip2px(10);
        mLineWidth = UIUtils.dip2px(1);

        mTitleColor = Color.parseColor("#008577");
        mTopOffset = UIUtils.dip2px(40);
        mTitleFontSize = UIUtils.sp2px(mContext, 20);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint.setTextSize(mTitleFontSize);
        mTextPaint.setColor(mTitleColor);
        mTextPaint = new Paint();

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int pos = params.getViewAdapterPosition();
        ITimeItem timeItem = timeItems.get(pos);

        if (isSameTitleHide) {
            if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                if (pos == 0 || !timeItem.getTitle().equals(lastTitle)) {
                    outRect.set(mLineOffset, mTopOffset, 0, 0);
                } else {
                    outRect.set(mLineOffset, 0, 0, 0);
                }
                lastTitle = timeItem.getTitle();
            } else {
                outRect.set(mLineOffset + mLeftOffset, 0, 0, 0);
            }
        } else {
            if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                outRect.set(mLineOffset, mTopOffset, 0, 0);
            } else {
                outRect.set(mLineOffset + mLeftOffset, 0, 0, 0);
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
    private void drawTitle(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        String mLastTitle = null;
        mTextPaint.setTextSize(UIUtils.sp2px(mContext, 20));
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getTop();
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int pos = params.getViewAdapterPosition();
            ITimeItem timeItem = timeItems.get(pos);
            int mLeft, mTop, mRight, mBottom;

            if (isSameTitleHide) {
                if ((mFlag & FLAG_TITLE_TYPE_TOP)!=0) {
                    if (pos == 0 || !timeItem.getTitle().equals(mLastTitle)) {
                        mLeft = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth);
                        mTop = child.getTop() - params.topMargin - mTopOffset;
                        mRight = child.getRight();
                        mBottom = child.getTop() - params.topMargin;
                        onDrawTitleItem(canvas, mTextPaint, mBgPaint, mLeft, mTop, mRight, mBottom, i);
                    }
                } else {
                    if (pos == 0 || !timeItem.getTitle().equals(mLastTitle)) {
                        mLeft = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth) - mLeftOffset;
                        mTop = child.getTop();
                        mRight = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth);
                        mBottom = child.getBottom();
                        onDrawTitleItem(canvas, mTextPaint, mBgPaint, mLeft, mTop, mRight, mBottom, i);
                    }
                }
                mLastTitle = timeItem.getTitle();
            } else {
                if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                    // 绘制上面的标题
                    // TODO 验证RecyclerView子布局中的内外边距
                    mLeft = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth);
                    mTop = child.getTop() - params.topMargin - mTopOffset;
                    mRight = child.getRight();
                    mBottom = child.getTop() - params.topMargin;
                    onDrawTitleItem(canvas, mTextPaint, mBgPaint, mLeft, mTop, mRight, mBottom, i);
                } else {
                    mLeft = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth) - mLeftOffset;
                    mTop = child.getTop();
                    mRight = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth);
                    mBottom = child.getBottom();
                    onDrawTitleItem(canvas, mTextPaint, mBgPaint, mLeft, mTop, mRight, mBottom, i);
                }
            }
        }
    }

    /**
     * 绘制标题
     *
     * @param textPaint 文本画笔
     * @param bgPaint   背景画笔
     * @param left      绘制文本区域范围
     * @param top       绘制文本区域范围
     * @param right     绘制文本区域范围
     * @param bottom    绘制文本区域范围
     * @param pos       使用数据的位置
     */
    protected abstract void onDrawTitleItem(Canvas canvas, Paint textPaint, Paint bgPaint, int left, int top, int right, int bottom, int pos);


    private void drawVerticalLine(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int left = parent.getPaddingLeft();
        int bottom;
        int childCount = parent.getChildCount();

        if((mFlag & FLAG_LINE_DIVIDE) != 0 && isSameTitleHide){
            // 这个模式位置写死了
            String mLastTitle = null;
            int beginY = 0,endY = 0;
            for(int i = 0;i<childCount;i++){
                View child = parent.getChildAt(i);
                ITimeItem item = timeItems.get(i);
                if(i == 0 || !item.getTitle().equals(mLastTitle)|| i == childCount -1){
                    if(i == 0){
                        beginY = (child.getTop() + child.getBottom())/2;
                    }else if(i == childCount -1){
                        endY = (child.getTop() + child.getBottom())/2;
                        c.drawLine(left + mLineOffset, beginY, left + mLineOffset + mLineWidth, endY, mLinePaint);
                    }else {
                        View lastChild = parent.getChildAt(i-1);
                        endY = (lastChild.getTop() + lastChild.getBottom())/2;
                        if(endY != beginY){
                            c.drawLine(left + mLineOffset, beginY, left + mLineOffset + mLineWidth, endY, mLinePaint);
                        }
                        beginY = (child.getTop() + child.getBottom())/2;
                    }
                }
                mLastTitle = item.getTitle();
            }
        }else {
            View lastChild = parent.getChildAt(childCount - 1);
            bottom = lastChild.getBottom();

            c.drawLine(left + mLineOffset, top, left + mLineOffset + mLineWidth, top + bottom, mLinePaint);
        }


    }

    private void drawPoint(Canvas c, RecyclerView parent) {
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

            if ((mFlag & FLAG_TITLE_POS_NONE) != 0 || (mFlag &FLAG_TITLE_TYPE_TOP)!= 0) {
                cx = mLineOffset + (mLineWidth + 1) / 2;
            } else {
                cx = mLeftOffset + mLineOffset + (mLineWidth + 1) / 2;
            }
            onDrawPointItem(c, mDotPaint, cx, cy, r, i);
        }
    }

    /**
     * 绘制原点
     *
     * @param dotPaint 原点的画笔
     * @param cx       圆心x
     * @param cy       原因y
     * @param radius   最大半径
     * @param pos      位置
     */
    protected abstract void onDrawPointItem(Canvas canvas, Paint dotPaint, int cx, int cy, int radius, int pos);


}
