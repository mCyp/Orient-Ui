package com.orient.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.orient.me.data.ITimeItem;
import com.orient.me.widget.rv.itemdocration.timeline.AbstractTimeLineDecoration;
import com.orient.ui.utils.UIUtils;

import java.util.List;

public class TimeLineDecoration extends AbstractTimeLineDecoration {


    public TimeLineDecoration(Context context) {
        super(context);
    }

    public TimeLineDecoration(Context context, List<? extends ITimeItem> timeItems) {
        super(context, timeItems);
    }

    @Override
    protected void onDrawTitleItem(Canvas canvas,Paint textPaint, Paint bgPaint, int left, int top, int right, int bottom, int pos) {
        ITimeItem item = timeItems.get(pos);
        String title = item.getTitle();
        if(TextUtils.isEmpty(title))
            return;
        Rect mRect = new Rect();

        textPaint.getTextBounds(title,0,title.length(),mRect);
        int x = left + UIUtils.dip2px(20);
        int height = bottom - top;
        int y = bottom - (height - mRect.height())/2;
        canvas.drawText(title,x,y,textPaint);
    }

    @Override
    protected void onDrawPointItem(Canvas canvas, Paint dotPaint, int cx, int cy, int radius, int pos) {
        canvas.drawCircle(cx,cy,UIUtils.dip2px(6),dotPaint);
    }
}
