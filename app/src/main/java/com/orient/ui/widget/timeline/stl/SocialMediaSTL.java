package com.orient.ui.widget.timeline.stl;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.orient.me.data.ITimeItem;
import com.orient.me.utils.UIUtils;
import com.orient.me.widget.rv.itemdocration.timeline.DoubleTimeLineDecoration;
import com.orient.me.widget.rv.itemdocration.timeline.SingleTimeLineDecoration;
import com.orient.ui.widget.timeline.dtl.DateInfo;

import java.util.Calendar;
import java.util.Date;

public class SocialMediaSTL extends SingleTimeLineDecoration {

    private static final String[] MONTHS = new String[]{
            "/1月", "/2月", "/2月", "/4月", "/5月", "/6月", "/7月", "/8月", "/9月", "/10月", "/11月", "/12月"
    };

    private int r;
    private Paint monTextPaint;
    private Paint dayTextPaint;
    private int space;

    public SocialMediaSTL(Config config) {
        super(config);

        r = UIUtils.dip2px(24);
        monTextPaint = new Paint();
        monTextPaint.setTextSize(UIUtils.sp2px(mContext, 10));
        monTextPaint.setColor(Color.parseColor("#F5F5F5"));

        dayTextPaint = new Paint();
        dayTextPaint.setTextSize(UIUtils.sp2px(mContext, 18));
        dayTextPaint.setColor(Color.parseColor("#ffffff"));

        space = UIUtils.dip2px(2);

        mDotPaint.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.SOLID));

        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    @Override
    protected void onDrawTitleItem(Canvas canvas, int left, int top, int right, int bottom, int pos) {
        ITimeItem item = timeItems.get(pos);

        int rectWidth = UIUtils.dip2px(20);
        int mid = (bottom + top) / 2;

        String title = item.getTitle();
        if (TextUtils.isEmpty(title))
            return;
        Rect mRect = new Rect();

        mTextPaint.getTextBounds(title, 0, title.length(), mRect);
        int x = left + rectWidth;
        //int x = left + UIUtils.dip2px(20);
        int y = mid + mRect.height() / 2;
        canvas.drawText(title, x, y, mTextPaint);
    }


    @Override
    protected void onDrawDotItem(Canvas canvas, int cx, int cy, int radius, int pos) {
        super.onDrawDotItem(canvas, cx, cy, radius, pos);

        DateInfo timeItem = (DateInfo) timeItems.get(pos);
        Date date = timeItem.getDate();
        mDotPaint.setColor(timeItem.getColor());
        canvas.drawRoundRect(cx - r, cy - r, cx + r, cy + r
                , UIUtils.dip2px(2),UIUtils.dip2px(2),mDotPaint);

        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String mon = MONTHS[calendar.get(Calendar.MONTH)];
            int n = calendar.get(Calendar.DAY_OF_MONTH);
            String day = n < 10 ? "0" + n : Integer.toString(n);

            Rect monRect = new Rect();
            monTextPaint.getTextBounds(mon, 0, mon.length(), monRect);
            Rect dayRect = new Rect();
            dayTextPaint.getTextBounds(day, 0, day.length(), dayRect);

            int monWidth = monRect.width();
            int dayWidth = dayRect.width();
            int dayHeight = dayRect.height();

            int beginY = cy + r - (r * 2 -dayHeight) / 2;
            int beginX = cx - r +(r *2 - monWidth -space -  dayWidth)/2;
            canvas.drawText(day, beginX, beginY, dayTextPaint);
            beginX += space+dayWidth;
            canvas.drawText(mon, beginX, beginY, monTextPaint);
        }
    }


}
