package com.orient.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;


import com.orient.ui.R;
import com.orient.ui.utils.UIUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.R.attr.drawableRight;

/**
 * *          _       _
 * *   __   _(_)_   _(_) __ _ _ __
 * *   \ \ / / \ \ / / |/ _` | '_ \
 * *    \ V /| |\ V /| | (_| | | | |
 * *     \_/ |_| \_/ |_|\__,_|_| |_|
 * <p>
 * Created by vivian on 2017/6/9.
 */

public class DotItemDecoration extends RecyclerView.ItemDecoration {
    public static final int STYLE_DRAW = 0;
    public static final int STYLE_RESOURCE = 1;

    //you can choose to draw or use resource as divider
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STYLE_DRAW, STYLE_RESOURCE})
    public @interface ItemStyle {

    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    //you can choose the orientation of item decoration
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VERTICAL, HORIZONTAL})
    public @interface Orientation {

    }

    private Context mContext;
    private Paint mLinePaint;
    private Drawable mDrawable;
    private Drawable mBottomDrawable;
    private Paint mDotPaint;
    private Paint mTextPaint;
    private Rect mTextRect;

    private SpanIndexListener mSpanIndexListener;
    private Config mConfig;

    public DotItemDecoration(Context context, Config config) {
        mContext = context;
        mConfig = config;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextRect = new Rect();
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setSpanIndexListener(SpanIndexListener spanIndexListener) {
        this.mSpanIndexListener = spanIndexListener;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int currentPosition = parent.getChildAdapterPosition(view);

        if (mConfig.mOrientation == VERTICAL) {
            outRect.left = mConfig.mItemPaddingLeft;
            outRect.right = mConfig.mItemPaddingRight;
            outRect.bottom = mConfig.mItemInterval;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mConfig.mTopDistance;
                //outRect.top = mConfig.mItemInterval;
            } else if (parent.getChildAdapterPosition(view) == 1) {
                if (mConfig.mItemInterval == 0) {
                    outRect.top = mConfig.mTopDistance;
                } else {
                    outRect.top = mConfig.mTopDistance + mConfig.mItemInterval;
                    //outRect.top = mConfig.mItemInterval * 2;
                }

            } else if (parent.getAdapter() != null && (currentPosition == itemCount - 1 || currentPosition == itemCount - 2)) {
                outRect.bottom = outRect.height() + mConfig.mBottomDistance + mConfig.mDotPaddingText + mConfig.mTextSize + mTextRect.height() + mConfig.mDotRadius;
            }
        } else {
            outRect.top = mConfig.mItemPaddingLeft;
            outRect.bottom = mConfig.mItemPaddingRight;
            outRect.right = mConfig.mItemInterval;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = mConfig.mTopDistance;
            } else if (parent.getChildAdapterPosition(view) == 1) {
                outRect.left = 2 * mConfig.mTopDistance;
            } else if (parent.getAdapter() != null && (currentPosition == itemCount - 1 || currentPosition == itemCount - 2)) {
                outRect.right = outRect.width() + mConfig.mItemInterval + mConfig.mBottomDistance + mConfig.mDotPaddingText + mConfig.mTextSize + mTextRect.width() + mConfig.mDotRadius;
            }
        }

        if (mSpanIndexListener == null) return;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams staggerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            int spanIndex = staggerLayoutParams.getSpanIndex();
            if (mSpanIndexListener != null && spanIndex != -1) {
                mSpanIndexListener.onSpanIndexChange(view, spanIndex);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mConfig.mStyle == STYLE_RESOURCE) {
            mDrawable = ContextCompat.getDrawable(mContext, mConfig.mDotRes);
        }
        mBottomDrawable = ContextCompat.getDrawable(mContext, mConfig.mBottomRes);

        mTextPaint.setColor(mConfig.mTextColor);
        mTextPaint.setTextSize(mConfig.mTextSize);

        mLinePaint.setColor(mConfig.mLineColor);
        mLinePaint.setStrokeWidth(mConfig.mLineWidth);

        mDotPaint.setColor(mConfig.mDotColor);

        if (mConfig.mOrientation == VERTICAL) {
            drawCenterVerticalLine(c, parent);
            drawVerticalItem(c, parent);
        } else {
            drawCenterHorizontalLine(c, parent);
            drawHorizontalItem(c, parent);
        }
    }

    public void drawCenterVerticalLine(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int parentWidth = parent.getMeasuredWidth();
        int bottom;

        final int childCount = parent.getChildCount();
        if (childCount == 0) return;
        View lastChild = parent.getChildAt(childCount - 1);
        if (childCount > 1) {
            View child = parent.getChildAt(childCount - 2);
            bottom = mConfig.mBottomDistance + (child.getBottom() > lastChild.getBottom() ? child.getBottom() : lastChild.getBottom());
        } else {
            bottom = mConfig.mBottomDistance + lastChild.getBottom();
        }

        mLinePaint.setShader(new LinearGradient(parentWidth / 2, top, parentWidth / 2, bottom, Color.parseColor("#b3e5fc")
                , Color.parseColor("#2a36b1"), Shader.TileMode.CLAMP));
        c.drawLine(parentWidth / 2, top, parentWidth / 2, bottom, mLinePaint);

    }

    public void drawVerticalItem(Canvas c, RecyclerView parent) {
        final int parentWidth = parent.getMeasuredWidth();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            int top = child.getTop() + mConfig.mDotPaddingTop;
            int bottom;
            int drawableLeft;
            if (mConfig.mStyle == STYLE_RESOURCE) {
                bottom = top + mDrawable.getIntrinsicHeight();
                drawableLeft = parentWidth / 2 - mDrawable.getIntrinsicWidth() / 2;
                int drawableRight = parentWidth / 2 + mDrawable.getIntrinsicWidth() / 2;

                mDrawable.setBounds(drawableLeft, top, drawableRight, bottom);
                mDrawable.draw(c);
            } else {
                drawableLeft = parentWidth / 2;

                if (mConfig.mDotInItemCenter) {
                    c.drawCircle(drawableLeft, (child.getTop() + child.getBottom()) / 2 + mConfig.mDotPaddingTop, mConfig.mDotRadius, mDotPaint);
                } else {
                    c.drawCircle(drawableLeft, top, mConfig.mDotRadius, mDotPaint);
                }
            }

            if (i == childCount - 1) {
                View lastChild = parent.getChildAt(i - 1);
                if (null == lastChild) lastChild = child;
                if (mConfig.mBottomStyle == STYLE_RESOURCE) {
                    if (lastChild.getBottom() < child.getBottom()) {
                        top = child.getBottom() + mConfig.mBottomDistance;
                        bottom = child.getBottom() + mConfig.mBottomDistance + mBottomDrawable.getIntrinsicHeight();
                    } else {
                        top = lastChild.getBottom() + mConfig.mBottomDistance;
                        bottom = lastChild.getBottom() + mConfig.mBottomDistance + mBottomDrawable.getIntrinsicHeight();
                    }
                } else {
                    if (lastChild.getBottom() < child.getBottom()) {
                        top = child.getBottom() + mConfig.mBottomDistance;
                        bottom = child.getBottom() + (mConfig.mStyle == STYLE_RESOURCE ? mDrawable.getIntrinsicHeight() : mConfig.mDotRadius);
                    } else {
                        top = lastChild.getBottom() + mConfig.mBottomDistance;
                        bottom = lastChild.getBottom() + (mConfig.mStyle == STYLE_RESOURCE ? mDrawable.getIntrinsicHeight() : mConfig.mDotRadius);
                    }
                }
                if (mConfig.mBottomStyle == STYLE_RESOURCE) {
                    int bottomDrawableLeft = parentWidth / 2 - mBottomDrawable.getIntrinsicWidth() / 2 + 8;
                    int bottomDrawableRight = parentWidth / 2 + mBottomDrawable.getIntrinsicWidth() / 2 + 8;
                    mBottomDrawable.setBounds(bottomDrawableLeft, top, bottomDrawableRight, bottom);
                    mBottomDrawable.draw(c);
                } else {
                    if (mConfig.mStyle == STYLE_RESOURCE) {
                        mDrawable.setBounds(drawableLeft, top, drawableRight, bottom);
                        mDrawable.draw(c);
                    } else {
                        c.drawCircle(drawableLeft, top, mConfig.mDotRadius, mDotPaint);
                    }
                    mTextPaint.getTextBounds(mConfig.mEnd, 0, mConfig.mEnd.length(), mTextRect);
                    mTextPaint.setTextSize(mConfig.mTextSize);
                    c.drawText(mConfig.mEnd, parentWidth / 2 - mTextRect.width() / 2, bottom + mConfig.mBottomDistance + mConfig.mDotPaddingText + mConfig.mTextSize, mTextPaint);
                }
            }
        }
    }

    public void drawCenterHorizontalLine(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int parentHeight = parent.getMeasuredHeight();
        int right;

        final int childCount = parent.getChildCount();
        if (childCount == 0) return;
        View lastChild = parent.getChildAt(childCount - 1);
        if (childCount > 1) {
            View child = parent.getChildAt(childCount - 2);
            right = mConfig.mBottomDistance + (child.getRight() > lastChild.getRight() ? child.getRight() : lastChild.getRight());
        } else {
            right = mConfig.mBottomDistance + lastChild.getRight();
        }

        c.drawLine(left, parentHeight / 2, right, parentHeight / 2, mLinePaint);
    }

    public void drawHorizontalItem(Canvas c, RecyclerView parent) {
        final int parentHeight = parent.getMeasuredHeight();
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int left = child.getLeft() + mConfig.mDotPaddingTop;
            int right;
            int drawableLeft;

            if (mConfig.mStyle == STYLE_RESOURCE) {
                right = left + mDrawable.getIntrinsicWidth();
                drawableLeft = parentHeight / 2 - mDrawable.getIntrinsicWidth() / 2;
                int drawableRight = parentHeight / 2 + mDrawable.getIntrinsicWidth() / 2;

                mDrawable.setBounds(drawableLeft, left, drawableRight, right);
                mDrawable.draw(c);
            } else {
                drawableLeft = parentHeight / 2;

                if (mConfig.mDotInItemCenter) {
                    c.drawCircle((child.getLeft() + child.getRight()) / 2 + mConfig.mDotPaddingTop, drawableLeft, mConfig.mDotRadius, mDotPaint);
                } else {
                    c.drawCircle(left, drawableLeft, mConfig.mDotRadius, mDotPaint);
                }
            }

            if (i == childCount - 1) {
                View lastChild = parent.getChildAt(i - 1);
                if (null == lastChild) lastChild = child;
                if (mConfig.mBottomStyle == STYLE_RESOURCE) {
                    if (lastChild.getBottom() < child.getBottom()) {
                        left = child.getLeft() + mConfig.mBottomDistance;
                        right = child.getRight() + mConfig.mBottomDistance + mBottomDrawable.getIntrinsicHeight();
                    } else {
                        left = lastChild.getLeft() + mConfig.mBottomDistance;
                        right = lastChild.getRight() + mConfig.mBottomDistance + mBottomDrawable.getIntrinsicHeight();
                    }
                } else {
                    if (lastChild.getRight() < child.getRight()) {
                        left = child.getRight() + mConfig.mBottomDistance;
                        right = child.getRight() + (mConfig.mStyle == STYLE_RESOURCE ? mDrawable.getIntrinsicWidth() : mConfig.mDotRadius);
                    } else {
                        left = lastChild.getRight() + mConfig.mBottomDistance;
                        right = lastChild.getRight() + (mConfig.mStyle == STYLE_RESOURCE ? mDrawable.getIntrinsicWidth() : mConfig.mDotRadius);
                    }
                }


                if (mConfig.mBottomStyle == STYLE_RESOURCE) {
                    int bottomDrawableTop = parentHeight / 2 - mBottomDrawable.getIntrinsicHeight() / 2;
                    int bottomDrawableBottom = parentHeight / 2 + mBottomDrawable.getIntrinsicHeight() / 2;
                    mBottomDrawable.setBounds(left, bottomDrawableTop, right, bottomDrawableBottom);
                    mBottomDrawable.draw(c);
                } else {
                    if (mConfig.mStyle == STYLE_RESOURCE) {
                        mDrawable.setBounds(left, drawableLeft, drawableRight, right);
                        mDrawable.draw(c);
                    } else {
                        c.drawCircle(left, drawableLeft, mConfig.mDotRadius, mDotPaint);
                    }

                    mTextPaint.getTextBounds(mConfig.mEnd, 0, mConfig.mEnd.length(), mTextRect);
                    mTextPaint.setTextSize(mConfig.mTextSize);
                    c.drawText(mConfig.mEnd, right + mConfig.mBottomDistance + mConfig.mDotPaddingText + mConfig.mTextSize, parentHeight / 2 + mTextRect.height() / 2, mTextPaint);
                }


            }
        }
    }

    static class Config {
        @ItemStyle
        public int mStyle = STYLE_DRAW;
        @ItemStyle
        public int mBottomStyle = STYLE_DRAW;
        @Orientation
        public int mOrientation = VERTICAL;
        public int mTopDistance = 40;
        public int mItemInterval = 20;
        public int mItemPaddingLeft = mItemInterval;//default
        public int mItemPaddingRight = mItemInterval;
        public int mLineWidth = 4;
        public int mLineColor = Color.WHITE;
        public int mDotRes = R.drawable.dot_itemdocoration_ic_dot;
        private int mBottomRes = R.drawable.dot_itemdocoration_ic_ma;
        public int mDotPaddingTop = 20;
        public int mDotRadius = 5;
        public int mDotColor = Color.WHITE;
        public String mEnd = "END";
        public int mTextColor = Color.WHITE;
        public int mTextSize = 18;
        public int mDotPaddingText = 10;
        public int mBottomDistance = 30;
        public boolean mDotInItemCenter = false;
    }

    public static class Builder {
        private Context mContext;
        private Config mConfig;

        public Builder(Context context) {
            this.mContext = context;
            mConfig = new Config();
        }

        public Builder setOrientation(@Orientation int orientation) {
            mConfig.mOrientation = orientation;
            return this;
        }

        public Builder setItemStyle(@ItemStyle int itemStyle) {
            mConfig.mStyle = itemStyle;
            return this;
        }

        public Builder setTopDistance(float distance) {
            mConfig.mTopDistance = UIUtils.dip2px(mContext, distance);
            return this;
        }

        public Builder setItemPaddingLeft(float itemPaddingLeft) {
            mConfig.mItemPaddingLeft = UIUtils.dip2px(mContext, itemPaddingLeft);
            return this;
        }

        public Builder setItemPaddingRight(float itemPaddingRight) {
            mConfig.mItemPaddingRight = UIUtils.dip2px(mContext, itemPaddingRight);
            return this;
        }

        public Builder setItemInterVal(float interval) {
            mConfig.mItemInterval = UIUtils.dip2px(mContext, interval);
            return this;
        }

        public Builder setLineWidth(float lineWidth) {
            mConfig.mLineWidth = UIUtils.dip2px(mContext, lineWidth);
            return this;
        }

        public Builder setLineColor(int lineColor) {
            mConfig.mLineColor = lineColor;
            return this;
        }

        public Builder setDotPaddingTop(int paddingTop) {
            mConfig.mDotPaddingTop = UIUtils.dip2px(mContext, paddingTop);
            return this;
        }

        public Builder setDotRes(int dotRes) {
            mConfig.mDotRes = dotRes;
            return this;
        }

        public Builder setBottomStyle(@ItemStyle int itemStyle) {
            mConfig.mBottomStyle = itemStyle;
            return this;
        }

        public Builder setBottomRes(int bottomRes) {
            mConfig.mBottomRes = bottomRes;
            return this;
        }

        public Builder setDotRadius(int dotRadius) {
            mConfig.mDotRadius = UIUtils.dip2px(mContext, dotRadius);
            return this;
        }

        public Builder setDotColor(int dotColor) {
            mConfig.mDotColor = dotColor;
            return this;
        }

        public Builder setTextColor(int textColor) {
            mConfig.mTextColor = textColor;
            return this;
        }

        public Builder setTextSize(float textSize) {
            mConfig.mTextSize = UIUtils.dip2px(mContext, textSize);
            return this;
        }

        public Builder setDotPaddingText(float dotPaddingText) {
            mConfig.mDotPaddingText = UIUtils.dip2px(mContext, dotPaddingText);
            return this;
        }

        public Builder setDotInItemOrientationCenter(boolean tag) {
            mConfig.mDotInItemCenter = tag;
            return this;
        }

        public Builder setBottomDistance(float bottomDistance) {
            mConfig.mBottomDistance = UIUtils.dip2px(mContext, bottomDistance);
            return this;
        }

        public Builder setEndText(String end) {
            mConfig.mEnd = end;
            return this;
        }

        public DotItemDecoration create() {
            return new DotItemDecoration(mContext, mConfig);
        }
    }
}