package com.orient.me.widget.rv.itemdocration.timeline;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orient.me.data.ITimeItem;
import com.orient.me.utils.UIUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractTimeLineDecoration extends RecyclerView.ItemDecoration {

    // 标题
    public static final int FLAG_TITLE_POS_NONE = 0X0001;
    public static final int FLAG_TITLE_TYPE_TOP = 0x0002;
    public static final int FLAG_TITLE_TYPE_LEFT = 0x0004;
    public static final int FLAG_TITLE_DRAW_BG = 0x0008;
    // 时间线
    public static final int FLAG_LINE_DIVIDE = 0x0010;
    public static final int FLAG_LINE_FULL = 0x0020;
    public static final int FLAG_SAME_TITLE_HIDE = 0x0100;
    // 时间点
    public static final int FLAG_DOT_RES = 0x1000;
    public static final int FLAG_DOT_DRAW = 0x2000;


    // 目标
    // 支持类型
    // 1. 支持绘制右边
    // 2. 支持绘制上边的标题
    // 3. 支持中间绘制原点或者使用Drawable
    // 4. 支持自定义绘制

    protected List<? extends ITimeItem> timeItems;
    // 标题放置的类型
    private int mFlag;
    // 上次的标题
    private String lastTitle = null;


    // 标题分两种，
    // 1. 上方
    // 2. 左侧
    private int mTitleColor;
    protected int mTopOffset;
    protected int mLeftOffset;
    protected Paint mTextPaint;
    private int mTitleFontSize;
    private int mBgColor;
    protected Paint mBgPaint;

    // 线
    private int mLineColor;
    protected Paint mLinePaint;
    protected int mLineOffset;
    private int mLineWidth;

    // 点
    protected Paint mDotPaint;
    private int dotRadius;

    public AbstractTimeLineDecoration(Config config) {
        Context mContext = config.context;
        this.timeItems = config.timeItems;
        this.mFlag = config.flag;

        // 标题
        this.mTitleColor = config.titleColor;
        if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
            mTopOffset = UIUtils.dip2px(config.titleOffset);
        } else if ((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
            mLeftOffset = UIUtils.dip2px(config.titleOffset);
        }
        this.mTitleFontSize = UIUtils.sp2px(mContext, config.titleFontSize);
        this.mBgColor = config.bgColor;

        // 时间线
        this.mLineColor = config.lineColor;
        this.mLineOffset = UIUtils.dip2px(config.lineOffset);
        this.mLineWidth = UIUtils.dip2px(1);

        // 点
        this.dotRadius = UIUtils.dip2px(config.dowRadius);
        init();
    }

    private void init() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mLinePaint.setColor(mLineColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint.setTextSize(mTitleFontSize);
        mTextPaint.setColor(mTitleColor);
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

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
                        mLeft = child.getLeft() - params.leftMargin - (mLineOffset);
                        mTop = child.getTop() - params.topMargin - mTopOffset;
                        mRight = child.getRight();
                        mBottom = child.getTop() - params.topMargin;
                        if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                            canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                        onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, i);
                    }
                } else if((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
                    if (pos == 0 || !timeItem.getTitle().equals(mLastTitle)) {
                        mLeft = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth) - mLeftOffset;
                        mTop = child.getTop();
                        mRight = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth);
                        mBottom = child.getBottom();
                        if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                            canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                        onDrawTitleItem(canvas,  mLeft, mTop, mRight, mBottom, i);
                    }
                }
                mLastTitle = timeItem.getTitle();
            } else {
                if ((mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                    // 绘制上面的标题
                    // TODO 验证RecyclerView子布局中的内外边距
                    mLeft = child.getLeft() - params.leftMargin - (mLineOffset);
                    mTop = child.getTop() - params.topMargin - mTopOffset;
                    mRight = child.getRight();
                    mBottom = child.getTop() - params.topMargin;
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas,  mLeft, mTop, mRight, mBottom, i);
                } else if((mFlag & FLAG_TITLE_TYPE_LEFT) != 0) {
                    mLeft = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth) - mLeftOffset;
                    mTop = child.getTop();
                    mRight = child.getLeft() - params.leftMargin - (mLineOffset * 2 + mLineWidth);
                    mBottom = child.getBottom();
                    if ((mFlag & FLAG_TITLE_DRAW_BG) != 0)
                        canvas.drawRect(mLeft, mTop, mRight, mBottom, mBgPaint);
                    onDrawTitleItem(canvas, mLeft, mTop, mRight, mBottom, i);
                }
            }
        }
    }

    /**
     * 绘制标题
     *
     * @param left      绘制文本区域范围
     * @param top       绘制文本区域范围
     * @param right     绘制文本区域范围
     * @param bottom    绘制文本区域范围
     * @param pos       使用数据的位置
     */
    protected abstract void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int pos);


    private void drawVerticalLine(Canvas c, RecyclerView parent) {
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
                        c.drawLine(left + mLineOffset, beginY, left + mLineOffset + mLineWidth, endY, mLinePaint);
                    } else {
                        View lastChild = parent.getChildAt(i - 1);
                        endY = (lastChild.getTop() + lastChild.getBottom()) / 2;
                        if (endY != beginY) {
                            c.drawLine(left + mLineOffset, beginY, left + mLineOffset + mLineWidth, endY, mLinePaint);
                        }
                        beginY = (child.getTop() + child.getBottom()) / 2;
                    }
                }
                mLastTitle = item.getTitle();
            }
        } else {
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

            if ((mFlag & FLAG_TITLE_POS_NONE) != 0 || (mFlag & FLAG_TITLE_TYPE_TOP) != 0) {
                cx = mLineOffset + (mLineWidth + 1) / 2;
            } else {
                cx = mLeftOffset + mLineOffset + (mLineWidth + 1) / 2;
            }
            onDrawPointItem(c, cx, cy, r, i);
        }
    }

    /**
     * 绘制原点
     *
     * @param cx       圆心x
     * @param cy       原因y
     * @param radius   最大半径
     * @param pos      位置
     */
    protected abstract void onDrawPointItem(Canvas canvas, int cx, int cy, int radius, int pos);


    public static class Config {
        Context context;
        List<? extends ITimeItem> timeItems = new ArrayList<>();
        int flag = 0;
        // 标题
        int titleColor = Color.parseColor("#4e5864");
        int titleFontSize = 20;
        int bgColor;
        int titleOffset = 40;
        // 线
        int lineColor = Color.parseColor("#8d9ca9");
        int lineOffset = 30;
        // 点
        int dowRadius = 8;
    }

    public static class Builder {
        private Config mConfig;

        public Builder(Context context) {
            this(context,new ArrayList());
        }

        public Builder(Context context,List<? extends ITimeItem> timeItems) {
            this.mConfig = new Config();
            this.mConfig.context = context;
            this.mConfig.timeItems = timeItems;
        }

        /**
         * 设置标题
         *
         * @param titleColor 标题文本的颜色
         * @param fontSize   标题文本的大小 dp
         * @param bgColor    背景颜色
         */
        public Builder setTitle(int titleColor, int fontSize, int bgColor) {
            this.mConfig.titleColor = titleColor;
            this.mConfig.titleFontSize = fontSize;
            this.mConfig.bgColor = bgColor;
            this.mConfig.flag |= FLAG_TITLE_DRAW_BG;
            return this;
        }

        /**
         * 设置标题
         *
         * @param titleColor 标题文本的颜色
         * @param fontSize   标题文本的大小 dp
         */
        public Builder setTitle(int titleColor, int fontSize) {
            this.mConfig.titleColor = titleColor;
            this.mConfig.titleFontSize = fontSize;
            return this;
        }

        /**
         * 可以设置Title的位置，比如将标题设置在顶部或者将标题设置左边
         *
         * @param type        类型  FLAG_TITLE_POS_NONE/FLAG_TITLE_TYPE_TOP/FLAG_TITLE_TYPE_LEFT
         * @param titleOffset 偏移量
         */
        public Builder setTitleStyle(int type, int titleOffset) {
            this.mConfig.flag |= type;
            this.mConfig.titleOffset = titleOffset;
            return this;
        }

        /**
         * 启动隐藏相同标题
         */
        public Builder setSameTitleHide() {
            this.mConfig.flag |= FLAG_SAME_TITLE_HIDE;
            return this;
        }

        /**
         * @param type       type 时间线的类型
         * @param lineOffset 时间轴左边偏移的大小，右边也会偏移同样的大小
         */
        public Builder setLine(int type, int lineOffset, int lineColor) {
            this.mConfig.flag |= type;
            this.mConfig.lineOffset = lineOffset;
            this.mConfig.lineColor = lineColor;
            return this;
        }

        /**
         * 设置原点
         *
         * @param type   点的类型
         * @param radius 点的半径
         */
        public Builder setDot(int type, int radius) {
            this.mConfig.flag |= type;
            this.mConfig.dowRadius = radius;
            return this;
        }

        /**
         * 构建
         *
         * @param cls 构建的类
         * @return T
         */
        public AbstractTimeLineDecoration build(Class<? extends AbstractTimeLineDecoration> cls) {
            AbstractTimeLineDecoration t = null;
            try {
                Constructor<? extends AbstractTimeLineDecoration> con = cls.getConstructor(Config.class);
                t = con.newInstance(mConfig);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return t;
        }

    }


}
