package com.orient.ui.widget.timeline.stl;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import com.orient.me.data.ITimeItem;
import com.orient.me.utils.UIUtils;
import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;

public class StepSTL extends SingleTimeLineDecoration {

    private Paint mRectPaint;

    public StepSTL(SingleTimeLineDecoration.Config config) {
        super(config);

        mRectPaint = new Paint();
        mDotPaint.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.SOLID));
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
    protected void onDrawDotItem(Canvas canvas, int cx, int cy, int radius, int pos) {
        ITimeItem item = timeItems.get(pos);
        mDotPaint.setColor(item.getColor());
        canvas.drawCircle(cx,cy,UIUtils.dip2px(6),mDotPaint);
    }
}
