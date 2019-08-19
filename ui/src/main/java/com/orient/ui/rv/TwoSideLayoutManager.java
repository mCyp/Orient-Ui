package com.orient.ui.rv;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class TwoSideLayoutManager extends RecyclerView.LayoutManager {
    // TODO 按照LinearLayoutManager编写

    private static final String TAG = "TwoSideLayoutManager";

    // 开始的一边
    public static final int START_LEFT = 1;
    public static final int START_RIGHT = 2;
    public static final int NO_POSITION = -1;
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;

    private static int DEFAULT_SIDE = START_LEFT;

    // 工具类
    OrientationHelper mOrientationHelper;

    private int mStartSide;
    /**
     * When LayoutManager needs to scroll to a position, it sets this variable and requests a
     * layout which will check this variable and re-layout accordingly.
     */
    private int mPendingScrollPosition = RecyclerView.NO_POSITION;
    /**
     * Used to keep the offset value when {@link #(int, int)} is
     * called.
     */
    int mPendingScrollPositionOffset = INVALID_OFFSET;

    private boolean mStackFromEnd = false;
    private LayoutState mLayoutState;
    // 坐标信息
    private AnchorInfo mAnchorInfo = new AnchorInfo();

    public TwoSideLayoutManager(Context context, int startSide) {
        this.mStartSide = startSide;
        initOrientHelper();
    }

    public TwoSideLayoutManager(Context context) {
        this(context, DEFAULT_SIDE);
    }

    /**
     * 初始化
     */
    public void initOrientHelper() {
        assertNotInLayoutOrScroll(null);
        if (mOrientationHelper == null) {
            mOrientationHelper =
                    OrientationHelper.createOrientationHelper(this, LinearLayout.VERTICAL);
            mAnchorInfo.mOrientationHelper = mOrientationHelper;
            requestLayout();
        }
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 放置Children
        // 1. 判断数量
        if (mPendingScrollPosition != RecyclerView.NO_POSITION) {
            if (state.getItemCount() == 0) {
                removeAndRecycleAllViews(recycler);
            }
        }
        // 1. 回收已经存在的视图
        detachAndScrapAttachedViews(recycler);
        // 初始化LayoutState
        ensureLayoutState();
        mLayoutState.mRecycle = false;
        final View focused = getFocusedChild();
        if (!mAnchorInfo.mValid || mPendingScrollPosition != NO_POSITION) {
            mAnchorInfo.reset();
            // 默认从结束方向开始绘制
            mAnchorInfo.mLayoutFromEnd = true;
            updateAnchorInfoForLayout(recycler, state, mAnchorInfo);
            mAnchorInfo.mValid = true;
        } else if (focused != null && (mOrientationHelper.getDecoratedStart(focused)
                >= mOrientationHelper.getEndAfterPadding()
                || mOrientationHelper.getDecoratedEnd(focused)
                <= mOrientationHelper.getStartAfterPadding())) {
            mAnchorInfo.assignFromViewAndKeepVisibleRect(focused, getPosition(focused));
        }
        final int firstLayoutDirection = LayoutState.ITEM_DIRECTION_TAIL;

        // 2. 填充
        // fill towards end
        int extraForStart = mOrientationHelper.getStartAfterPadding();
        int extraForEnd = mOrientationHelper.getEndAfterPadding();
        int startOffset;
        int endOffset;

        updateLayoutStateToFillEnd(mAnchorInfo);
        mLayoutState.mExtra = extraForEnd;
        //fill(recycler, mLayoutState, state, false);
        endOffset = mLayoutState.mOffset;
        final int lastElement = mLayoutState.mCurrentPosition;
        if (mLayoutState.mAvailable > 0) {
            extraForStart += mLayoutState.mAvailable;
        }
        // fill towards start
        updateLayoutStateToFillStart(mAnchorInfo);
        mLayoutState.mExtra = extraForStart;
        mLayoutState.mCurrentPosition += mLayoutState.mItemDirection;
        //fill(recycler, mLayoutState, state, false);
        startOffset = mLayoutState.mOffset;

        if (mLayoutState.mAvailable > 0) {
            extraForEnd = mLayoutState.mAvailable;
            // start could not consume all it should. add more items towards end
            updateLayoutStateToFillEnd(lastElement, endOffset);
            mLayoutState.mExtra = extraForEnd;
            //fill(recycler, mLayoutState, state, false);
            endOffset = mLayoutState.mOffset;
        }

    }

    void ensureLayoutState() {
        if (mLayoutState == null) {
            mLayoutState = createLayoutState();
        }
    }

    LayoutState createLayoutState() {
        return new LayoutState();
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
        anchorInfo.mPosition = mStackFromEnd ? state.getItemCount() - 1 : 0;
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
                final int childSize = mOrientationHelper.getDecoratedMeasurement(child);
                if (childSize > mOrientationHelper.getTotalSpace()) {
                    // item does not fit. fix depending on layout direction
                    anchorInfo.assignCoordinateFromPadding();
                    return true;
                }
                final int startGap = mOrientationHelper.getDecoratedStart(child)
                        - mOrientationHelper.getStartAfterPadding();
                if (startGap < 0) {
                    anchorInfo.mCoordinate = mOrientationHelper.getStartAfterPadding();
                    anchorInfo.mLayoutFromEnd = false;
                    return true;
                }
                final int endGap = mOrientationHelper.getEndAfterPadding()
                        - mOrientationHelper.getDecoratedEnd(child);
                if (endGap < 0) {
                    anchorInfo.mCoordinate = mOrientationHelper.getEndAfterPadding();
                    anchorInfo.mLayoutFromEnd = true;
                    return true;
                }
                anchorInfo.mCoordinate = anchorInfo.mLayoutFromEnd
                        ? (mOrientationHelper.getDecoratedEnd(child) + mOrientationHelper
                        .getTotalSpaceChange())
                        : mOrientationHelper.getDecoratedStart(child);
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
        anchorInfo.mCoordinate = mOrientationHelper.getStartAfterPadding()
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
                        mOrientationHelper.getDecoratedStart(referenceChild) >= mOrientationHelper
                                .getEndAfterPadding()
                                || mOrientationHelper.getDecoratedEnd(referenceChild)
                                < mOrientationHelper.getStartAfterPadding();
                if (notVisible) {
                    anchorInfo.mCoordinate = anchorInfo.mLayoutFromEnd
                            ? mOrientationHelper.getEndAfterPadding()
                            : mOrientationHelper.getStartAfterPadding();
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

    View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state,
                            int start, int end, int itemCount) {
        ensureLayoutState();
        View invalidMatch = null;
        View outOfBoundsMatch = null;
        final int boundsStart = mOrientationHelper.getStartAfterPadding();
        final int boundsEnd = mOrientationHelper.getEndAfterPadding();
        final int diff = end > start ? 1 : -1;
        for (int i = start; i != end; i += diff) {
            final View view = getChildAt(i);
            final int position = getPosition(view);
            if (position >= 0 && position < itemCount) {
                if (((RecyclerView.LayoutParams) view.getLayoutParams()).isItemRemoved()) {
                    if (invalidMatch == null) {
                        invalidMatch = view; // removed item, least preferred
                    }
                } else if (mOrientationHelper.getDecoratedStart(view) >= boundsEnd
                        || mOrientationHelper.getDecoratedEnd(view) < boundsStart) {
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

    private void updateLayoutStateToFillEnd(AnchorInfo anchorInfo) {
        updateLayoutStateToFillEnd(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    private void updateLayoutStateToFillEnd(int itemPosition, int offset) {
        mLayoutState.mAvailable = mOrientationHelper.getEndAfterPadding() - offset;
        mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_TAIL;
        mLayoutState.mCurrentPosition = itemPosition;
        mLayoutState.mLayoutDirection = LayoutState.LAYOUT_END;
        mLayoutState.mOffset = offset;
        mLayoutState.mScrollingOffset = LayoutState.SCROLLING_OFFSET_NaN;
    }

    private void updateLayoutStateToFillStart(AnchorInfo anchorInfo) {
        updateLayoutStateToFillStart(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    private void updateLayoutStateToFillStart(int itemPosition, int offset) {
        mLayoutState.mAvailable = offset - mOrientationHelper.getStartAfterPadding();
        mLayoutState.mCurrentPosition = itemPosition;
        mLayoutState.mItemDirection = LayoutState.ITEM_DIRECTION_HEAD;
        mLayoutState.mLayoutDirection = LayoutState.LAYOUT_START;
        mLayoutState.mOffset = offset;
        mLayoutState.mScrollingOffset = LayoutState.SCROLLING_OFFSET_NaN;

    }


    // TODO 考虑一下布局的时候需要记录那些状态
    static class LayoutState {
        static final String TAG = "LayoutState";
        static final int LAYOUT_START = -1;
        static final int LAYOUT_END = 1;
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;

        static final int ITEM_DIRECTION_HEAD = -1;
        static final int ITEM_DIRECTION_TAIL = 1;

        static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;

        // 某些情况下可能不想回收
        boolean mRecycle = true;
        // 布局开始的位置
        int mOffset;
        // 可以填充的空间
        int mAvailable;
        // 当前的位置
        int mCurrentPosition;
        // 当前的方向？
        int mItemDirection;
        // 布局方向
        int mLayoutDirection;
        // 滑动状态下的参数
        int mScrollingOffset;
        // 额外距离 is not considered to avoid recycling visible children.
        int mExtra = 0;
        // 预布局
        boolean mIsPreLayout = false;
        // 最近的滑动距离
        int mLastScrollDelta;

        // 是否是无限
        boolean mInfinite;

        // 是否有距离
        boolean hasMore(RecyclerView.State state) {
            return mCurrentPosition >= 0 && mCurrentPosition < state.getItemCount();
        }

        /**
         * 获取下一个子View
         */
        View next(RecyclerView.Recycler recycler) {
            final View view = recycler.getViewForPosition(mCurrentPosition);
            mCurrentPosition += mItemDirection;
            return view;
        }
    }

    // 记录锚点信息
    static class AnchorInfo {
        OrientationHelper mOrientationHelper;
        int mPosition;
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

        @Override
        public String toString() {
            return "AnchorInfo{"
                    + "mPosition=" + mPosition
                    + ", mCoordinate=" + mCoordinate
                    + ", mLayoutFromEnd=" + mLayoutFromEnd
                    + ", mValid=" + mValid
                    + '}';
        }

        boolean isViewValidAsAnchor(View child, RecyclerView.State state) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            return !lp.isItemRemoved() && lp.getViewLayoutPosition() >= 0
                    && lp.getViewLayoutPosition() < state.getItemCount();
        }

        public void assignFromViewAndKeepVisibleRect(View child, int position) {
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

        public void assignFromView(View child, int position) {
            if (mLayoutFromEnd) {
                mCoordinate = mOrientationHelper.getDecoratedEnd(child)
                        + mOrientationHelper.getTotalSpaceChange();
            } else {
                mCoordinate = mOrientationHelper.getDecoratedStart(child);
            }

            mPosition = position;
        }
    }

    protected static class LayoutChunkResult {
        public int mConsumed;
        public boolean mFinished;
        public boolean mIgnoreConsumed;
        public boolean mFocusable;

        void resetInternal() {
            mConsumed = 0;
            mFinished = false;
            mIgnoreConsumed = false;
            mFocusable = false;
        }
    }


    public static class LayoutParams extends RecyclerView.LayoutParams {
        // 无效的边
        public static final int INVALID_SIDE_ID = -1;
        public static final int SIDE_LEFT = 1;
        public static final int SIDE_RIGHT = 2;
        int side = INVALID_SIDE_ID;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

        public int getSide() {
            return side;
        }
    }
}
