package com.orient.me.widget.rv.itemdocration.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.orient.me.data.ITimeItem;
import com.orient.me.utils.UIUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class TimeLine extends RecyclerView.ItemDecoration {

    // 标题
    public static final int FLAG_TITLE_POS_NONE = 0X0001;
    public static final int FLAG_TITLE_TYPE_TOP = 0x0002;
    public static final int FLAG_TITLE_TYPE_LEFT = 0x0004;
    public static final int FLAG_TITLE_DRAW_BG = 0x0008;

    public static final int FLAG_SAME_TITLE_HIDE = 0x0100;

    // 时间线
    public static final int FLAG_LINE_DIVIDE = 0x0010;
    public static final int FLAG_LINE_CONSISTENT = 0x0020;
    public static final int FLAG_LINE_BEGIN_TO_END= 0x0040;
    // 时间点
    public static final int FLAG_DOT_RES = 0x1000;
    public static final int FLAG_DOT_DRAW = 0x2000;


    protected Context mContext;
    protected List<? extends ITimeItem> timeItems;
    // 标题放置的类型
    protected int mFlag;
    // 上次的标题

    // 标题分两种，
    // 1. 上方
    // 2. 左侧
    protected int mTitleColor;
    protected int mTopOffset;
    protected int mLeftOffset;
    protected Paint mTextPaint;
    protected int mTitleFontSize;
    protected int mBgColor;
    protected Paint mBgPaint;

    // 线
    protected int mLineColor;
    protected Paint mLinePaint;
    protected int mLineOffset;
    protected int mLineWidth;

    // 点
    protected Paint mDotPaint;

    public TimeLine(Config config) {
        mContext = config.context;
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
        this.mLineWidth = UIUtils.dip2px(config.lineWidth);

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


    /**
     * 更新部分数据
     */
    public void addItems(List items){
        this.timeItems.addAll(items);
    }

    /**
     * 更新全部数据
     * @param items 数据
     */
    public void replace(List<? extends ITimeItem> items){
        this.timeItems = items;
    }

    /**
     * 清除数据
     */
    public void remove(){
        this.timeItems.clear();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        // 兼容4.0硬件加速无效
        parent.setLayerType(View.LAYER_TYPE_SOFTWARE,mDotPaint);
        
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
    protected abstract void drawTitle(Canvas canvas, RecyclerView parent);

    /**
     * 绘制直线
     * @param c Canvas
     * @param parent RecyclerView
     */
    protected abstract void drawVerticalLine(Canvas c, RecyclerView parent);

    /**
     * 绘制点
     * @param c Canvas
     * @param parent RecyclerView
     */
    protected abstract void drawPoint(Canvas c, RecyclerView parent);


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
        int lineWidth = 1;
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
            return setLine(type, lineOffset, lineColor,1);
        }

        /**
         * @param type       type 时间线的类型
         * @param lineOffset 时间轴左边偏移的大小，右边也会偏移同样的大小
         */
        public Builder setLine(int type, int lineOffset, int lineColor,int lineWidth) {
            this.mConfig.flag |= type;
            this.mConfig.lineOffset = lineOffset;
            this.mConfig.lineColor = lineColor;
            this.mConfig.lineWidth = lineWidth;
            return this;
        }

        /**
         * 设置原点
         *
         * @param type   点的类型
         */
        public Builder setDot(int type) {
            this.mConfig.flag |= type;
            return this;
        }

        /**
         * 构建
         *
         * @param cls 构建的类
         * @return T
         */
        public TimeLine build(Class<? extends TimeLine> cls) {
            TimeLine t = null;
            try {
                Constructor<? extends TimeLine> con = cls.getConstructor(Config.class);
                t = con.newInstance(mConfig);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return t;
        }

    }


}
