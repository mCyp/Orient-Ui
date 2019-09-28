package com.orient.ui.widget.timeline.dtl;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.orient.me.data.ITimeItem;
import com.orient.me.utils.UIUtils;
import com.orient.me.widget.rv.itemdocration.timeline.DoubleTimeLineDecoration;
import com.orient.ui.R;

import java.util.Calendar;
import java.util.Date;

public class DateInfoDTL extends DoubleTimeLineDecoration {

    private static final String[] MONTHS = new String[]{
            "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"
    };

    private int r;
    private Paint monTextPaint;
    private Paint dayTextPaint;
    private int space;

    public DateInfoDTL(Config config) {
        super(config);

        r = UIUtils.dip2px(24);
        monTextPaint = new Paint();
        monTextPaint.setTextSize(UIUtils.sp2px(mContext,12));
        monTextPaint.setColor(Color.parseColor("#F5F5F5"));

        dayTextPaint = new Paint();
        dayTextPaint.setTextSize(UIUtils.sp2px(mContext,18));
        dayTextPaint.setColor(Color.parseColor("#ffffff"));

        space = UIUtils.dip2px(6);

        mDotPaint.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.SOLID));;

    }

    @Override
    protected void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int centerX, int pos, boolean isLeft) {
        // 不需要做什么
    }

    @Override
    protected void onDrawDotItem(Canvas canvas, int cx, int cy, int radius, int pos) {
        super.onDrawDotItem(canvas, cx, cy, radius, pos);

        DateInfo timeItem = (DateInfo) timeItems.get(pos);
        Date date = timeItem.getDate();
        mDotPaint.setColor(timeItem.getColor());
        canvas.drawCircle(cx,cy,r,mDotPaint);

        if(date != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String mon = MONTHS[calendar.get(Calendar.MONTH)];
            int n = calendar.get(Calendar.DAY_OF_MONTH);
            String day = n<10?"0"+n:Integer.toString(n);

            Rect monRect = new Rect();
            monTextPaint.getTextBounds(mon,0,mon.length(),monRect);
            Rect dayRect = new Rect();
            dayTextPaint.getTextBounds(day,0,day.length(),dayRect);

            int monWidth = monRect.width();
            int monHeight = monRect.height();
            int dayWidth = dayRect.width();
            int dayHeight = dayRect.height();

            int beginY = cy + r - (r * 2 - monHeight - dayHeight)/2;
            canvas.drawText(day,cx-dayWidth/2,beginY,dayTextPaint);
            beginY -= dayHeight + space;
            canvas.drawText(mon,cx-monWidth/2,beginY,monTextPaint);
        }
    }


}
