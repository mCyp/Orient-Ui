package com.orient.me.widget.rv.layoutmanager.table;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.orient.me.utils.UIUtils;
import com.orient.me.widget.rv.rv.ScrollerCallback;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * -
 * 二维表格LayoutManager
 * <p>
 * 目标：
 * 1. 表格内容使用固定权重或者宽高
 * 2. 可横向或者纵向滑动
 * 3. 左侧和上侧 悬浮
 * 4. 使用坐标
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class TableLayoutManager extends RecyclerView.LayoutManager {
    // TODO
    // 1. 添加和删除子View更新LayoutState中的ViewPositionCache
    // 2. 测量完成更新横纵向坐标
    // 3. 滑动的时候在滑动的函数里面处理LayoutState关于横滑和纵滑的状态
    // 4. 滑动完成处理状态
    // 5. 水平滑动的时候子View添加的顺序
    // 6. 计算横向或者纵向首行子视图的偏移量

    private static final String TAG = "TableLayoutManager";

    // 思路：
    // 考虑如何设置动态方向
    // 默认横纵向的权重为24
    private static final int s_base_span = 6;
    // 横纵向的模式
    private static final int s_row_span = 0x0001;
    private static final int s_row_value = 0x0002;
    private static final int s_col_span = 0x0010;
    private static final int s_col_value = 0x0020;
    // 默认的滚动位置
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;

    // 模式 A：横纵都是利用权重
    public static final int MODE_A = s_row_span | s_col_span;
    // 模式 B：横纵都是使用的具体值
    public static final int MODE_B = s_row_value | s_col_value;
    // 模式 C：横-权重 纵-具体值
    public static final int MODE_C = s_row_span | s_col_value;
    // 模式 D：横-具体指 纵-权重
    public static final int MODE_D = s_row_value | s_col_span;

    // 横纵向默认所有的权重 24
    private int mHorTotalSpan = s_base_span;
    private int mVerTotalSpan = s_base_span;

    // 当前模式
    private int mode;
    // 横纵向固定宽度
    private int mAveHolderWidth;
    private int mAveHolderHeight;
    // 多余的长度
    private int mWidthReminder;
    private int mHeightReminder;

    // 是否可以横纵向滑动的监听
    private ScrollerCallback scrollerCallback;
    private int[] mHorCacheBorders;
    private int[] mVerCacheBorders;

    // 主方向工具类、辅助方向工具类
    // 主方向是总方向、辅助方向是横方向
    private OrientationHelper mMainOrientationHelper;
    private OrientationHelper mAssistOrientationHelper;
    private View[] mSet;

    // 预布局SpanSize和SpanIndex设置
    final HashMap<String, Integer[]> mPreLayoutSpanCache = new HashMap<>();

    // 滚动的目标位置的标记位
    private int mPendingScrollPosition = RecyclerView.NO_POSITION;
    // 滑动到目标位置携带偏移量
    private int mPendingScrollPositionOffset = INVALID_OFFSET;

    /**
     * 辅助工具类记录当前布局的状态
     * 尽管当布局完成后这个类就不起作用了，但是仍然可以复用当下次布局的时候
     */
    private LayoutState mLayoutState;

    /**
     * 重用的参数当重新布局的时候
     */
    final AnchorInfo mAnchorInfo = new AnchorInfo();

    /**
     * 当前的滑动方向
     */
    private int mOrientation = RecyclerView.VERTICAL;

    /**
     * 当前屏幕起始二维坐标记录辅助类
     */
    private final ScreenCoordinateRecorder mScreenCoordinateRecorder = new ScreenCoordinateRecorder();

    /**
     * 位置转换回调
     */
    private CoordinateCallback mCoordinateCallback;

    /**
     * 加载一行结果的工具类
     */
    private LayoutChunkResult mLayoutChunkResult = new LayoutChunkResult();

    // 滑动标记位 true表示正在滑动
    private boolean scrollFlag = false;

    /**
     * 构造函数
     *
     * @param mode 模式
     * @param w    横向的参数 具体的宽度或者权重
     * @param h    纵向到的参数 具体的高度或者权重
     */
    public TableLayoutManager(int mode, int w, int h) {
        // 默认使用横向限制
        this.mode = mode;
        if ((mode & s_row_span) != 0) {
            mHorTotalSpan = w;
        } else if ((mode & s_row_value) != 0) {
            mAveHolderWidth = w;
        }

        if ((mode & s_col_span) != 0) {
            mVerTotalSpan = h;
        } else if ((mode & s_row_value) != 0) {
            mAveHolderHeight = h;
        }

        initOrientHelper();
    }


    /**
     * 初始化工具类
     */
    private void initOrientHelper() {
        assertNotInLayoutOrScroll(null);
        mMainOrientationHelper = OrientationHelper.createVerticalHelper(this);
        mAnchorInfo.mOrientationHelper = mMainOrientationHelper;
        mAssistOrientationHelper = OrientationHelper.createHorizontalHelper(this);
        requestLayout();
    }

    /**
     * 设置横纵向滑动监听器
     */
    public void setScrollerCallback(ScrollerCallback callback) {
        this.scrollerCallback = callback;
    }

    /**
     * 设置坐标
     *
     * @param coordinateCallback 坐标的转换类
     */
    public void setCoordinateCallback(CoordinateCallback coordinateCallback) {
        this.mCoordinateCallback = coordinateCallback;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new TableLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        } else {
            return new LayoutParams(lp);
        }
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    /**
     * 得到当前屏幕的其实坐标
     */
    public int[] getCurrentScreenStartCoordinate() {
        return new int[]{mScreenCoordinateRecorder.mCurStartRowIndex, mScreenCoordinateRecorder.mCurStartColIndex};
    }

    /**
     * 得到第一个位置的偏移量
     */
    public int[] getCurrentFirstOffset() {
        if (mLayoutState == null)
            return null;
        return new int[]{mLayoutState.mHeadXOffset, mLayoutState.mHeadYOffset};
    }



    /**
     * 得到子视图的宽和高
     */
    public int[] getChildViewWidthAndHeight() {
        int width;
        int height;
        width =  mAveHolderWidth;
        height = mAveHolderHeight;
        return new int[]{width, height};
    }

    public boolean isRowSpan() {
        return (mode & s_row_span) != 0;
    }

    public boolean isColSpan() {
        return (mode & s_col_span) != 0;
    }

    public int[] getReminder() {
        return new int[]{mWidthReminder, mHeightReminder};
    }

    @Override
    public boolean canScrollHorizontally() {
        if (scrollFlag && mOrientation == RecyclerView.VERTICAL) {
            return false;
        }
        return !scrollerCallback.canScrollVertical();
        //return true;
    }

    @Override
    public boolean canScrollVertically() {
        if (scrollFlag && mOrientation == RecyclerView.HORIZONTAL) {
            return false;
        }
        return scrollerCallback.canScrollVertical();
        //return true;
    }


    @Override
    public synchronized int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        /*if (mOrientation == RecyclerView.VERTICAL)
            return 0;*/
       /* if (scrollFlag && mOrientation == RecyclerView.VERTICAL)
            return 0;*/
        if (scrollerCallback.canScrollVertical())
            return 0;

        scrollFlag = true;
        mOrientation = RecyclerView.HORIZONTAL;
        updateMeasurements();
        ensureViewSet();
        return scrollBy(dx, recycler, state);
    }

    @Override
    public synchronized int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        /*if (mOrientation == RecyclerView.HORIZONTAL)
            return 0;*/
       /* if (scrollFlag && mOrientation == RecyclerView.HORIZONTAL)
            return 0;*/
       /* if (!scrollerCallback.canScrollVertical())
            return 0;*/

        scrollFlag = true;
        mOrientation = RecyclerView.VERTICAL;
        updateMeasurements();
        ensureViewSet();
        return scrollBy(dy, recycler, state);
    }

    public void clearScrollFlag() {
        scrollFlag = false;
    }

    private int scrollBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy == 0 || getChildCount() == 0)
            return 0;

        mLayoutState.mRecycle = true;
        ensureLayoutState();
        final int layoutDirection = dy > 0 ? LayoutState.LAYOUT_END : LayoutState.LAYOUT_START;
        final int absDy = Math.abs(dy);
        updateLayoutState(layoutDirection, absDy, true, state);
        final int consumed = mLayoutState.mScrollingOffset + fill(recycler, mLayoutState, state, false);
        if (consumed <= 0)
            return 0;
        final int scrolled = absDy > consumed ? layoutDirection * consumed : dy;
        if (mOrientation == RecyclerView.VERTICAL) {
            mMainOrientationHelper.offsetChildren(-scrolled);
            calculateScreenStartCoordinate(true, scrolled);
        } else {
            mAssistOrientationHelper.offsetChildren(-scrolled);
            calculateScreenStartCoordinate(false, scrolled);
        }
        mLayoutState.mLastScrollDelta = scrolled;
        mLayoutState.mLastScrollOrientation = mOrientation;
        return scrolled;
    }

    /**
     * 更新屏幕的起始点的坐标
     *
     * @param isVertical 是否是竖直方向
     * @param offset     偏移量
     */
    private void calculateScreenStartCoordinate(boolean isVertical, int offset) {
        if (isVertical) {
            final View child = getChildCloseToStart();
            if (child == null)
                return;

            int top = mMainOrientationHelper.getDecoratedStart(child);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int pos = lp.getViewAdapterPosition();
            int[] coordinate = mCoordinateCallback.coordinate(pos);
            int currentRow = coordinate[0];
            while (top <= -mAveHolderHeight) {
                top += mAveHolderHeight;
                currentRow++;
            }
            mLayoutState.mHeadYOffset = top;
            mScreenCoordinateRecorder.mCurStartRowIndex = currentRow;
        } else {
            View child = getChildCloseToStart();
            if (child == null)
                return;

            int left = mAssistOrientationHelper.getDecoratedStart(child);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int pos = lp.getViewAdapterPosition();
            int[] coordinate = mCoordinateCallback.coordinate(pos);
            int currentCol = coordinate[1];
            while (left <= -mAveHolderWidth) {
                left += mAveHolderWidth;
                currentCol++;
            }
            mLayoutState.mHeadXOffset = left;
            mScreenCoordinateRecorder.mCurStartColIndex = currentCol;
        }
    }

    /**
     * 滑动的时候初始化LayoutState
     *
     * @param layoutDirection      方向
     * @param requireSpace         需要的空间
     * @param canUserExistingSpace 可以使用的空间
     * @param state                RecyclerView.State
     */
    private void updateLayoutState(int layoutDirection, int requireSpace
            , boolean canUserExistingSpace, RecyclerView.State state) {
        mLayoutState.mInfinite = false;
        mLayoutState.mExtra = getExtraLayoutSpace(state);
        mLayoutState.mLayoutDirection = layoutDirection;
        int scrollingOffset;
        if (mOrientation == RecyclerView.VERTICAL) {
            scrollingOffset = updateLayoutInVertical(layoutDirection);
        } else {
            scrollingOffset = updateLayoutInHorizontal(layoutDirection);
        }
        mLayoutState.mAvailable = requireSpace;
        if (canUserExistingSpace) {
            mLayoutState.mAvailable -= scrollingOffset;
        }
        mLayoutState.mScrollingOffset = scrollingOffset;
    }

    private int updateLayoutInVertical(int layoutDirection) {
        int scrollingOffset;
        if (layoutDirection == LayoutState.LAYOUT_END) {
            mLayoutState.mExtra += mMainOrientationHelper.getEndPadding();
            mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_TAIL;

            final View child = getChildCloseToEnd();
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int pos = lp.getViewAdapterPosition();
            int[] coordinate = mCoordinateCallback.coordinate(pos);
            if (lp.mHeightSpan == 1) {
                mLayoutState.mCurRow = coordinate[0];
                mLayoutState.mCurRow += mLayoutState.mItemDirection;
                mLayoutState.mCurCol = mScreenCoordinateRecorder.mCurStartColIndex;
                mLayoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(mLayoutState.mCurRow, mLayoutState.mCurCol);
                mLayoutState.mYOffset = mMainOrientationHelper.getDecoratedEnd(child);
                scrollingOffset = mMainOrientationHelper.getDecoratedEnd(child) - mMainOrientationHelper.getEndAfterPadding();
            } else {
                int currentRow = coordinate[0] + lp.mHeightSpan;
                int bottom = mMainOrientationHelper.getDecoratedEnd(child);
                while (bottom >= mMainOrientationHelper.getEndAfterPadding() + mAveHolderHeight) {
                    bottom -= mAveHolderHeight;
                    currentRow--;
                }
                mLayoutState.mCurRow = currentRow;
                mLayoutState.mCurCol = mScreenCoordinateRecorder.mCurStartColIndex;
                mLayoutState.mYOffset = bottom;
                scrollingOffset = bottom - mMainOrientationHelper.getEndAfterPadding();
            }
        } else {
            mLayoutState.mExtra += mMainOrientationHelper.getStartAfterPadding();
            mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_HEAD;
            mLayoutState.mCurRow = mScreenCoordinateRecorder.mCurStartRowIndex;
            mLayoutState.mCurCol = mScreenCoordinateRecorder.mCurStartColIndex;
            mLayoutState.mCurRow += mLayoutState.mItemDirection;
            mLayoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(mScreenCoordinateRecorder.mCurStartRowIndex
                    , mScreenCoordinateRecorder.mCurStartColIndex);

            final View child = getChildCloseToStart();
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int[] coordinate = mCoordinateCallback.coordinate(lp.getViewAdapterPosition());
            if (lp.mHeightSpan == 1) {
                mLayoutState.mYOffset = mMainOrientationHelper.getDecoratedStart(child);
                scrollingOffset = -mMainOrientationHelper.getDecoratedStart(child) + mMainOrientationHelper.getStartAfterPadding();
            } else {
                int delta = mScreenCoordinateRecorder.mCurStartRowIndex - coordinate[0];
                mLayoutState.mYOffset = mMainOrientationHelper.getDecoratedStart(child) + delta * mAveHolderHeight;
                scrollingOffset = -(mMainOrientationHelper.getDecoratedStart(child) + delta * mAveHolderHeight) + mMainOrientationHelper.getStartAfterPadding();
            }
        }
        return scrollingOffset;
    }

    private int updateLayoutInHorizontal(int layoutDirection) {
        int scrollingOffset;
        if (layoutDirection == LayoutState.LAYOUT_END) {
            mLayoutState.mExtra += mAssistOrientationHelper.getEndPadding();
            mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_TAIL;

            final View child = getChildCloseToEnd();
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int pos = lp.getViewAdapterPosition();
            int[] coordinate = mCoordinateCallback.coordinate(pos);
            if (lp.mWidthSpan == 1) {
                mLayoutState.mCurCol = coordinate[1];
                mLayoutState.mCurCol += mLayoutState.mItemDirection;
                mLayoutState.mCurRow = mScreenCoordinateRecorder.mCurStartRowIndex;
                mLayoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(mLayoutState.mCurRow, mLayoutState.mCurCol);
                mLayoutState.mXOffset = mAssistOrientationHelper.getDecoratedEnd(child);
                scrollingOffset = mAssistOrientationHelper.getDecoratedEnd(child) - mAssistOrientationHelper.getEndAfterPadding();
            } else {
                int currentCol = coordinate[1] + lp.mWidthSpan;
                int right = mAssistOrientationHelper.getDecoratedEnd(child);
                while (right >= mAssistOrientationHelper.getEndAfterPadding() + mAveHolderWidth) {
                    right -= mAveHolderWidth;
                    currentCol--;
                }
                mLayoutState.mCurCol = currentCol;
                mLayoutState.mCurRow = mScreenCoordinateRecorder.mCurStartRowIndex;
                mLayoutState.mXOffset = right;
                scrollingOffset = right - mAssistOrientationHelper.getEndAfterPadding();
            }
        } else {
            mLayoutState.mExtra += mAssistOrientationHelper.getStartAfterPadding();
            mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_HEAD;
            mLayoutState.mCurRow = mScreenCoordinateRecorder.mCurStartRowIndex;
            mLayoutState.mCurCol = mScreenCoordinateRecorder.mCurStartColIndex;
            mLayoutState.mCurCol += mLayoutState.mItemDirection;
            mLayoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(mScreenCoordinateRecorder.mCurStartRowIndex
                    , mScreenCoordinateRecorder.mCurStartColIndex);

            final View child = getChildCloseToStart();
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int[] coordinate = mCoordinateCallback.coordinate(lp.getViewAdapterPosition());
            if (lp.mWidthSpan == 1) {
                mLayoutState.mXOffset = mAssistOrientationHelper.getDecoratedStart(child);
                scrollingOffset = -mAssistOrientationHelper.getDecoratedStart(child) + mAssistOrientationHelper.getStartAfterPadding();
            } else {
                int delta = mScreenCoordinateRecorder.mCurStartColIndex - coordinate[1];
                mLayoutState.mXOffset = mAssistOrientationHelper.getDecoratedStart(child) + delta * mAveHolderWidth;
                scrollingOffset = -(mAssistOrientationHelper.getDecoratedStart(child) + delta * mAveHolderWidth) + mAssistOrientationHelper.getStartAfterPadding();
            }
        }
        return scrollingOffset;
    }

    private View getChildCloseToEnd() {
        if (mOrientation == RecyclerView.VERTICAL) {
            return getChildAt(getChildCount() - 1);
        } else {
            int maxColPos = getChildCount() - 1;
            int maxCol = Integer.MIN_VALUE;
            for (int i = getChildCount() - 1; i >= getChildCount() - 1 - mHorTotalSpan && i >= 0; i--) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int[] coordinate = mCoordinateCallback.coordinate(lp.getViewAdapterPosition());
                if (maxCol < coordinate[1]) {
                    maxCol = coordinate[1];
                    maxColPos = i;
                    if (mAssistOrientationHelper.getDecoratedEnd(child) > mAssistOrientationHelper.getEndAfterPadding())
                        break;
                }
            }
            return getChildAt(maxColPos);
        }
    }


    private View getChildCloseToStart() {
        if (mOrientation == RecyclerView.VERTICAL)
            return getChildAt(0);
        else {
            int minColPos = 0;
            int minCol = Integer.MAX_VALUE;
            for (int i = 0; i < mHorTotalSpan && i < getChildCount(); i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int[] coordinate = mCoordinateCallback.coordinate(lp.getViewAdapterPosition());
                if (coordinate[1] < minCol) {
                    minCol = coordinate[1];
                    minColPos = i;
                    if (mAssistOrientationHelper.getDecoratedStart(child) < 0)
                        break;
                }
            }
            return getChildAt(minColPos);
        }
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        // 计算边界值
        if (state.isPreLayout())
            cachePreLayoutSpanMapping();

        if (mPendingScrollPosition != RecyclerView.NO_POSITION) {
            if (state.getItemCount() == 0) {
                removeAndRecycleAllViews(recycler);
            }
        }

        // 1. 回收视图
        detachAndScrapAttachedViews(recycler);
        if(mLayoutState != null){
            mLayoutState.clear();
        }
        ensureLayoutState();
        mLayoutState.mRecycle = false;
        // 寻找焦点视图
        final View focused = getFocusedChild();

        // 2. 设置锚点信息
        if (!mAnchorInfo.mValid || mPendingScrollPosition != NO_POSITION) {
            mAnchorInfo.reset();
            mAnchorInfo.mLayoutFromEnd = false;
            updateAnchorInfoForLayout(recycler, state, mAnchorInfo);
            mAnchorInfo.mValid = true;
        } else if (focused != null
                && (mMainOrientationHelper.getDecoratedEnd(focused) <= mMainOrientationHelper.getStartAfterPadding()
                || mMainOrientationHelper.getDecoratedStart(focused) >= mMainOrientationHelper.getEndAfterPadding())) {
            // TODO 修改
            mAnchorInfo.assignFromViewAndKeepVisibleRect(focused, getPosition(focused));
        }

        onAnchorReady(recycler, state, mAnchorInfo,focused);

        // 3. 填充视图
        int extraForEnd = 0, extraForStart = 0;
        final int extra = getExtraLayoutSpace(state);
        if (mLayoutState.mLastScrollOrientation == RecyclerView.VERTICAL) {
            if (mLayoutState.mLastScrollDelta >= 0) {
                extraForEnd = extra;
                extraForStart = 0;
            } else {
                extraForEnd = 0;
                extraForStart = extra;
            }
        }
        extraForStart += mMainOrientationHelper.getStartAfterPadding();
        extraForEnd += mMainOrientationHelper.getEndPadding();
        int startOffset = 0, endOffset = 0;

        // 3.1 从锚点往结束的方向填充
        updateLayoutStateToFillEnd(mAnchorInfo);
        mLayoutState.mExtra = extraForEnd;
        fill(recycler, mLayoutState, state, false);
        endOffset = mLayoutState.mYOffset;
        final int lastRow = mLayoutState.mCurRow;
        if (mLayoutState.mAvailable > 0) {
            extraForStart += mLayoutState.mAvailable;
        }
        // 3.2 从锚点往开始的方便填充
        updateLayoutStateToFillStart(mAnchorInfo);
        mLayoutState.mExtra = extraForStart;
        mLayoutState.mCurRow += mLayoutState.mItemDirection;
        fill(recycler, mLayoutState, state, false);
        startOffset = mLayoutState.mYOffset;

        if (mLayoutState.mAvailable > 0) {
            extraForEnd = mLayoutState.mAvailable;
            // start could not consume all it should. add more items towards end
            mLayoutState.mCurRow = lastRow;
            mLayoutState.mCurCol = mScreenCoordinateRecorder.mCurStartColIndex;
            mLayoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(mLayoutState.mCurRow, mLayoutState.mCurCol);
            updateLayoutStateToFillEnd(lastRow, mLayoutState.mCurCol, endOffset);
            mLayoutState.mExtra = extraForEnd;
            fill(recycler, mLayoutState, state, false);
            endOffset = mLayoutState.mYOffset;
        }

        if(focused != null){
            // 键盘导致界面变形
            if(mMainOrientationHelper.getEndAfterPadding() - endOffset != 0){
                calculateScreenStartCoordinate(true,0);
            }
        }

        // 3.3 修复其中的间隙
        if (getChildCount() > 0) {
            // because layout from end may be changed by scroll to position
            // we re-calculate it.
            // find which side we should check for gaps.
            int fixOffset = fixLayoutEndGap(endOffset, recycler, state, false);
            startOffset += fixOffset;
            fixLayoutStartGap(startOffset, recycler, state, true);
        }
        if (!state.isPreLayout()) {
            mMainOrientationHelper.onLayoutComplete();

        } else {
            mAnchorInfo.reset();
        }

    }

    private void cachePreLayoutSpanMapping() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LayoutParams params = (LayoutParams) getChildAt(i).getLayoutParams();
            int row = mScreenCoordinateRecorder.mCurStartRowIndex + params.mRowIndex;
            int col = mScreenCoordinateRecorder.mCurStartColIndex + params.mColIndex;
            String key = row + "-" + col;
            mPreLayoutSpanCache.put(key, new Integer[]{params.mRowIndex, params.mColIndex, params.mWidthSpan, params.mHeightSpan});
        }
    }

    /**
     * 确保当前的LayoutState不为空
     */
    private void ensureLayoutState() {
        if (mLayoutState == null) {
            mLayoutState = new LayoutState();
            mLayoutState.mCoordinateCallback = mCoordinateCallback;
        }
    }

    /**
     * 更新锚点信息
     */
    private void updateAnchorInfoForLayout(RecyclerView.Recycler recycler, RecyclerView.State state,
                                           AnchorInfo anchorInfo) {
        if (updateAnchorFromPendingData(state, anchorInfo)) {
            return;
        }

        if (updateAnchorFromChildren(recycler, state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        anchorInfo.mPosition = 0;
    }

    private boolean updateAnchorFromPendingData(RecyclerView.State state, AnchorInfo anchorInfo) {
        if (state.isPreLayout() || mPendingScrollPosition == NO_POSITION) {
            return false;
        }
        // validate scroll position
        if (mPendingScrollPosition < 0 || mPendingScrollPosition >= state.getItemCount()) {
            mPendingScrollPosition = NO_POSITION;
            mPendingScrollPositionOffset = INVALID_OFFSET;
            return false;
        }

        // if child is visible, try to make it a reference child and ensure it is fully visible.
        // if child is not visible, align it depending on its virtual position.
        anchorInfo.mPosition = mPendingScrollPosition;
        if (mPendingScrollPositionOffset == INVALID_OFFSET) {
            View child = findViewByPosition(mPendingScrollPosition);
            if (child != null) {
                final int childSize = mMainOrientationHelper.getDecoratedMeasurement(child);
                if (childSize > mMainOrientationHelper.getTotalSpace()) {
                    // item does not fit. fix depending on layout direction
                    anchorInfo.assignCoordinateFromPadding();
                    return true;
                }
                final int startGap = mMainOrientationHelper.getDecoratedStart(child)
                        - mMainOrientationHelper.getStartAfterPadding();
                if (startGap < 0) {
                    anchorInfo.mCoordinate = mMainOrientationHelper.getStartAfterPadding();
                    anchorInfo.mLayoutFromEnd = false;
                    return true;
                }
                final int endGap = mMainOrientationHelper.getEndAfterPadding()
                        - mMainOrientationHelper.getDecoratedEnd(child);
                if (endGap < 0) {
                    anchorInfo.mCoordinate = mMainOrientationHelper.getEndAfterPadding();
                    anchorInfo.mLayoutFromEnd = true;
                    return true;
                }
                anchorInfo.mCoordinate = anchorInfo.mLayoutFromEnd
                        ? (mMainOrientationHelper.getDecoratedEnd(child) + mMainOrientationHelper
                        .getTotalSpaceChange())
                        : mMainOrientationHelper.getDecoratedStart(child);
            } else { // item is not visible.
                if (getChildCount() > 0) {
                    // get position of any child, does not matter
                    int pos = getPosition(getChildAt(0));
                    anchorInfo.mLayoutFromEnd = mPendingScrollPosition < pos;
                }
                anchorInfo.assignCoordinateFromPadding();
            }
            return true;
        }
        // override layout from end values for consistency
        anchorInfo.mLayoutFromEnd = false;
        anchorInfo.mCoordinate = mMainOrientationHelper.getStartAfterPadding()
                + mPendingScrollPositionOffset;
        return true;
    }

    /**
     * Finds an anchor child from existing Views. Most of the time, this is the view closest to
     * start or end that has a valid position (e.g. not removed).
     * <p>
     * If a child has focus, it is given priority.
     */
    private boolean updateAnchorFromChildren(RecyclerView.Recycler recycler,
                                             RecyclerView.State state, AnchorInfo anchorInfo) {
        if (getChildCount() == 0) {
            return false;
        }
        final View focused = getFocusedChild();
        if (focused != null && anchorInfo.isViewValidAsAnchor(focused, state)) {
            anchorInfo.assignFromViewAndKeepVisibleRect(focused, getPosition(focused));
            return true;
        }

        View referenceChild = anchorInfo.mLayoutFromEnd
                ? findLastReferenceChild(recycler, state)
                : findFirstReferenceChild(recycler, state);
        if (referenceChild != null) {
            anchorInfo.assignFromView(referenceChild, getPosition(referenceChild));
            if (!state.isPreLayout() && supportsPredictiveItemAnimations()) {
                final boolean notVisible =
                        mMainOrientationHelper.getDecoratedStart(referenceChild) >= mMainOrientationHelper
                                .getEndAfterPadding()
                                || mMainOrientationHelper.getDecoratedEnd(referenceChild)
                                < mMainOrientationHelper.getStartAfterPadding();
                if (notVisible) {
                    anchorInfo.mCoordinate = anchorInfo.mLayoutFromEnd
                            ? mMainOrientationHelper.getEndAfterPadding()
                            : mMainOrientationHelper.getStartAfterPadding();
                }
            }
            return true;
        }
        return false;
    }

    private View findFirstReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return findReferenceChild(recycler, state, 0, getChildCount(), state.getItemCount());
    }

    private View findLastReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return findReferenceChild(recycler, state, getChildCount() - 1, -1, state.getItemCount());
    }

    private View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state,
                                    int start, int end, int itemCount) {
        ensureLayoutState();
        View invalidMatch = null;
        View outOfBoundsMatch = null;
        final int boundsStart = mMainOrientationHelper.getStartAfterPadding();
        final int boundsEnd = mMainOrientationHelper.getEndAfterPadding();
        final int diff = end > start ? 1 : -1;
        for (int i = start; i != end; i += diff) {
            final View view = getChildAt(i);
            final int position = getPosition(view);
            if (position >= 0 && position < itemCount) {
                if (((RecyclerView.LayoutParams) view.getLayoutParams()).isItemRemoved()) {
                    if (invalidMatch == null) {
                        invalidMatch = view; // removed item, least preferred
                    }
                } else if (mMainOrientationHelper.getDecoratedStart(view) >= boundsEnd
                        || mMainOrientationHelper.getDecoratedEnd(view) < boundsStart) {
                    if (outOfBoundsMatch == null) {
                        outOfBoundsMatch = view; // item is not visible, less preferred
                    }
                } else {
                    return view;
                }
            }
        }
        return outOfBoundsMatch != null ? outOfBoundsMatch : invalidMatch;
    }

    /**
     * 锚点生成后的准备工作
     *
     * @param recycler   Recycler
     * @param state      State
     * @param anchorInfo 锚点信息
     */
    private void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state,
                               AnchorInfo anchorInfo,View focused) {
        updateMeasurements();
        // 确保锚点的位置
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            if(focused != null) {
                LayoutParams lp = (LayoutParams) focused.getLayoutParams();
                if (lp != null) {
                    int[] coordinate = mCoordinateCallback.coordinate(lp.getViewAdapterPosition());
                    if (coordinate != null) {
                        anchorInfo.mRow = coordinate[0];
                        anchorInfo.mCol = coordinate[1];
                    }
                }
            }
            ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo);
        }
        ensureViewSet();
    }

    private void updateMeasurements() {
        int horSpace, verSpace;
        horSpace = getWidth() - getPaddingStart() - getPaddingEnd();
        verSpace = getHeight() - getPaddingTop() - getPaddingBottom();
        calculateItemBorders(horSpace, verSpace, mOrientation);
    }

    /**
     * @param horSpace    可用横向距离
     * @param verSpace    可用纵向距离
     * @param orientation 方向
     */
    private void calculateItemBorders(int horSpace, int verSpace, int orientation) {
        if (orientation == RecyclerView.VERTICAL) {
            // 更新横向mHorCacheBorders
            if ((mode & s_row_span) != 0) {
                mHorCacheBorders = calculateSpanBorders(horSpace, mHorTotalSpan, mHorCacheBorders, mLayoutState.mHeadXOffset);
                mWidthReminder = horSpace % mHorTotalSpan;
                mAveHolderWidth = mWidthReminder == 0 ? horSpace / mHorTotalSpan : horSpace / mHorTotalSpan + 1;
            } else {
                mHorCacheBorders = calculateValueBorder(horSpace, mAveHolderWidth, mVerCacheBorders, mLayoutState.mHeadXOffset);
                mHorTotalSpan = mHorCacheBorders.length - 2;
            }

            if ((mode & s_col_span) != 0) {
                mHeightReminder = verSpace % mVerTotalSpan;
                mAveHolderHeight = mHeightReminder == 0 ? verSpace / mVerTotalSpan : verSpace / mVerTotalSpan + 1;
            }
        } else {
            // 新纵向mVerCacheBorders
            if ((mode & s_col_span) != 0) {
                mVerCacheBorders = calculateSpanBorders(verSpace, mVerTotalSpan, mVerCacheBorders, mLayoutState.mHeadYOffset);
                mHeightReminder = verSpace % mVerTotalSpan;
                mAveHolderHeight = mHeightReminder == 0 ? verSpace / mVerTotalSpan : verSpace / mVerTotalSpan + 1;
            } else {
                mVerCacheBorders = calculateValueBorder(verSpace, mAveHolderHeight, mVerCacheBorders, mLayoutState.mHeadYOffset);
                mVerTotalSpan = mVerCacheBorders.length - 2;
            }

            if ((mode & s_row_span) != 0) {
                mWidthReminder = horSpace % mHorTotalSpan;
                mAveHolderWidth = mWidthReminder == 0 ? horSpace / mHorTotalSpan : horSpace / mHorTotalSpan + 1;
            }
        }
    }

    /**
     * 计算以Span为列或行的坐标
     *
     * @param totalSpace   全长
     * @param spanCount    span数量
     * @param cacheBorders 数组
     * @param offset       第一个Cell横向或者纵向的偏移
     * @return 数组
     */
    private int[] calculateSpanBorders(int totalSpace, int spanCount, int[] cacheBorders, int offset) {
        if (cacheBorders == null || cacheBorders.length != spanCount + 2) {
            cacheBorders = new int[spanCount + 2];
        }
        cacheBorders[cacheBorders.length - 1] = 0;
        cacheBorders[cacheBorders.length - 2] = 0;
        if (offset < 0) {
            cacheBorders[0] = offset;
        } else {
            cacheBorders[0] = 0;
        }
        int sizePerSpanReminder = totalSpace % spanCount;
        int sizePerSpan = sizePerSpanReminder == 0 ? totalSpace / spanCount : totalSpace / spanCount + 1;
        int consumePixels = offset;
        for (int i = 1; i <= spanCount; i++) {
            int itemSize = sizePerSpan;
            /*if (sizePerSpanReminder > 0) {
                itemSize++;
                sizePerSpanReminder--;
            }*/
            consumePixels += itemSize;
            cacheBorders[i] = consumePixels;
        }
        if (consumePixels < totalSpace) {
            cacheBorders[spanCount + 1] = consumePixels + sizePerSpan;
        } else {
            cacheBorders[spanCount + 1] = 0;
        }
        return cacheBorders;
    }

    /**
     * 计算以固定长度为列或行的坐标
     *
     * @param totalSpace   权杖
     * @param value        长或宽的具体的值
     * @param cacheBorders 边界数组
     * @param offset       偏移量
     * @return 数组
     */
    private int[] calculateValueBorder(int totalSpace, int value, int[] cacheBorders, int offset) {
        int spanCount = totalSpace / value;
        int sizeReminder = totalSpace % value;
        if (sizeReminder != 0) {
            spanCount++;
        }
        cacheBorders = new int[spanCount + 2];
        if (offset < 0) {
            cacheBorders[0] = offset;
        } else {
            cacheBorders[0] = 0;
        }
        int consumePixels = offset;
        for (int i = 1; i <= spanCount; i++) {
            consumePixels += value;
            cacheBorders[i] = consumePixels;
        }
        if (consumePixels < totalSpace) {
            cacheBorders[spanCount + 1] = consumePixels + value;
        } else {
            cacheBorders[spanCount + 1] = 0;
        }
        return cacheBorders;
    }

    private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler recycler,
                                             RecyclerView.State state, AnchorInfo anchorInfo) {
        // 暂时不考虑逆序

        if (anchorInfo.mRow == 0 && anchorInfo.mCol == 0)
            return;

        while (anchorInfo.mRow > mScreenCoordinateRecorder.mCurStartColIndex && anchorInfo.mCol > mScreenCoordinateRecorder.mCurStartColIndex) {
            if (mOrientation == RecyclerView.VERTICAL) {
                anchorInfo.mRow--;
            } else {
                anchorInfo.mCol--;
            }
        }
    }

    /**
     * 检查二维坐标
     *
     * @param coordinate 坐标轴
     */
    private boolean checkCoordinate(int[] coordinate) {
        return coordinate != null
                && coordinate.length == 2;
    }

    /**
     * 确保数组正确
     */
    private void ensureViewSet() {
        if (mOrientation == RecyclerView.VERTICAL) {
            if (mSet == null || mSet.length != mHorTotalSpan) {
                mSet = new View[mHorTotalSpan + 1];
            }
        } else {
            if (mSet == null || mSet.length != mVerTotalSpan) {
                mSet = new View[mVerTotalSpan + 1];
            }
        }
    }

    /************************ onLayoutChildren第三部分的函数 ************************/

    private void updateLayoutStateToFillEnd(AnchorInfo anchorInfo) {
        updateLayoutStateToFillEnd(anchorInfo.mRow, anchorInfo.mCol, anchorInfo.mCoordinate);
    }

    private void updateLayoutStateToFillEnd(int row, int col, int offset) {
        mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_TAIL;
        mLayoutState.mLayoutDirection = LayoutState.LAYOUT_END;
        mLayoutState.mScrollingOffset = LayoutState.SCROLLING_OFFSET_NaN;
        mLayoutState.mCurRow = row;
        mLayoutState.mCurCol = col;
        mLayoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(row,col);

        if (mOrientation == RecyclerView.VERTICAL) {
            mLayoutState.mAvailable = mMainOrientationHelper.getEndAfterPadding() - offset;
            mLayoutState.mYOffset = offset;
        } else {
            mLayoutState.mAvailable = mAssistOrientationHelper.getEndAfterPadding() - offset;
            mLayoutState.mXOffset = offset;
        }

    }

    private void updateLayoutStateToFillStart(AnchorInfo anchorInfo) {
        updateLayoutStateToFillStart(anchorInfo.mRow, anchorInfo.mCol, anchorInfo.mCoordinate);
    }

    private void updateLayoutStateToFillStart(int row, int col, int offset) {
        mLayoutState.mCurRow = row;
        mLayoutState.mCurCol = col;
        mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_HEAD;
        mLayoutState.mLayoutDirection = LayoutState.LAYOUT_START;
        //mLayoutState.mCurRow += mLayoutState.mItemDirection;
        mLayoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(mLayoutState.mCurRow,mLayoutState.mCurCol);

        mLayoutState.mScrollingOffset = LayoutState.SCROLLING_OFFSET_NaN;
        if (mOrientation == RecyclerView.VERTICAL) {
            mLayoutState.mAvailable = mMainOrientationHelper.getStartAfterPadding() - offset;
            mLayoutState.mYOffset = offset;
        } else {
            mLayoutState.mAvailable = mAssistOrientationHelper.getStartAfterPadding() - offset;
            mLayoutState.mXOffset = offset;
        }
    }

    /**
     * 返回LayoutManager需要测量的额外空间
     */
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if (state.hasTargetScrollPosition()) {
            return mMainOrientationHelper.getTotalSpace();
        } else {
            return 0;
        }
    }

    private int fill(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state, boolean stopFoucusable) {
        final int start = layoutState.mAvailable;
        if (layoutState.mScrollingOffset != LayoutState.SCROLLING_OFFSET_NaN) {
            if (layoutState.mAvailable < 0) {
                layoutState.mScrollingOffset += layoutState.mAvailable;
            }
            recycleByLayoutState(recycler, mLayoutState);
        }
        int remainSpace = mLayoutState.mAvailable + mLayoutState.mExtra;
        LayoutChunkResult result = mLayoutChunkResult;
        while ((layoutState.mInfinite || remainSpace > 0)
                && hasMore(state, layoutState, mOrientation, mScreenCoordinateRecorder)) {
            result.resetInternal();

            if (mOrientation == RecyclerView.VERTICAL)
                layoutChunkInVertical(recycler, state, mLayoutState, result);
            else
                layoutChunkInHorizontal(recycler, state, mLayoutState, result);

            if (mOrientation == RecyclerView.VERTICAL) {
                layoutState.mYOffset += layoutState.mLayoutDirection * result.mConsume;
                if (!result.mIgnoreConsumed || mLayoutState.mScrapList != null
                        || !state.isPreLayout()) {
                    layoutState.mAvailable -= result.mConsume;

                    // we keep a separate remaining space because mAvailable is important for recycling
                    remainSpace -= result.mConsume;
                    layoutState.mCurRow += layoutState.mLayoutDirection * result.mConsumedRowOrCol;
                    layoutState.mCurCol = mScreenCoordinateRecorder.mCurStartColIndex;
                    layoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(layoutState.mCurRow, layoutState.mCurCol);
                }
            } else {
                layoutState.mXOffset += layoutState.mLayoutDirection * result.mConsume;
                if (!result.mIgnoreConsumed || mLayoutState.mScrapList != null
                        || !state.isPreLayout()) {
                    layoutState.mAvailable -= result.mConsume;

                    // we keep a separate remaining space because mAvailable is important for recycling
                    remainSpace -= result.mConsume;
                    layoutState.mCurCol += layoutState.mLayoutDirection * result.mConsumedRowOrCol;
                    layoutState.mCurRow = mScreenCoordinateRecorder.mCurStartRowIndex;
                    layoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(layoutState.mCurRow, layoutState.mCurCol);
                }
            }

            if (layoutState.mScrollingOffset != LayoutState.SCROLLING_OFFSET_NaN) {
                layoutState.mScrollingOffset += result.mConsume;
                if (layoutState.mAvailable < 0) {
                    layoutState.mScrollingOffset += layoutState.mAvailable;
                }
                recycleByLayoutState(recycler, layoutState);
            }
            if (stopFoucusable && result.mFocusable) {
                break;
            }
        }
        return start - layoutState.mAvailable;
    }

    /**
     * Helper method to call appropriate recycle method depending on current layout direction
     *
     * @param recycler    Current recycler that is attached to RecyclerView
     * @param layoutState Current layout state. Right now, this object does not change but
     *                    we may consider moving it out of this view so passing around as a
     *                    parameter for now, rather than accessing {@link #mLayoutState}
     * @see #recycleViewsFromStart(android.support.v7.widget.RecyclerView.Recycler, int)
     * @see #recycleViewsFromEnd(android.support.v7.widget.RecyclerView.Recycler, int)
     */
    private void recycleByLayoutState(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (!layoutState.mRecycle || layoutState.mInfinite) {
            return;
        }
        if (layoutState.mLayoutDirection == LayoutState.LAYOUT_START) {
            if (mOrientation == RecyclerView.VERTICAL)
                recycleViewsFromEnd(recycler, layoutState.mScrollingOffset);
            else
                recycleViewsFromRight(recycler, layoutState.mScrollingOffset);
        } else {
            if (mOrientation == RecyclerView.VERTICAL)
                recycleViewsFromStart(recycler, layoutState.mScrollingOffset);
            else
                recycleViewsFromLeft(recycler, layoutState.mScrollingOffset);
        }
    }


    /**
     * 从开始的方向回收，方法有点问题，不过问题不大
     */
    private void recycleViewsFromStart(RecyclerView.Recycler recycler, int dt) {
        if (dt < 0) {
            return;
        }
        // ignore padding, ViewGroup may not clip children.
        final int limit = dt;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (mMainOrientationHelper.getDecoratedEnd(child) > limit
                    || mMainOrientationHelper.getTransformedEndWithDecoration(child) > limit) {
                // stop here
                recycleChildren(recycler, 0, i);
                return;
            }
        }
    }

    private void recycleViewsFromEnd(RecyclerView.Recycler recycler, int dt) {
        final int childCount = getChildCount();
        if (dt < 0) {
            return;
        }
        final int limit = mMainOrientationHelper.getEndAfterPadding() - dt;

        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (mMainOrientationHelper.getDecoratedStart(child) < limit
                    || mMainOrientationHelper.getTransformedStartWithDecoration(child) < limit) {
                // stop here
                recycleChildren(recycler, childCount - 1, i);
                return;
            }
        }
    }

    private void recycleViewsFromLeft(RecyclerView.Recycler recycler, int dt) {
        final int childCount = getChildCount();
        if (dt < 0)
            return;
        final int limit = dt;
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child == null)
                continue;
            if (mAssistOrientationHelper.getDecoratedEnd(child) <= limit
                    && mAssistOrientationHelper.getTransformedEndWithDecoration(child) <= limit) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                mLayoutState.removeViewInParent(lp.getViewAdapterPosition());
                removeAndRecycleViewAt(i, recycler);
            }
        }
    }

    private void recycleViewsFromRight(RecyclerView.Recycler recycler, int dt) {
        final int childCount = getChildCount();
        if (dt < 0) {
            return;
        }
        final int limit = mAssistOrientationHelper.getEndAfterPadding() - dt;

        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child == null)
                continue;
            if (mAssistOrientationHelper.getDecoratedStart(child) >= limit
                    && mAssistOrientationHelper.getTransformedEndWithDecoration(child) >= limit) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                mLayoutState.removeViewInParent(lp.getViewAdapterPosition());
                removeAndRecycleViewAt(i, recycler);
            }
        }
    }

    /**
     * Recycles children between given indices.
     *
     * @param startIndex inclusive
     * @param endIndex   exclusive
     */
    private void recycleChildren(RecyclerView.Recycler recycler, int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return;
        }
        if (endIndex > startIndex) {
            for (int i = endIndex - 1; i >= startIndex; i--) {
                View view = getChildAt(i);
                LayoutParams lp = (LayoutParams) view.getLayoutParams();
                mLayoutState.removeViewInParent(lp.getViewAdapterPosition());
                removeAndRecycleViewAt(i, recycler);
            }
        } else {
            for (int i = startIndex; i > endIndex; i--) {
                View view = getChildAt(i);
                LayoutParams lp = (LayoutParams) view.getLayoutParams();
                mLayoutState.removeViewInParent(lp.getViewAdapterPosition());
                removeAndRecycleViewAt(i, recycler);
            }
        }
    }

    private boolean hasMore(RecyclerView.State state, LayoutState layoutState, int orientation, ScreenCoordinateRecorder recorder) {
        int pos;
        if (orientation == RecyclerView.VERTICAL) {
            pos = mCoordinateCallback.covertToPosition(layoutState.mCurRow, recorder.mCurStartColIndex);
        } else {
            pos = mCoordinateCallback.covertToPosition(recorder.mCurStartRowIndex, layoutState.mCurCol);
        }
        return pos != -1 && pos < state.getItemCount();
    }

    /**
     * 当主方向是竖直方向的时候加载子View
     */
    void layoutChunkInVertical(RecyclerView.Recycler recycler, RecyclerView.State state,
                               LayoutState layoutState, LayoutChunkResult result) {
        final boolean layingOutInPrimaryDirection = layoutState.mItemDirection == LayoutState.ITEM_DIRECTION_TAIL;
        // 该行子视图的数量
        int count = 0;
        int remainSpan;
        int consumeMinRow = -1;
        int consumeMinHeight = -1;
        int totalSpan;

        // 一行或者一列的子视图的数量根据偏移会存在变化
        remainSpan = mHorCacheBorders[mHorCacheBorders.length - 1] == 0 ? mHorTotalSpan : mHorTotalSpan + 1;
        totalSpan = remainSpan;

        // 1. 生成子View
        while (count < totalSpan && remainSpan > 0 && layoutState.hasMore(state)) {
            // 判断是否是跨行的视图
            int row = layoutState.mCurRow;
            int col = layoutState.mCurCol;
            int pos = mCoordinateCallback.covertToPosition(row, col);
            mLayoutState.mCurrentPosition = pos;
            // 宽高所占单元格比例
            int[] spanArray = mCoordinateCallback.getSpanArray(pos);
            // 起始坐标
            int[] coordinate = mCoordinateCallback.coordinate(pos);
            int curConsumeRow = coordinate[0] + spanArray[1] - row;
            if (layoutState.isViewExist(pos)) {
                // 如果当前子视图已经存在
                // 记录消耗的行或列 以及 消耗的长度或者宽度即可
                if (consumeMinRow == -1) {
                    consumeMinRow = curConsumeRow;
                    consumeMinHeight = consumeMinRow * mAveHolderHeight;
                } else {
                    consumeMinRow = Math.min(curConsumeRow, consumeMinRow);
                    consumeMinHeight = Math.min(consumeMinHeight, consumeMinRow * mAveHolderHeight);
                }
                int colIndex = coordinate[1] - mScreenCoordinateRecorder.mCurStartColIndex;
                if (colIndex < 0)
                    // **|*
                    // 这里记录的情况是，以上面的 **|* 为例，比如一个单元格占用3列，只有右边的部分显示在屏幕里，其他2/3被隐藏在屏幕外了
                    // 所以这里的消耗数量是屏幕里的一个*
                    remainSpan -= colIndex + spanArray[0];
                else
                    remainSpan -= spanArray[0];

                // 更新位置信息
                layoutState.mCurCol = coordinate[1] + spanArray[0];
                layoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(layoutState.mCurRow, layoutState.mCurCol);
                continue;
            }

            if (spanArray == null || spanArray[0] >= mHorTotalSpan + 1) {
                throw new IllegalArgumentException("UnSupport TableCell Size!");
            }
            if (consumeMinRow == -1) {
                consumeMinRow = curConsumeRow;
                consumeMinHeight = consumeMinRow * mAveHolderHeight;
            } else {
                consumeMinRow = Math.min(consumeMinRow, curConsumeRow);
                consumeMinHeight = Math.min(consumeMinHeight, consumeMinRow * mAveHolderHeight);
            }
            int rowIndex = coordinate[0] - mScreenCoordinateRecorder.mCurStartRowIndex;
            int colIndex = coordinate[1] - mScreenCoordinateRecorder.mCurStartColIndex;
            if (colIndex < 0)
                remainSpan -= colIndex + spanArray[0];
            else
                remainSpan -= spanArray[0];
            // 获取下一个视图
            View view = layoutState.next(recycler, colIndex);
            if (view == null)
                break;

            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();

            layoutParams.mRowIndex = rowIndex;
            layoutParams.mColIndex = colIndex;
            layoutParams.mWidthSpan = spanArray[0];
            layoutParams.mHeightSpan = spanArray[1];
            view.setLayoutParams(layoutParams);

            mSet[count] = view;
            count++;
        }

        if (count == 0) {
            // 有可能是跨行的子View造成的
            result.mFinished = true;
            result.mConsume = consumeMinHeight;
            result.mConsumedRowOrCol = consumeMinRow;
            return;
        }

        // 给子View排序
        mSet = sortView(mSet, count);
        if (layingOutInPrimaryDirection) {
            realLayoutChildrenSpecial(count, layoutState, result);
        } else {
            realLayoutChildren(count, layoutState, result);
        }

        result.mConsumedRowOrCol = consumeMinRow;
        result.mConsume = consumeMinHeight;
        Arrays.fill(mSet, null);
    }

    /**
     * 主方向是横方向
     */
    void layoutChunkInHorizontal(RecyclerView.Recycler recycler, RecyclerView.State state,
                                 LayoutState layoutState, LayoutChunkResult result) {
        final boolean layingOutInPrimaryDirection = layoutState.mItemDirection == LayoutState.ITEM_DIRECTION_TAIL;
        // 该行子视图的数量
        int count = 0;
        int remainSpan;
        int consumeCol = -1;
        int consumeMinWidth = -1;
        int totalSpan;

        remainSpan = mVerCacheBorders[mVerCacheBorders.length - 1] == 0 ? mVerTotalSpan : mVerTotalSpan + 1;
        totalSpan = remainSpan;

        // 1. 生成子View
        while (count < totalSpan && remainSpan > 0 && layoutState.hasMore(state)) {
            // 判断是否是跨行的视图
            int row = layoutState.mCurRow;
            int col = layoutState.mCurCol;
            int pos = mCoordinateCallback.covertToPosition(row, col);
            mLayoutState.mCurrentPosition = pos;
            int[] spanArray = mCoordinateCallback.getSpanArray(pos);
            int[] coordinate = mCoordinateCallback.coordinate(pos);
            int curConsumeCol = coordinate[1] + spanArray[0] - col;

            if (layoutState.isViewExist(pos)) {
                if (consumeCol == -1) {
                    consumeCol = curConsumeCol;
                    consumeMinWidth = consumeCol * mAveHolderWidth;
                } else {
                    consumeCol = Math.min(curConsumeCol, consumeCol);
                    consumeMinWidth = Math.min(consumeMinWidth, consumeCol * mAveHolderWidth);
                }
                int rowIndex = coordinate[0] - mScreenCoordinateRecorder.mCurStartRowIndex;
                if (rowIndex < 0) {
                    remainSpan -= (spanArray[1] + rowIndex);
                } else
                    remainSpan -= spanArray[1];
                layoutState.mCurRow = coordinate[0] + spanArray[1];
                layoutState.mCurrentPosition = mCoordinateCallback.covertToPosition(layoutState.mCurRow, layoutState.mCurCol);
                continue;
            }

            if (spanArray == null || spanArray[1] >= mVerTotalSpan + 1) {
                throw new IllegalArgumentException("UnSupport TableCell Size!");
            }
            if (consumeCol == -1) {
                consumeCol = curConsumeCol;
                consumeMinWidth = consumeCol * mAveHolderWidth;
            } else {
                consumeCol = Math.min(consumeCol, curConsumeCol);
                consumeMinWidth = Math.min(consumeMinWidth, consumeCol * mAveHolderWidth);
            }
            int rowIndex = coordinate[0] - mScreenCoordinateRecorder.mCurStartRowIndex;
            int colIndex = coordinate[1] - mScreenCoordinateRecorder.mCurStartColIndex;
            if (rowIndex < 0) {
                remainSpan -= (spanArray[1] + rowIndex);
            } else
                remainSpan -= spanArray[1];

            View view = layoutState.nextInHorizontal(recycler, rowIndex);
            if (view == null)
                break;

            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.mRowIndex = rowIndex;
            layoutParams.mColIndex = colIndex;
            layoutParams.mWidthSpan = spanArray[0];
            layoutParams.mHeightSpan = spanArray[1];
            view.setLayoutParams(layoutParams);
            mSet[count] = view;
            count++;
        }
        if (count == 0) {
            // 有可能是跨行的子View造成的
            if (consumeCol == -1) {
                result.mFinished = true;
                result.mConsume = 0;
                result.mConsumedRowOrCol = 0;
            } else {
                result.mFinished = true;
                result.mConsumedRowOrCol = consumeCol;
                result.mConsume = consumeMinWidth;
            }
            return;
        }

        // 给子视图排序
        mSet = sortView(mSet, count);

        // 添加子View、测量、定位
        realLayoutChildren(count, layoutState, result);

        result.mConsumedRowOrCol = consumeCol;
        result.mConsume = consumeMinWidth;
        Arrays.fill(mSet, null);
    }

    /**
     * 通用的真实的添加子View、测量子View、定位子View的过程
     * 适用于向上滑动、向左滑动、向右滑动
     *
     * @param count       子视图的数量
     * @param layoutState 布局状态
     * @param result      结果的记录类
     */
    private void realLayoutChildren(int count, LayoutState layoutState, LayoutChunkResult result) {
        // 过程
        // 将一行或者一列子视图 按序 添加进父视图中
        int curPos = 0;
        View child = getChildAt(curPos);
        LayoutParams childLp;
        int[] childCoordinate;
        int curNum = -1;
        if (child != null) {
            childLp = (LayoutParams) child.getLayoutParams();
            childCoordinate = mCoordinateCallback.coordinate(childLp.getViewAdapterPosition());
            curNum = getDetailNum(childCoordinate);
        }

        for (int i = 0; i < count; i++) {
            View view = mSet[i];
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            int[] coordinate = mCoordinateCallback.coordinate(params.getViewAdapterPosition());
            int mNum = getDetailNum(coordinate);

            while (mNum >= curNum && curNum != -1) {
                curPos++;
                child = getChildAt(curPos);
                if (child == null)
                    break;
                childLp = (LayoutParams) child.getLayoutParams();
                childCoordinate = mCoordinateCallback.coordinate(childLp.getViewAdapterPosition());
                curNum = getDetailNum(childCoordinate);
            }

            if (layoutState.mScrapList == null) {
                if (curPos == getChildCount())
                    addView(view);
                else {
                    addView(view, curPos);
                }
            } else {
                if (curPos == getChildCount()) {
                    addDisappearingView(view);
                } else {
                    addDisappearingView(view, curPos);
                }
            }
            curNum = mNum;

            Rect mInsets = new Rect();
            calculateItemDecorationsForChild(view, mInsets);
            // 测量
            measureChild(view, View.MeasureSpec.EXACTLY, mInsets);
            // 布局
            layoutChild(view);

            if (params.isItemRemoved() || params.isItemChanged()) {
                result.mIgnoreConsumed = true;
            }
            result.mFocusable |= view.hasFocusable();
            layoutState.addViewInParent(params.getViewAdapterPosition());
        }
    }

    /**
     * 特殊的添加子视图的过程
     * 适用于向下滑动
     */
    private void realLayoutChildrenSpecial(int count, LayoutState layoutState, LayoutChunkResult result) {
        // 给子视图添加、测量、定位
        int curPos = getChildCount() - 1;
        View child = getChildAt(curPos);
        LayoutParams childLp;
        int[] childCoordinate;
        int curNum = -1;
        if (child != null) {
            childLp = (LayoutParams) child.getLayoutParams();
            childCoordinate = mCoordinateCallback.coordinate(childLp.getViewAdapterPosition());
            curNum = getDetailNum(childCoordinate);
        }

        for (int i = count - 1; i >= 0; i--) {
            View view = mSet[i];
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            int[] coordinate = mCoordinateCallback.coordinate(params.getViewAdapterPosition());
            int mNum = getDetailNum(coordinate);

            while (mNum < curNum && curPos > -1) {
                curPos--;
                child = getChildAt(curPos);
                childLp = (LayoutParams) child.getLayoutParams();
                childCoordinate = mCoordinateCallback.coordinate(childLp.getViewAdapterPosition());
                curNum = getDetailNum(childCoordinate);
            }

            if (layoutState.mScrapList == null) {
                if (curPos == getChildCount() - 1)
                    addView(view);
                else {
                    addView(view, curPos + 1);
                }
            } else {
                if (curPos == getChildCount() - 1) {
                    addDisappearingView(view);
                } else {
                    addDisappearingView(view, curPos + 1);
                }
            }

            Rect mInsets = new Rect();
            calculateItemDecorationsForChild(view, mInsets);
            measureChild(view, View.MeasureSpec.EXACTLY, mInsets);
            layoutChild(view);

            if (params.isItemRemoved() || params.isItemChanged()) {
                result.mIgnoreConsumed = true;
            }
            result.mFocusable |= view.hasFocusable();
            layoutState.addViewInParent(params.getViewAdapterPosition());
        }
    }


    /**
     * 给子视图按照行列的顺序排序
     *
     * @param view  子视图
     * @param count 子视图的数量，因为View[]不一定会填满
     * @return 排完顺序的子视图
     */
    private View[] sortView(View[] view, int count) {
        if (view == null || count == 0)
            return view;

        View[] sortView = new View[view.length];
        int minRow = Integer.MAX_VALUE;
        for (int i = 0; i < count; i++) {
            View v = view[i];
            if (v == null)
                break;
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            int[] coordinate = mCoordinateCallback.coordinate(lp.getViewAdapterPosition());
            minRow = Math.min(coordinate[0], minRow);
        }

        int c = 0;
        while (c < count) {
            for (int i = 0; i < count; i++) {
                View v = view[i];
                LayoutParams lp = (LayoutParams) v.getLayoutParams();
                int[] coordinate = mCoordinateCallback.coordinate(lp.getViewAdapterPosition());
                if (coordinate[0] == minRow) {
                    sortView[c] = v;
                    c++;
                }
            }
            minRow++;
        }
        return sortView;
    }

    /**
     * 将坐标转化为数字，方便比较
     *
     * @param coordinate 数组
     */
    private int getDetailNum(int[] coordinate) {
        if (coordinate == null || coordinate.length < 2)
            return 0;
        return coordinate[0] * 1000 + coordinate[1];
    }


    /**
     * 测量子视图
     *
     * @param view   子视图
     * @param insets 外边距
     */
    private void measureChild(View view, int otherDirParentSpecMode, Rect insets) {
        final LayoutParams lp = (LayoutParams) view.getLayoutParams();
        final int verticalInsets = insets.top + insets.bottom
                + lp.topMargin + lp.bottomMargin;
        final int horizontalInsets = insets.left + insets.right
                + lp.leftMargin + lp.rightMargin;
        final int verticalSpace = getSpaceForSpanRange(RecyclerView.VERTICAL, lp.mHeightSpan);
        final int horizontalSpace = getSpaceForSpanRange(RecyclerView.HORIZONTAL, lp.mWidthSpan);
        final int wSpec;
        final int hSpec;

        wSpec = getChildMeasureSpec(horizontalSpace, otherDirParentSpecMode,
                horizontalInsets, lp.width, true);
        hSpec = getChildMeasureSpec(verticalSpace, getHeightMode(),
                verticalInsets, lp.height, true);

        measureChildWithDecorationsAndMargin(view, wSpec, hSpec, false);
    }

    /**
     * 测量子View
     */
    private void measureChildWithDecorationsAndMargin(View child, int widthSpec, int heightSpec,
                                                      boolean alreadyMeasured) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        final boolean measure;
        if (alreadyMeasured) {
            measure = shouldReMeasureChild(child, widthSpec, heightSpec, lp);
        } else {
            measure = shouldMeasureChild(child, widthSpec, heightSpec, lp);
        }
        if (measure) {
            child.measure(widthSpec, heightSpec);
        }
    }


    private boolean shouldReMeasureChild(View child, int widthSpec, int heightSpec, RecyclerView.LayoutParams lp) {
        return !isMeasurementUpToDate(child.getMeasuredWidth(), widthSpec, lp.width)
                || !isMeasurementUpToDate(child.getMeasuredHeight(), heightSpec, lp.height);
    }

    private boolean shouldMeasureChild(View child, int widthSpec, int heightSpec, RecyclerView.LayoutParams lp) {
        return child.isLayoutRequested()
                || !isMeasurementUpToDate(child.getWidth(), widthSpec, lp.width)
                || !isMeasurementUpToDate(child.getHeight(), heightSpec, lp.height);
    }

    private static boolean isMeasurementUpToDate(int childSize, int spec, int dimension) {
        final int specMode = View.MeasureSpec.getMode(spec);
        final int specSize = View.MeasureSpec.getSize(spec);
        if (dimension > 0 && childSize != dimension) {
            return false;
        }
        switch (specMode) {
            case View.MeasureSpec.UNSPECIFIED:
                return true;
            case View.MeasureSpec.AT_MOST:
                return specSize >= childSize;
            case View.MeasureSpec.EXACTLY:
                return specSize == childSize;
        }
        return false;
    }

    int getSpaceForSpanRange(int orientation, int spanSize) {
        if (orientation == RecyclerView.VERTICAL) {
            return mAveHolderHeight * spanSize;
        } else {
            return mAveHolderWidth * spanSize;
        }
    }

    /**
     * 给子视图定位
     *
     * @param view 子视图
     */
    private void layoutChild(View view) {
        final LayoutParams lp = (LayoutParams) view.getLayoutParams();
        final int[] coordinate = mCoordinateCallback.coordinate(lp.getViewAdapterPosition());
        int left, right, top, bottom;
        if (mOrientation == RecyclerView.VERTICAL) {
            int limit = mHorCacheBorders[mHorCacheBorders.length - 1] == 0 ? mHorCacheBorders.length - 1 : mHorCacheBorders.length;
            // 测量子视图的左边有两种情况
            // 情况一 子视图的长占了多列，子视图的left看不见了，且向外占了多个单元格的宽度
            // 情况二 正常情况下子视图的left
            left = lp.mColIndex < 0 ? mHorCacheBorders[0] + mAveHolderWidth * lp.mColIndex : mHorCacheBorders[lp.mColIndex];
            // right同上
            if (lp.mColIndex + lp.mWidthSpan >= limit)
                right = mHorCacheBorders[limit - 1] + (lp.mColIndex + lp.mWidthSpan - limit + 1) * mAveHolderWidth;
            else
                right = mHorCacheBorders[lp.mColIndex + lp.mWidthSpan];
            if (mLayoutState.mLayoutDirection == LayoutState.LAYOUT_END) {
                top = mLayoutState.mYOffset;
                bottom = mLayoutState.mYOffset + lp.mHeightSpan * mAveHolderHeight;
            } else {
                top = mLayoutState.mYOffset - lp.mHeightSpan * mAveHolderHeight;
                bottom = mLayoutState.mYOffset;
            }
        } else {
            int r = coordinate[0] - mScreenCoordinateRecorder.mCurStartRowIndex;
            int limit = mVerCacheBorders[mVerCacheBorders.length - 1] == 0 ? mVerCacheBorders.length - 1 : mVerCacheBorders.length;
            if (r < 0) {
                top = mVerCacheBorders[0] + r * mAveHolderHeight;
            } else
                top = mVerCacheBorders[r];
            if (r + lp.mHeightSpan >= limit) {
                bottom = mVerCacheBorders[limit - 1] + (r + lp.mHeightSpan - limit + 1) * mAveHolderHeight;
            } else {
                //if (r + lp.mHeightSpan == mVerCacheBorders.length - 1 && mVerCacheBorders[r + lp.mHeightSpan] == 0) {
                //  bottom = mVerCacheBorders[r+lp.m]
                //} else
                bottom = mVerCacheBorders[r + lp.mHeightSpan];
            }

            if (mLayoutState.mLayoutDirection == LayoutState.LAYOUT_END) {
                left = mLayoutState.mXOffset;
                right = mLayoutState.mXOffset + lp.mWidthSpan * mAveHolderWidth;
            } else {
                right = mLayoutState.mXOffset;
                left = mLayoutState.mXOffset - lp.mWidthSpan * mAveHolderWidth;
            }
        }
        layoutDecoratedWithMargins(view, left, top, right, bottom);
    }

    /**
     * @return The final offset amount for children
     */
    private int fixLayoutEndGap(int endOffset, RecyclerView.Recycler recycler,
                                RecyclerView.State state, boolean canOffsetChildren) {
        int gap = mMainOrientationHelper.getEndAfterPadding() - endOffset;
        int fixOffset;
        if (gap > 0) {
            fixOffset = -scrollBy(-gap, recycler, state);
        } else {
            return 0; // nothing to fix
        }
        // move offset according to scroll amount
        endOffset += fixOffset;
        if (canOffsetChildren) {
            // re-calculate gap, see if we could fix it
            gap = mMainOrientationHelper.getEndAfterPadding() - endOffset;
            if (gap > 0) {
                mMainOrientationHelper.offsetChildren(gap);
                return gap + fixOffset;
            }
        }
        return fixOffset;
    }

    /**
     * @return The final offset amount for children
     */
    private int fixLayoutStartGap(int startOffset, RecyclerView.Recycler recycler,
                                  RecyclerView.State state, boolean canOffsetChildren) {
        int gap = startOffset - mMainOrientationHelper.getStartAfterPadding();
        int fixOffset = 0;
        if (gap > 0) {
            // check if we should fix this gap.
            fixOffset = -scrollBy(gap, recycler, state);
        } else {
            return 0; // nothing to fix
        }
        startOffset += fixOffset;
        if (canOffsetChildren) {
            // re-calculate gap, see if we could fix it
            gap = startOffset - mMainOrientationHelper.getStartAfterPadding();
            if (gap > 0) {
                mMainOrientationHelper.offsetChildren(-gap);
                return fixOffset - gap;
            }
        }
        return fixOffset;
    }

    /**
     * Helper class that keeps temporary state while {LayoutManager} is filling out the empty
     * space.
     */
    static class LayoutState {

        static final String TAG = "LLM#LayoutState";

        static final int LAYOUT_START = -1;

        static final int LAYOUT_END = 1;

        static final int INVALID_LAYOUT = Integer.MIN_VALUE;

        static final int ITEM_DIRECTION_HEAD = -1;

        static final int ITEM_DIRECTION_TAIL = 1;

        static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;

        SparseBooleanArray mViewPositionCache = new SparseBooleanArray();

        /**
         * 横向坐标偏移量
         */
        int mHeadXOffset, mHeadYOffset;

        /**
         * 当前的行或列
         */
        int mCurRow, mCurCol;

        CoordinateCallback mCoordinateCallback;

        /**
         * We may not want to recycle children in some cases (e.g. layout)
         */
        boolean mRecycle = true;

        /**
         * Pixel offset where layout should start
         */
        int mYOffset, mXOffset;

        /**
         * Number of pixels that we should fill, in the layout direction.
         */
        int mAvailable;

        /**
         * Current position on the adapter to get the next item.
         */
        int mCurrentPosition;

        /**
         * Defines the direction in which the data adapter is traversed.
         * Should be {@link #ITEM_DIRECTION_HEAD} or {@link #ITEM_DIRECTION_TAIL}
         */
        int mItemDirection;

        /**
         * Defines the direction in which the layout is filled.
         * Should be {@link #LAYOUT_START} or {@link #LAYOUT_END}
         */
        int mLayoutDirection;

        /**
         * Used when LayoutState is constructed in a scrolling state.
         * It should be set the amount of scrolling we can make without creating a new view.
         * Settings this is required for efficient view recycling.
         */
        int mScrollingOffset;

        /**
         * Used if you want to pre-layout items that are not yet visible.
         * The difference with {@link #mAvailable} is that, when recycling, distance laid out for
         * {@link #mExtra} is not considered to avoid recycling visible children.
         */
        int mExtra = 0;

        /**
         * Equal to {@link RecyclerView.State#isPreLayout()}. When consuming scrap, if this value
         * is set to true, we skip removed views since they should not be laid out in post layout
         * step.
         */
        boolean mIsPreLayout = false;

        /**
         * The most recent {@link #scrollBy(int, RecyclerView.Recycler, RecyclerView.State)}
         * amount.
         */
        int mLastScrollDelta;

        /**
         * 上次滑动的方向
         */
        int mLastScrollOrientation;

        /**
         * Used when there is no limit in how many views can be laid out.
         */
        boolean mInfinite;

        /**
         * When LLM needs to layout particular views, it sets this list in which case, LayoutState
         * will only return views from this list and return null if it cannot find an item.
         */
        List<RecyclerView.ViewHolder> mScrapList = null;

        public LayoutState() {

        }

        /**
         * @return true if there are more items in the data adapter
         */
        boolean hasMore(RecyclerView.State state) {
            return mCurrentPosition >= 0
                    && mCurrentPosition < state.getItemCount();
        }

        /**
         * 当前RecyclerView是否已经有Adapter位置是position的子视图
         */
        boolean isViewExist(int position) {
            return mViewPositionCache.get(position);
        }

        /**
         * @param position 数据在Adapter中的位置
         */
        void addViewInParent(int position) {
            mViewPositionCache.put(position, true);
        }

        void removeViewInParent(int position) {
            mViewPositionCache.put(position, false);
        }

        void clear() {
            mViewPositionCache.clear();
        }

        /**
         * Gets the view for the next element that we should layout.
         * Also updates current item index to the next item, based on {@link #mItemDirection}
         *
         * @return The next element that we should layout.
         */
        View next(RecyclerView.Recycler recycler, int colIndex) {
            if (mCoordinateCallback == null)
                throw new IllegalArgumentException("mCoordinateCallback in LayoutState can't be null");
            final View view = recycler.getViewForPosition(mCurrentPosition);

            int[] spanArray = mCoordinateCallback.getSpanArray(mCurrentPosition);
            if (colIndex < 0)
                mCurCol += spanArray[0] + colIndex;
            else
                mCurCol += spanArray[0];
            mCurrentPosition = mCoordinateCallback.covertToPosition(mCurRow, mCurCol);
            return view;
        }

        /**
         * 当主方向为水平方向的时候获取下一个子视图
         */
        View nextInHorizontal(RecyclerView.Recycler recycler, int rowIndex) {
            if (mCoordinateCallback == null)
                throw new IllegalArgumentException("mCoordinateCallback in LayoutState can't be null");
            final View view = recycler.getViewForPosition(mCurrentPosition);

            int[] spanArray = mCoordinateCallback.getSpanArray(mCurrentPosition);
            if (rowIndex < 0)
                mCurRow += rowIndex + spanArray[1];
            else
                mCurRow += spanArray[1];
            mCurrentPosition = mCoordinateCallback.covertToPosition(mCurRow, mCurCol);
            return view;
        }

        /**
         * Returns the next item from the scrap list.
         * <p>
         * Upon finding a valid VH, sets current item position to VH.itemPosition + mItemDirection
         *
         * @return View if an item in the current position or direction exists if not null.
         */
        private View nextViewFromScrapList() {
            final int size = mScrapList.size();
            for (int i = 0; i < size; i++) {
                final View view = mScrapList.get(i).itemView;
                final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (lp.isItemRemoved()) {
                    continue;
                }
                if (mCurrentPosition == lp.getViewLayoutPosition()) {
                    assignPositionFromScrapList(view);
                    return view;
                }
            }
            return null;
        }

        public void assignPositionFromScrapList() {
            assignPositionFromScrapList(null);
        }

        public void assignPositionFromScrapList(View ignore) {
            final View closest = nextViewInLimitedList(ignore);
            if (closest == null) {
                mCurrentPosition = NO_POSITION;
            } else {
                mCurrentPosition = ((RecyclerView.LayoutParams) closest.getLayoutParams())
                        .getViewLayoutPosition();
            }
        }

        public View nextViewInLimitedList(View ignore) {
            int size = mScrapList.size();
            View closest = null;
            int closestDistance = Integer.MAX_VALUE;
            for (int i = 0; i < size; i++) {
                View view = mScrapList.get(i).itemView;
                final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (view == ignore || lp.isItemRemoved()) {
                    continue;
                }
                final int distance = (lp.getViewLayoutPosition() - mCurrentPosition)
                        * mItemDirection;
                if (distance < 0) {
                    continue; // item is not in current direction
                }
                if (distance < closestDistance) {
                    closest = view;
                    closestDistance = distance;
                    if (distance == 0) {
                        break;
                    }
                }
            }
            return closest;
        }

    }

    /**
     * 锚点信息
     */
    static class AnchorInfo {
        OrientationHelper mOrientationHelper;
        int mPosition;
        int mRow = 1;
        int mCol = 1;
        int mCoordinate;
        boolean mLayoutFromEnd;
        boolean mValid;


        AnchorInfo() {
            reset();
        }

        void reset() {
            mPosition = NO_POSITION;
            mCoordinate = INVALID_OFFSET;
            mLayoutFromEnd = false;
            mValid = false;
        }

        /**
         * assigns anchor coordinate from the RecyclerView's padding depending on current
         * layoutFromEnd value
         */
        void assignCoordinateFromPadding() {
            mCoordinate = mLayoutFromEnd
                    ? mOrientationHelper.getEndAfterPadding()
                    : mOrientationHelper.getStartAfterPadding();
        }

        boolean isViewValidAsAnchor(View child, RecyclerView.State state) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            return !lp.isItemRemoved() && lp.getViewLayoutPosition() >= 0
                    && lp.getViewLayoutPosition() < state.getItemCount();
        }

        void assignFromViewAndKeepVisibleRect(View child, int position) {
            final int spaceChange = mOrientationHelper.getTotalSpaceChange();
            if (spaceChange >= 0) {
                assignFromView(child, position);
                return;
            }
            mPosition = position;
            if (mLayoutFromEnd) {
                final int prevLayoutEnd = mOrientationHelper.getEndAfterPadding() - spaceChange;
                final int childEnd = mOrientationHelper.getDecoratedEnd(child);
                final int previousEndMargin = prevLayoutEnd - childEnd;
                mCoordinate = mOrientationHelper.getEndAfterPadding() - previousEndMargin;
                // ensure we did not push child's top out of bounds because of this
                if (previousEndMargin > 0) { // we have room to shift bottom if necessary
                    final int childSize = mOrientationHelper.getDecoratedMeasurement(child);
                    final int estimatedChildStart = mCoordinate - childSize;
                    final int layoutStart = mOrientationHelper.getStartAfterPadding();
                    final int previousStartMargin = mOrientationHelper.getDecoratedStart(child)
                            - layoutStart;
                    final int startReference = layoutStart + Math.min(previousStartMargin, 0);
                    final int startMargin = estimatedChildStart - startReference;
                    if (startMargin < 0) {
                        // offset to make top visible but not too much
                        mCoordinate += Math.min(previousEndMargin, -startMargin);
                    }
                }
            } else {
                final int childStart = mOrientationHelper.getDecoratedStart(child);
                final int startMargin = childStart - mOrientationHelper.getStartAfterPadding();
                mCoordinate = childStart;
                if (startMargin > 0) { // we have room to fix end as well
                    final int estimatedEnd = childStart
                            + mOrientationHelper.getDecoratedMeasurement(child);
                    final int previousLayoutEnd = mOrientationHelper.getEndAfterPadding()
                            - spaceChange;
                    final int previousEndMargin = previousLayoutEnd
                            - mOrientationHelper.getDecoratedEnd(child);
                    final int endReference = mOrientationHelper.getEndAfterPadding()
                            - Math.min(0, previousEndMargin);
                    final int endMargin = endReference - estimatedEnd;
                    if (endMargin < 0) {
                        mCoordinate -= Math.min(startMargin, -endMargin);
                    }
                }
            }
        }

        void assignFromView(View child, int position) {
            if (mLayoutFromEnd) {
                mCoordinate = mOrientationHelper.getDecoratedEnd(child)
                        + mOrientationHelper.getTotalSpaceChange();
            } else {
                mCoordinate = mOrientationHelper.getDecoratedStart(child);
            }

            mPosition = position;
        }

        @Override
        public String toString() {
            return "AnchorInfo{"
                    + "mPosition=" + mPosition
                    + ", mCoordinate=" + mCoordinate
                    + ", mLayoutFromEnd=" + mLayoutFromEnd
                    + ", mValid=" + mValid
                    + '}';
        }
    }

    /**
     * 加载一行或者一列内容的结果记录类
     */
    protected static class LayoutChunkResult {
        // 加载的行数或者列数
        int mConsumedRowOrCol;
        // 当前消耗的高度或者长度
        int mConsume;
        boolean mFinished;
        boolean mIgnoreConsumed;
        boolean mFocusable;

        void resetInternal() {
            mConsumedRowOrCol = 0;
            mConsume = 0;
            mFinished = false;
            mIgnoreConsumed = false;
            mFocusable = false;
        }
    }

    /**
     * 记录屏幕起始的工具类
     */
    public static class ScreenCoordinateRecorder {

        // 屏幕内起始位置的横纵坐标
        private int mCurStartRowIndex, mCurStartColIndex;

        public ScreenCoordinateRecorder() {
            this.mCurStartRowIndex = 1;
            this.mCurStartColIndex = 1;
        }

        /**
         * 设置屏幕内起始位置的横纵坐标
         *
         * @param row 横
         * @param col 列
         */
        public void setCoordinate(int row, int col) {
            this.mCurStartRowIndex = row;
            this.mCurStartColIndex = col;
        }

        /**
         * 获取屏幕内的坐标
         *
         * @return 二维坐标
         */
        public int[] getScreenCoordinate(int row, int col) {
            return new int[]{row - mCurStartRowIndex, col - mCurStartColIndex};
        }
    }

    /**
     * 坐标的辅助工具类
     */
    public interface CoordinateCallback {

        /**
         * 将横纵坐标转化为位置
         *
         * @param row 横坐标
         * @param col 纵坐标
         * @return 数组在List中的位置
         */
        int covertToPosition(int row, int col);

        /**
         * 通过位置获取横纵的Span
         *
         * @param pos 位置
         * @return int[] 0-占了几行 1-占了几列
         */
        int[] getSpanArray(int pos);

        /**
         * 如果是跨行或者跨列，寻找第一个方块
         */
        int[] coordinate(int pos);

    }

    public static class LayoutParams extends RecyclerView.LayoutParams {

        public static final int INVALID_SPAN_ID = -1;

        /**
         * 行或列的屏幕中的坐标
         */
        int mRowIndex = INVALID_SPAN_ID;
        int mColIndex = INVALID_SPAN_ID;

        int mWidthSpan;
        int mHeightSpan;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public int[] getScreenCoordinate() {
            return new int[]{mRowIndex, mColIndex};
        }

        public int[] getSpanArray() {
            return new int[]{mWidthSpan, mHeightSpan};
        }
    }


}
