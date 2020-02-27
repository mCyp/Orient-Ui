package com.orient.me.widget.sw;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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
    enum SwitchShape {
        RECT,
        OVAl
    }

    /**
     * 类型
     * 1. 文本
     * 2. 图标
     */
    enum SwitchType {
        TEXT,
        ICON
    }

    // 默认大小
    public static final int DEFAULT_WIDTH = UIUtils.dip2px(112);
    public static final int DEFAULT_HEIGHT = UIUtils.dip2px(56);
    public static final int CORNER_RADIUS = UIUtils.dip2px(4);


    // 类型
    private SwitchType mType;
    // 图标资源
    private int[] mIconRes;
    // 文本内容
    private String[] mItems;
    // 内容数量
    private int mItemCount;

    // 内容的横向坐标
    private int[] mItemCoordinate;
    private int top, bottom;

    private SwitchShape mShape;

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
    private int mAveWidth;

    // 文本大小
    private int mTextSize;
    // 图标大小
    private int mIconSize;

    private Paint mBgColorPaint;
    private Paint mBgTextPaint;
    private Paint mThumbColorPaint;
    private Paint mThumbTextPaint;

    // 当前滑块的状态
    private ThumbState mThumbState;
    private int mThumbMargin;

    private ValueAnimator mValueAnimator;

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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiSwitch);
        mBgColor = typedArray.getColor(R.styleable.MultiSwitch_msBackgroundColor, getResources().getColor(R.color.textSecond));
        mBgTextColor = typedArray.getColor(R.styleable.MultiSwitch_msNormalTextColor, getResources().getColor(R.color.textPrimary));
        mThumbColor = typedArray.getColor(R.styleable.MultiSwitch_msThumbColor, getResources().getColor(R.color.colorPrimary));
        mThumbTextColor = typedArray.getColor(R.styleable.MultiSwitch_msThumbTextColor, getResources().getColor(R.color.white));
        // TODO 检验得出来的文本大小是否转化过
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.MultiSwitch_msTextSize, UIUtils.sp2px(context, 20));
        mIconSize = typedArray.getDimensionPixelSize(R.styleable.MultiSwitch_msIconSize, UIUtils.dip2px(context, 24));
        mType = SwitchType.values()[typedArray.getInt(R.styleable.MultiSwitch_msType, SwitchType.TEXT.ordinal())];
        mShape = SwitchShape.values()[typedArray.getInt(R.styleable.MultiSwitch_msShape, SwitchShape.RECT.ordinal())];
        mThumbMargin = typedArray.getDimensionPixelOffset(R.styleable.MultiSwitch_msThumbMargin, UIUtils.dip2px(2));
        // TODO 设置默认位置

        // 初始化画笔
        mBgColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBgColorPaint.setColor(mBgColor);
        mBgTextPaint = new Paint();
        mBgTextPaint.setTextSize(mTextSize);
        mBgTextPaint.setColor(mBgTextColor);
        mThumbColorPaint = new Paint();
        mThumbColorPaint.setColor(mThumbColor);
        mThumbTextPaint = new Paint();
        mThumbTextPaint.setTextSize(mTextSize);
        mThumbTextPaint.setColor(mThumbTextColor);

        // 初始化滑块状态
        mThumbState = new ThumbState(0, 0);
    }

    /**
     * 设置选择项内容
     */
    public void setItemsArray(String[] items) {
        if (items == null || items.length <= 1)
            throw new UnsupportedOperationException("items'length can't be null or smaller than 1");

        if (mType == SwitchType.ICON)
            return;

        this.mItems = items;
        this.mItemCount = items.length;
        this.mItemCoordinate = new int[items.length + 1];
    }

    /**
     * 设置选择项图标
     */
    public void setIconArray(int[] items) {
        if (items == null || items.length == 0)
            throw new UnsupportedOperationException("items'length can't be null or smaller than 1");

        if (mType == SwitchType.TEXT)
            return;

        this.mIconRes = items;
        this.mItemCount = items.length;
        this.mItemCoordinate = new int[items.length + 1];
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

        // 初始化信息
        int w, h;
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        if (w != 0) {
            mWidth = w;
        }
        if (h != 0) {
            mHeight = h;
        }
        if (mWidth != 0 && mItemCount != 0) {
            int aveWidth = mWidth / mItemCount;
            int reminder = mWidth % mItemCount;
            int startLeft = 0;
            mItemCoordinate[0] = startLeft;
            for (int i = 1; i < mItemCoordinate.length; i++) {
                startLeft += aveWidth;
                if (reminder > 0) {
                    startLeft++;
                    reminder--;
                }
                mItemCoordinate[i] = startLeft;
            }
            mAveWidth = aveWidth;
        }
        if (mHeight != 0) {
            top = 0;
            bottom = top + mHeight;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mItemCount == 0)
            return;

        int l = mItemCoordinate[0];
        int r = mItemCoordinate[mItemCoordinate.length - 1];
        int t = top;
        int b = bottom;

        // 1. 绘制背景
        mBgColorPaint.setStyle(Paint.Style.FILL);
        int radius;
        if (mShape == SwitchShape.RECT) {
            radius = CORNER_RADIUS;
            drawRoundRect(canvas, l, t, r, b, radius, mBgColorPaint);
        } else {
            radius = mHeight / 2;
            drawRoundRect(canvas, l, t, r, b, radius, mBgColorPaint);
        }

        // 2. 绘制内容
        if (mType == SwitchType.TEXT) {
            // 绘制文字
            for (int i = 0; i < mItems.length; i++) {
                drawText(canvas, mItems[i], mItemCoordinate[i], top, mItemCoordinate[i + 1], bottom, mBgTextPaint);
            }
        } else {
            // 绘制图标
            for (int i = 0; i < mIconRes.length; i++) {
                drawIcon(canvas, mIconRes[i], mItemCoordinate[i], top, mItemCoordinate[i + 1], bottom, mBgTextPaint);
            }
        }

        // 3. 绘制滑块和滑块上的文字或者图标
        drawThumb(canvas);
    }

    /**
     * 绘制带圆角的矩形
     */
    private void drawRoundRect(Canvas canvas, int left, int top, int right, int bottom, int radius, Paint paint) {
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, radius, radius, paint);
    }

    /**
     * 绘制文本
     */
    private void drawText(Canvas canvas, String text, int left, int top, int right, int bottom, Paint paint) {
        int w = right - left;
        int h = bottom - top;
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int x = w / 2 - bounds.width() / 2;
        int y = (int) ((h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
        canvas.drawText(text, x, y, paint);
    }

    /**
     * 绘制图标
     */
    private void drawIcon(Canvas canvas, int res, int left, int top, int right, int bottom, Paint paint) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        drawable.setTint(paint.getColor());
        int cx = left + (right - left) / 2;
        int cy = top + (bottom - top) / 2;
        int l = cx - mIconSize / 2;
        int t = cy - mIconSize / 2;
        int r = cx + mIconSize / 2;
        int b = cy + mIconSize / 2;
        drawable.setBounds(l, t, r, b);
        drawable.draw(canvas);
    }

    /**
     * 绘制Switch滑动块
     */
    private void drawThumb(Canvas canvas) {
        int left = mItemCoordinate[mThumbState.pos] + mThumbState.offset;
        int right = mItemCoordinate[mThumbState.pos + 1] + mThumbState.offset;
        canvas.save();
        Rect rect = new Rect(left + mThumbMargin, top + mThumbMargin, right - mThumbMargin, bottom - mThumbMargin);
        canvas.clipRect(rect);

        // 绘制滑块
        if (mShape == SwitchShape.RECT) {
            drawRoundRect(canvas, left + mThumbMargin, top + mThumbMargin
                    , right - mThumbMargin, bottom - mThumbMargin, CORNER_RADIUS, mThumbColorPaint);
        } else {
            drawRoundRect(canvas, left + mThumbMargin, top + mThumbMargin
                    , right - mThumbMargin, bottom - mThumbMargin, (bottom - top) / 2 - mThumbMargin, mThumbColorPaint);
        }

        int first, second;
        if (mThumbState.offset > 0) {
            first = mThumbState.pos;
            second = first + 1;
        } else if (mThumbState.offset == 0) {
            first = mThumbState.pos;
            second = -1;
        } else {
            first = mThumbState.pos - 1;
            second = first + 1;
        }

        // 绘制文字
        if (mType == SwitchType.TEXT) {
            drawText(canvas, mItems[first], mItemCoordinate[first], top, mItemCoordinate[first + 1], bottom, mThumbColorPaint);
            if (second != -1) {
                drawText(canvas, mItems[second], mItemCoordinate[second], top, mItemCoordinate[second + 1], bottom, mThumbColorPaint);
            }
        } else {
            drawIcon(canvas, mIconRes[first], mItemCoordinate[first], top, mItemCoordinate[first + 1], bottom, mThumbColorPaint);
            if (second != -1) {
                drawIcon(canvas, mIconRes[second], mItemCoordinate[second], top, mItemCoordinate[second + 1], bottom, mThumbColorPaint);
            }
        }

        canvas.restore();

    }

    private float curX, curY;
    private float lastX, lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        curX = event.getX();
        curY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                checkDrag();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mThumbState.canDrag)
                    break;
                float deltaX = curX - lastX;
                if (deltaX > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    handleMove(deltaX);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;

        }
        lastX = curX;
        lastY = curY;
        return super.onTouchEvent(event);
    }

    /**
     * 检测移动状态
     */
    private void checkDrag() {
        int l = mItemCoordinate[mThumbState.pos] + mThumbState.offset + mThumbMargin;
        int r = mItemCoordinate[mThumbState.pos + 1] + mThumbState.offset - mThumbMargin;
        int t = top + mThumbMargin;
        int b = bottom - mThumbMargin;
        if (curX >= l && curX <= r && curY >= t && curY <= b) {
            mThumbState.canDrag = true;
        }
    }

    /**
     * 处理移动
     *
     * @param dx 滑动距离
     */
    private void handleMove(float dx) {
        mThumbState.offset += dx;
        if (Math.abs(mThumbState.offset) >= mAveWidth) {
            int pos = mThumbState.pos;
            if (mThumbState.offset > 0) {
                while (mItemCoordinate[pos] + mThumbState.offset >= mItemCoordinate[pos + 1]) {
                    if (pos == mItemCount - 1
                            || mItemCoordinate[pos] + mThumbState.offset >= mItemCoordinate[mItemCount - 1]) {
                        mThumbState.pos = mItemCount - 1;
                        mThumbState.offset = 0;
                        invalidate();
                        return;
                    }

                    pos++;
                    mThumbState.offset -= (mItemCoordinate[pos] - mItemCoordinate[pos - 1]);
                }
            } else {
                while (pos != 0 && mItemCoordinate[pos] + mThumbState.offset <= mItemCoordinate[pos - 1]) {
                    if (pos == 1
                            || mItemCoordinate[pos] + mThumbState.offset < mItemCoordinate[0]) {
                        mThumbState.pos = 0;
                        mThumbState.offset = 0;
                        invalidate();
                        return;
                    }

                    pos--;
                    mThumbState.offset += (mItemCoordinate[pos + 1] - mItemCoordinate[pos - 1]);
                }
            }
            mThumbState.pos = pos;
        }
        invalidate();
    }

    private void animate(float x){
        int startX = mThumbState.pos+mThumbState.offset;
        mValueAnimator = ValueAnimator.ofFloat(startX,x);
        mValueAnimator.setDuration(200);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cx = (float) animation.getAnimatedValue();
                //int x = (cx - (mItemCoordinate[mThumbState.pos]+mThumbState.offset))
            }
        });
    }


    /**
     * 滑块的状态
     */
    enum ThumbStaus {
        STATUS_STATIC,
        STATUS_DRAG,
        STATUS_ANIMATION
    }

    /**
     * 滑块的位置参数
     */
    class ThumbState {
        int pos;
        int offset;
        boolean canDrag;
        ThumbStaus state = ThumbStaus.STATUS_STATIC;

        public ThumbState(int pos, int offset) {
            this.pos = pos;
            this.offset = offset;
        }
    }


}
