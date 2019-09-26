package com.orient.ui.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.orient.me.data.ITimeItem;
import com.orient.me.widget.rv.itemdocration.timeline.DoubleTimeLineDecoration;
import com.orient.ui.utils.UIUtils;

public class DoubleSideTimeLine extends DoubleTimeLineDecoration {

    public DoubleSideTimeLine(Config config) {
        super(config);
    }

    @Override
    protected void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int centerX, int pos, boolean isLeft) {
        ITimeItem item = timeItems.get(pos);

        int height = bottom - top;
        String title = item.getTitle();
        if (TextUtils.isEmpty(title))
            return;
        Rect mRect = new Rect();
        //mTextPaint.setColor(item.getColor());
        mTextPaint.getTextBounds(title, 0, title.length(), mRect);

        int x, y;
        if (isLeft) {
            x = centerX + UIUtils.dip2px(30);
        } else {
            x = centerX - UIUtils.dip2px(30) - mRect.width();
        }
        y = bottom - (height - mRect.height()) / 2;
        canvas.drawText(title, x, y, mTextPaint);
    }

    @Override
    protected void onDrawPointItem(Canvas canvas, int cx, int cy, int radius, int pos) {
        ITimeItem item = timeItems.get(pos);

        int res = item.getResource();
        Drawable drawable = ContextCompat.getDrawable(mContext,res);
        if(drawable != null){
            int height = drawable.getIntrinsicHeight();
            int width = drawable.getIntrinsicWidth();
            int left = cx - width/2;
            int top = cy- height/2;
            int right = cx + width/2;
            int bottom = cy + height/2;
            drawable.setBounds(left,top,right,bottom);
            drawable.draw(canvas);
            mDotPaint.setStyle(Paint.Style.STROKE);
            mDotPaint.setColor(Color.parseColor("#ffffff"));
            mDotPaint.setStrokeWidth(UIUtils.dip2px(2));
            canvas.drawCircle(cx,cy,width/2-UIUtils.dip2px(3),mDotPaint);
        }

        /*mDotPaint.setColor(item.getColor());
        mDotPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(cx, cy, UIUtils.dip2px(6), mDotPaint);*/
    }
}
