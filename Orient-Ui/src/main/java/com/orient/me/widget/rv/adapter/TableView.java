package com.orient.me.widget.rv.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.orient.me.R;
import com.orient.me.data.table.ICellItem;
import com.orient.me.widget.rv.layoutmanager.table.TableLayoutManager;
import com.orient.me.widget.rv.rv.NoScrollRecyclerView;
import com.orient.me.widget.rv.rv.TableRecyclerView;

import java.util.List;

public class TableView<Data extends ICellItem> extends FrameLayout implements FirstItemCallback {

    private static final String TAG ="TableView";

    private NoScrollRecyclerView mTitleRv;
    private FrameLayout mTitleHeadFl;
    private NoScrollRecyclerView mLeftRv;
    private TableRecyclerView mTableRv;

    private int mWidth;
    private int mHeight;

    // 适配器
    private TableAdapter<Data> mTableAdapter;
    private GridAdapterProxy<Data> mGridAdapter;
    private LeftAdapterProxy<Data> mLeftAdapter;
    private TitleAdapterProxy<Data> mTitleAdapter;

    private boolean isLeftOpen = true;
    private boolean isTitleOpen = true;

    private int mMode = TableLayoutManager.MODE_A;
    private int w = 4;
    private int h = 8;

    // 边界的监听器
    private BoundCallback callback;

    public TableView(@NonNull Context context) {
        this(context, null);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 设置模型和长度
     *
     * @param mode 模型
     * @param w    长度 Or WidthSpan
     * @param h    高度 or heightSpan
     */
    public void setModeAndValue(int mode, int w, int h) {
        this.mMode = mode;
        this.w = w;
        this.h = h;
    }

    /**
     * 设置是否使用标题和左边的栏
     *
     * @param isLeftOpen 使用坐标的第一栏
     * @param isTopOpen  使用标题栏
     */
    public void setTitle(boolean isLeftOpen, boolean isTopOpen) {
        this.isLeftOpen = isLeftOpen;
        this.isTitleOpen = isTopOpen;

        mLeftRv.setVisibility(isLeftOpen ? VISIBLE : GONE);
        mTitleRv.setVisibility(isTopOpen ? VISIBLE : GONE);
        mTitleHeadFl.setVisibility(isTopOpen ? VISIBLE : GONE);
    }

    /**
     * 设置边界监听器
     * 可以是被是否被滑动到底部或者顶部
     */
    public void setBoundCallback(BoundCallback callback){
        this.callback = callback;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.table_view, this, true);

        mTitleRv = findViewById(R.id.rv_head);
        mTitleHeadFl = findViewById(R.id.fl_head);
        mLeftRv = findViewById(R.id.rv_left);
        mTableRv = findViewById(R.id.rv_table);

        mTitleRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mLeftRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        addScrollerListener();
    }

    public void setAdapter(TableAdapter<Data> adapter) {
        this.mTableAdapter = adapter;
        if (mTableAdapter != null) {
            mGridAdapter = new GridAdapterProxy<>(mTableAdapter);

            final TableLayoutManager tll;
            /*if(mMode == TableLayoutManager.MODE_A && isLeftOpen && isTitleOpen){
                tll = new TableLayoutManager(mMode, w-1, h-1);
            }else if(isLeftOpen && mMode == TableLayoutManager.MODE_C){
                tll = new TableLayoutManager(mMode,w-1,h);
            }else if(isTitleOpen && mMode == TableLayoutManager.MODE_D){
                tll = new TableLayoutManager(mMode,w,h-1);
            }else {
                tll = new TableLayoutManager(mMode,w,h);
            }*/
            tll = new TableLayoutManager(mMode,w,h);
            mTableRv.setLayoutManager(tll);
            mTableRv.setAdapter(mGridAdapter);

            int value[] = tll.getChildViewWidthAndHeight();

            mLeftAdapter = new LeftAdapterProxy<>(mTableAdapter, value[0], value[1]);
            mTitleAdapter = new TitleAdapterProxy<>(mTableAdapter, value[0], value[1]);
            mTableAdapter.setTitleAdapter(mTitleAdapter, mLeftAdapter, mGridAdapter);
            mTableAdapter.setHeaderFirstItemCallback(this);
            mLeftRv.setAdapter(mLeftAdapter);
            mTitleRv.setAdapter(mTitleAdapter);

            if (mTitleAdapter.getItemCount() > 0) {
                setHeadFirstItem();
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void reMeasure(){
        if(mTableAdapter != null){
            TableLayoutManager tll = ((TableLayoutManager)mTableRv.getLayoutManager());

            if(!tll.isColSpan() && !tll.isRowSpan())
                return;

            int value[] = tll.getChildViewWidthAndHeight();
            List<Data> tableData = mGridAdapter.getItems();
            List<Data> leftItems = mLeftAdapter.getItems();
            List<Data> titles = mTitleAdapter.getItems();

            mGridAdapter = new GridAdapterProxy<>(mTableAdapter);
            mGridAdapter.addAllData(tableData);

            tll = new TableLayoutManager(TableLayoutManager.MODE_B, value[0], value[1]);
            mTableRv.setLayoutManager(tll);
            mTableRv.setAdapter(mGridAdapter);

            mLeftAdapter = new LeftAdapterProxy<>(mTableAdapter, value[0], value[1]);
            mLeftAdapter.addAllData(leftItems);
            mTitleAdapter = new TitleAdapterProxy<>(mTableAdapter, value[0], value[1]);
            mTitleAdapter.addAllData(titles);
            mTableAdapter.setTitleAdapter(mTitleAdapter, mLeftAdapter, mGridAdapter);
            mTableAdapter.setHeaderFirstItemCallback(this);
            mLeftRv.setAdapter(mLeftAdapter);
            mTitleRv.setAdapter(mTitleAdapter);

            if (mTitleAdapter.getItemCount() > 0) {
                mTitleHeadFl.removeAllViews();

                setHeadFirstItem();
            }

        }
    }

    void setHeadFirstItem() {
        if (mTitleAdapter == null || mTitleAdapter.getItemCount() == 0)
            return;

        if (mTitleHeadFl.getChildCount() == 1)
            return;

        BaseAdapter.ViewHolder<Data> holder = mTitleAdapter.onCreateViewHolder(mTitleRv, mTitleAdapter.getItemViewType(0));
        mTitleAdapter.onBindViewHolder(holder, -1);
        View item = holder.itemView;
        mTitleHeadFl.addView(item);
    }

    private void addScrollerListener() {
        mTableRv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mTableRv.stopScroll();
                        mLeftRv.stopScroll();
                        mTitleRv.stopScroll();
                        break;
                }
                return false;
            }
        });

        mTableRv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && callback != null) {
                    boolean isToBottom = recyclerView.canScrollVertically(1);
                    boolean isToTop = recyclerView.canScrollVertically(-1);
                    callback.onScrollToTopOrBottom(isToTop, isToBottom);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                TableLayoutManager layoutManager = (TableLayoutManager) mTableRv.getLayoutManager();
                if (layoutManager == null) {
                    throw new IllegalArgumentException("layout error");
                }
                int[] v = layoutManager.getChildViewWidthAndHeight();
                if (dx != 0) {
                    int[] coordinate = layoutManager.getCurrentScreenStartCoordinate();
                    int[] offset = layoutManager.getCurrentFirstOffset();
                    if (coordinate == null || coordinate.length == 0 || offset == null)
                        return;

                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mTitleRv.getLayoutManager();
                    int col = coordinate[1];
                    int xOffset = offset[0];
                    if (linearLayoutManager != null) {
                        if (xOffset < 0) {
                            xOffset += v[0];
                            linearLayoutManager.scrollToPositionWithOffset(col, xOffset);
                        }else {
                            linearLayoutManager.scrollToPositionWithOffset(col-1, xOffset);
                        }
                    }
                }

                if (dy != 0) {
                    int[] coordinate = layoutManager.getCurrentScreenStartCoordinate();
                    int[] offset = layoutManager.getCurrentFirstOffset();
                    if (coordinate == null || coordinate.length == 0 || offset == null)
                        return;

                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mLeftRv.getLayoutManager();
                    int row = coordinate[0];
                    int yOffset = offset[1];
                    if (linearLayoutManager != null) {
                        if (yOffset < 0) {
                            yOffset += v[1];
                            linearLayoutManager.scrollToPositionWithOffset(row, yOffset);
                        }else {
                            linearLayoutManager.scrollToPositionWithOffset(row-1, yOffset);
                        }
                    }
                }
            }
        });
    }




    @Override
    public void titleFirstItemAdd() {
        setHeadFirstItem();

    }

    /**
     * 用来监听是否话
     */
    public interface BoundCallback{
        void onScrollToTopOrBottom(boolean isToTop,boolean isToBottom);
    }


}
