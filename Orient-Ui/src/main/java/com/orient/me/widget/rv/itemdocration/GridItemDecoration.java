package com.orient.me.widget.rv.itemdocration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.orient.me.data.IGridItem;
import com.orient.me.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 适用于GridLayoutManager的分割线
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    // 记录上次偏移位置 防止一行多个数据的时候视图偏移
    private List<Integer> offsetPositions = new ArrayList<>();
    // 显示数据
    private List<? extends IGridItem> gridItems;
    // 画笔
    private Paint mTitlePaint;
    // 存放文字
    private Rect mRect;
    // 颜色
    private int mTitleBgColor;
    private int mTitleColor;
    private int mTitleHeight;
    private int mTitleFontSize;
    private boolean isDrawTitleBg = false;

    private Context mContext;

    // 总的SpanSize
    private int totalSpanSize;
    private int mCurrentSpanSize;

    private GridItemDecoration(Config config, Context context) {
        this.mContext = context;
        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setDither(true);
        mTitleFontSize = UIUtils.sp2px(mContext, config.titleFontSize);
        mRect = new Rect();
        init(config);
    }

    private void init(Config config) {
        this.gridItems = new ArrayList<>(config.gridItems);
        this.totalSpanSize = config.totalSpanSize;
        this.mTitleBgColor = config.titleBgColor;
        this.mTitleColor = config.titleTextColor;
        this.mTitleHeight = UIUtils.dip2px(mContext, config.titleHeight);
        this.isDrawTitleBg = config.isDrawTitleBg;
    }

    /**
     * 更新部分数据
     */
    public void addItems(List items){
        this.gridItems.addAll(items);
    }

    /**
     * 更新全部数据
     * @param items 数据
     */
    public void replace(List<? extends IGridItem> items){
        replace(items,0);
    }

    /**
     * 对于当前位置前的数据需要更新，不然，对于文字下方的多列的首行视图会发生偏移
     * @param items 更换的数据
     * @param pos 当前可见视图在数据中的位置
     */
    public void replace(List<? extends IGridItem> items,int pos){
        this.offsetPositions.clear();
        if(items == null || items.size() == 0){
            remove();
            return;
        }
        if(pos >= items.size())
            throw new UnsupportedOperationException();

        this.gridItems = new ArrayList<>(items);
        int currentSpanSize = gridItems.get(0).getSpanSize();
        for(int i= 1;i<pos;i++){
            IGridItem item = items.get(i);
            IGridItem lastItem = items.get(i-1);
            if(!item.getTag().equals(lastItem.getTag())){
                currentSpanSize = item.getSpanSize();
                offsetPositions.add(i);
                continue;
            }

            currentSpanSize += item.getSpanSize();
            if(currentSpanSize <= totalSpanSize){
                offsetPositions.add(i);
            }
        }
    }



    public void remove(){
        this.gridItems.clear();
        this.offsetPositions.clear();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final int paddingLeft = parent.getPaddingLeft();
        final int paddingRight = parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int pos = params.getViewLayoutPosition();
            IGridItem item = gridItems.get(pos);
            if (item == null || !item.isShow())
                continue;

            if (i == 0) {
                drawTitle(c, paddingLeft, paddingRight, child
                        , (RecyclerView.LayoutParams) child.getLayoutParams(), pos,parent);
            } else {
                IGridItem lastItem = gridItems.get(pos - 1);
                if (lastItem != null && !item.getTag().equals(lastItem.getTag())) {
                    drawTitle(c, paddingLeft, paddingRight, child,
                            (RecyclerView.LayoutParams) child.getLayoutParams(), pos,parent);
                }
            }
        }

    }

    /**
     * 绘制标题
     *
     * @param canvas 画布
     * @param pl     左边距
     * @param pr     右边距
     * @param child  子View
     * @param params RecyclerView.LayoutParams
     * @param pos    位置
     */
    private void drawTitle(Canvas canvas, int pl, int pr, View child, RecyclerView.LayoutParams params, int pos,RecyclerView parent) {
        ViewGroup.LayoutParams prentLayoutParams = parent.getLayoutParams();
        if (isDrawTitleBg) {
            mTitlePaint.setColor(mTitleBgColor);
            canvas.drawRect(pl, child.getTop() - params.topMargin - mTitleHeight, parent.getRight()  - parent.getPaddingEnd()
                    , child.getTop() - params.topMargin, mTitlePaint);
        }

        IGridItem item = gridItems.get(pos);
        String content = item.getTag();
        if (TextUtils.isEmpty(content))
            return;

        mTitlePaint.setColor(mTitleColor);
        mTitlePaint.setTextSize(mTitleFontSize);
        mTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTitlePaint.getTextBounds(content, 0, content.length(), mRect);
        float x = UIUtils.dip2px(20f);
        float y = child.getTop() - params.topMargin - (mTitleHeight - mRect.height()) / 2;
        canvas.drawText(content, x, y, mTitlePaint);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);


        int position = parent.getChildAdapterPosition(view);
        IGridItem item = gridItems.get(position);
        if (item == null || !item.isShow())
            return;
        if (position == 0) {
            outRect.set(0, mTitleHeight, 0, 0);
            mCurrentSpanSize = item.getSpanSize();
        } else {
            if (!offsetPositions.isEmpty() && offsetPositions.contains(position)) {
                outRect.set(0, mTitleHeight, 0, 0);
                return;
            }

            if (!TextUtils.isEmpty(item.getTag()) && !item.getTag().equals(gridItems.get(position - 1).getTag())) {
                mCurrentSpanSize = item.getSpanSize();
            } else
                mCurrentSpanSize += item.getSpanSize();

            if (mCurrentSpanSize <= totalSpanSize) {
                outRect.set(0, mTitleHeight, 0, 0);
                offsetPositions.add(position);
            }
        }
    }

    static class Config {
        // 数据
        List<? extends IGridItem> gridItems;
        // 总的SpanSize 来自GridLayoutManager
        int totalSpanSize;
        // 颜色 默认颜色
        boolean isDrawTitleBg = false;
        int titleBgColor;
        int titleTextColor = Color.parseColor("#4e5864");
        // 高度 40dp
        int titleHeight = 40;
        // 文本大小 24sp
        int titleFontSize = 40;
    }

    public static class Builder {
        private Config config;
        private Context context;

        public Builder(Context context, List<? extends IGridItem> gridItems, int totalSpanSize) {
            config = new Config();
            this.context = context;
            config.gridItems = gridItems;
            config.totalSpanSize = totalSpanSize;
        }

        public Builder setTitleBgColor(int titleBgColor) {
            config.titleBgColor = titleBgColor;
            config.isDrawTitleBg = true;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor) {
            config.titleTextColor = titleTextColor;
            return this;
        }

        /**
         * 设置高度
         *
         * @param titleHeight 高度 单位为Dp
         */
        public Builder setTitleHeight(int titleHeight) {
            config.titleHeight = titleHeight;
            return this;
        }

        /**
         * 设置文本大小
         *
         * @param fontSize 文本带下 单位为Sp
         */
        public Builder setTitleFontSize(int fontSize) {
            config.titleFontSize = fontSize;
            return this;
        }

        public GridItemDecoration build() {
            return new GridItemDecoration(config, context);
        }
    }

}
