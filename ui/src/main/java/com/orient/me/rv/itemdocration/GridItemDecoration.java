package com.orient.me.rv.itemdocration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.orient.me.data.IGridItem;

import java.util.List;

/**
 * 适用于GridLayoutManager的分割线
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    // TODO 利用构建者模式

    // 显示数据
    private List<IGridItem> gridItems;
    // 画笔
    private Paint mTitlePaint;
    // 存放文字
    private Rect mRect;
    // 颜色
    private int titleBgColor;
    private int titleColor;

    // 总的SpanSize
    private int totalSpanSize;
    private int mCurrentSpanSize;

    private int mTitleHeight;
    private int mTitleFontSize;





    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);


    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        if(position == 0){

        }
    }
}
