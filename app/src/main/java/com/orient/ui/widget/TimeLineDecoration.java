package com.orient.ui.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import com.orient.me.data.ITimeItem;
import com.orient.me.widget.rv.itemdocration.timeline.AbstractTimeLineDecoration;
import com.orient.ui.utils.UIUtils;

import java.util.List;

public class TimeLineDecoration extends AbstractTimeLineDecoration {

    private Paint mRectPaint;
    //private String[] COLORS = new String[]{"#F57F17","#0D47A1"};


    public TimeLineDecoration(AbstractTimeLineDecoration.Config config) {
        super(config);

        mRectPaint = new Paint();

    }

    @Override
    protected void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int pos) {
        ITimeItem item = timeItems.get(pos);

        int rectWidth = UIUtils.dip2px(100);
        int height = bottom - top;
        left += mLineOffset;
        mRectPaint.setColor(item.getColor());
        canvas.drawRect(left,top,left+rectWidth,bottom,mRectPaint);
        canvas.drawArc(new RectF(left+rectWidth-height/2,top,left+rectWidth+height/2
                ,bottom),270,180,true,mRectPaint);

        String title = item.getTitle();
        if(TextUtils.isEmpty(title))
            return;
        Rect mRect = new Rect();

        mTextPaint.getTextBounds(title,0,title.length(),mRect);
        int x = left + (rectWidth - mRect.width())/2;
        //int x = left + UIUtils.dip2px(20);
        int y = bottom - (height - mRect.height())/2;
        canvas.drawText(title,x,y,mTextPaint);
    }

    @Override
    protected void onDrawPointItem(Canvas canvas,  int cx, int cy, int radius, int pos) {
        ITimeItem item = timeItems.get(pos);
        mDotPaint.setColor(item.getColor());
        mDotPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(cx,cy,UIUtils.dip2px(6),mDotPaint);
    }
}
