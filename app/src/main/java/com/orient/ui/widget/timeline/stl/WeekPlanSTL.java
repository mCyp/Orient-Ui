package com.orient.ui.widget.timeline.stl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.orient.me.data.ITimeItem;
import com.orient.me.utils.UIUtils;
import com.orient.me.widget.rv.itemdocration.timeline.DoubleTimeLineDecoration;
import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;

public class WeekPlanSTL extends SingleTimeLineDecoration {

    public WeekPlanSTL(Config config) {
        super(config);
    }

    @Override
    protected void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int pos) {
        ITimeItem item = timeItems.get(pos);

        String title = item.getTitle();
        if (TextUtils.isEmpty(title))
            return;
        Rect mRect = new Rect();
        mTextPaint.setColor(item.getColor());
        mTextPaint.getTextBounds(title, 0, title.length(), mRect);

        int centerX = (left + right) / 2;
        int centerY = (bottom + top) / 2;
        int x, y;
        x = centerX - mRect.width() / 2;
        y = centerY + mRect.height() / 2;
        canvas.drawText(title, x, y, mTextPaint);
    }

    @Override
    protected void onDrawDotResItem(Canvas canvas, int cx, int cy, int radius, Drawable drawable, int pos) {
        super.onDrawDotResItem(canvas, cx, cy, radius, drawable, pos);

        if (drawable != null) {
            int height = drawable.getIntrinsicHeight();
            int width = drawable.getIntrinsicWidth();
            int left = cx - width / 2;
            int top = cy - height / 2;
            int right = cx + width / 2;
            int bottom = cy + height / 2;
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(canvas);
            mDotPaint.setStyle(Paint.Style.STROKE);
            mDotPaint.setColor(Color.parseColor("#ffffff"));
            mDotPaint.setStrokeWidth(UIUtils.dip2px(2));
            canvas.drawCircle(cx, cy, width / 2 - UIUtils.dip2px(3), mDotPaint);
        }
    }

}
