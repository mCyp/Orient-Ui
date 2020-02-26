package com.orient.me.widget.sw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.orient.me.R;
import com.orient.me.utils.UIUtils;

/**
 * 能够存放多个Item的Switch
 * <p>
 * 支持：
 * 1. 多个Item
 * 2. 支持文字和Icon
 * 3. 过度动画
 * 4. 支持设置大小
 * 5. 支持滑动和点击
 * 6. 支持椭圆或者圆角矩形
 */
public class MultiSwitch extends View {

    /**
     * 形状
     * 1. 圆角矩形
     * 2. 两端是圆形
     */
    public static final int SHAPE_RECT = 1;
    public static final int SHAPE_OVAL = 2;

    // 默认大小
    public static final int DEFAULT_WIDTH = UIUtils.dip2px(112);
    public static final int DEFAULT_HEIGHT = UIUtils.dip2px(56);

    /**
     * 类型
     * 1. 文本
     * 2. 图标
     */
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_ICON = 2;


    // 类型
    private int mType;
    // 图标资源
    private int[] mIconRes;
    // 文本内容
    private String[] mItems;
    // 内容数量
    private int mItemCount;

    // 背景色
    private int mBgColor;
    // 背景文本 Or Icon 颜色
    private int mBgTextColor;
    // 滑块颜色
    private int mThumbColor;
    // 滑块上文本 or Icon 颜色
    private int mThumbTextColor;

    // 一个Item所占的长和宽
    private int mWidth;
    private int mHeight;

    // 文本大小
    private int mTextSize;
    // 图标大小
    private int iconSize;

    private Paint mBgColorPaint;
    private Paint mBgTextPaint;
    private Paint mThumbColorPaint;
    private Paint mThumbTextPaint;

    public MultiSwitch(Context context) {
        this(context, null);
    }

    public MultiSwitch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 1. 获取必要的属性
     * 2. 初始化一些必要的参数，比如说画笔
     */
    private void init(Context context, AttributeSet attrs) {
        // TODO 定下需要获取的格式
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiSwitch);
        mBgColor = typedArray.getColor(R.styleable.MultiSwitch_msBackgroundColor,getResources().getColor(R.color.textSecond));
        mBgTextColor = typedArray.getColor(R.styleable.MultiSwitch_msNormalTextColor,getResources().getColor(R.color.textPrimary));
        mThumbColor = typedArray.getColor(R.styleable.MultiSwitch_msThumbColor,getResources().getColor(R.color.colorPrimary));
        mThumbTextColor = typedArray.getColor(R.styleable.MultiSwitch_msThumbTextColor,getResources().getColor(R.color.white));
        // TODO 检验得出来的文本大小是否转化过
        mTextSize = typedArray.getColor(R.styleable.MultiSwitch_msTextSize,UIUtils.sp2px(context,20));


        // 初始化画笔
        mBgColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mBgColorPaint.setColor(mBgColor);
        mBgTextPaint = new Paint();

    }


    public void setItemsArray(String[] items){
        if(items == null || items.length <= 1)
            throw new UnsupportedOperationException("items'length can't be null or smaller than 1");

        this.mItems = items;
        this.mItemCount = items.length;
        this.mType = TYPE_TEXT;
    }

    public void setIconArray(int[] items){
        if(items == null || items.length == 0)
            throw new UnsupportedOperationException("items'length can't be null or smaller than 1");

        this.mIconRes = items;
        this.mItemCount = items.length;
        this.mType = TYPE_ICON;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH * mItemCount, MeasureSpec.EXACTLY);
        }
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景

    }



    /**
     * 绘制文本
     */
    private void drawTextLeft(Canvas canvas,Paint paint,int start,int end){
        canvas.save();
        Rect r = new Rect(50,0,150,350);
        canvas.clipRect(r);
        mThumbColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mThumbColorPaint.setColor(Color.GRAY);
        canvas.drawRect(r,mThumbColorPaint);
        canvas.drawText("wangjie",100,100,paint);
        canvas.restore();
    }

    private void drawTextRight(Canvas canvas,Paint paint,int start,int end){
        canvas.save();
        Rect r = new Rect(150,0,1000,350);
        canvas.clipRect(r);
        canvas.drawText("wangjie",100,100,paint);
        canvas.restore();
    }
}
